package org.mdyk.netsim.view.controller;

import com.google.common.eventbus.Subscribe;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;
import org.controlsfx.dialog.Dialogs;
import org.mdyk.netsim.view.map.MapApp;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventType;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.node.geo.GeoSensorNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.event.IPhenomenonModel;
import org.mdyk.netsim.mathModel.network.GraphEdge;
import org.mdyk.netsim.view.edge.OSMEdge;
import org.mdyk.netsim.view.event.OSMEventView;
import org.mdyk.netsim.view.node.GraphEdgeViewWrapper;
import org.mdyk.netsim.view.node.OSMNodeView;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.*;


public class SenseSimJFXController implements Initializable {

    private static final Logger LOG = Logger.getLogger(SenseSimJFXController.class);
    private HashMap<Integer , OSMNodeView> nodeViews;
    private HashMap<GraphEdge<GeoPosition>, GraphEdgeViewWrapper<OSMEdge>> edgeViews;
    private HashMap<Integer , OSMEventView> eventViews;
    private File scenarioFile = null;

    @FXML
    private TreeView<String> nodesTree;
    @FXML
    private SwingNode swingMapNode;

    private MapApp app;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LOG.debug("JFXML initialization");
        app = new MapApp();
        app.setPreferredSize(new Dimension(950,750));
        createSwingContent(swingMapNode);
        nodeViews = new HashMap<>();
        eventViews = new HashMap<>();
        edgeViews = new HashMap<>();

        nodesTree.setRoot(new TreeItem<>("Nodes"));
        nodesTree.getRoot().setExpanded(true);

        EventBusHolder.getEventBus().register(this);

    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(app));
    }

    public void loadScenarioAction() {
        LOG.debug(">>> loadScenarioAction");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        scenarioFile = fileChooser.showOpenDialog(null);
        EventBusHolder.post(EventType.SCENARIO_LOADED , scenarioFile);
        LOG.debug("<<< loadScenarioAction");
    }

    public void startScenario() {
        LOG.debug(">>> startScenario");

        if(scenarioFile == null) {
            Dialogs.create()
                    .owner(null)
                        .title("No scenario loaded")
                            .message("Cannot start without scenario file.")
                                .showWarning();
        }
        EventBusHolder.post(EventType.SIM_START_NODES, null);

        LOG.debug("<<< startScenario");
    }

    public void addNode(int id, OSMNodeView node) {
        LOG.debug(">> addNode");
        node.renderNode();
        nodeViews.put(id, node);
        TreeItem<String> treeItem = new TreeItem<>("Node " + id);
        this.nodesTree.getRoot().getChildren().add(treeItem);
        LOG.debug("<< addNode");
    }

    public void addEvent(int id, OSMEventView event) {
        LOG.debug(">> addEvent");
        event.renderEvent();
        eventViews.put(id, event);
        LOG.debug("<< addEvent");
    }

    public void createEdge(GraphEdge<GeoPosition> graphEdge) {
        // Api JMapViewer wymaga podania trzech punktów, stąd dla linii
        // druga współrzędna jest powtórzona.
        OSMEdge polygon = new OSMEdge("", nodeViews.get(graphEdge.idA.getID()).getMapMaker(),
                                          nodeViews.get(graphEdge.idB.getID()).getMapMaker(),
                                          nodeViews.get(graphEdge.idB.getID()).getMapMaker());

        GraphEdgeViewWrapper<OSMEdge> edgeViewWrapper = new GraphEdgeViewWrapper<>(graphEdge.idA.getID(),graphEdge.idB.getID(),polygon);
        edgeViews.put(graphEdge,edgeViewWrapper);

        app.getMapContainer().addMapPolygon(polygon);
    }

    public void removeEdge(GraphEdge<GeoPosition> graphEdge){
        LOG.trace(">> removeEdge: " + graphEdge);
        if(edgeViews.containsKey(graphEdge)){
            LOG.debug("edgeViews.containsKey(graphEdge=" + graphEdge.toString() + ")");
            GraphEdgeViewWrapper<OSMEdge> edgeViewWrapper = edgeViews.get(graphEdge);
            app.getMapContainer().removeMapPolygon(edgeViewWrapper.getView());
            edgeViews.remove(graphEdge);
        }
        LOG.trace("<< removeEdge");
    }

    @Subscribe
    public synchronized void processEvent(InternalEvent event) {
        LOG.debug(">> processEvent");
        GeoSensorNode sensorModelNode;
        switch(event.getEventType()){
            case NEW_NODE:
                LOG.debug("NEW_NODE event");
                sensorModelNode = (GeoSensorNode) event.getPayload();
                OSMNodeView nodeView = new OSMNodeView(sensorModelNode, app.getMapContainer());
                addNode(sensorModelNode.getID(), nodeView);
                break;
            case NODE_POSITION_CHANGED:
                sensorModelNode = (GeoSensorNode) event.getPayload();
                nodeViews.get(sensorModelNode.getID()).relocate(sensorModelNode.getPosition());
                break;
            case EDGE_ADDED:
                LOG.debug("EDGE_ADDED event");
                GraphEdge newEdge = (GraphEdge) event.getPayload();
                createEdge(newEdge);
                break;
            case EDGE_REMOVED:
                LOG.debug("EDGE_REMOVED event");
                GraphEdge edge = (GraphEdge) event.getPayload();
                removeEdge(edge);
                break;
            case PHENOMENON_CREATED:
                LOG.debug("PHENOMENON_CREATED event");
                OSMEventView envEvent = new OSMEventView((IPhenomenonModel<GeoPosition>)event.getPayload(), app.getMapContainer(), "");
                addEvent(1,envEvent);
                break;
            case NODE_START_SENSE:
                sensorModelNode = (GeoSensorNode) event.getPayload();
                nodeViews.get(sensorModelNode.getID()).startSense();
                break;
            case NODE_END_SENSE:
                sensorModelNode = (GeoSensorNode) event.getPayload();
                nodeViews.get(sensorModelNode.getID()).stopSense();
                break;
        }
        LOG.debug("<< processEvent");
    }


}
