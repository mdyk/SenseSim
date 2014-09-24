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

    private static final Logger logger = Logger.getLogger(SenseSimJFXController.class);
    private HashMap<Integer , OSMNodeView> nodeViews;
    private HashMap<GraphEdge<GeoPosition>, GraphEdgeViewWrapper<OSMEdge>> edgeViews;
    private HashMap<Integer , OSMEventView> eventViews;
    private FileChooser fileChooser;
    private File scenarioFile = null;

    @FXML
    private TreeView<String> nodesTree;
    @FXML
    private SwingNode swingMapNode;

    private MapApp app;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.debug("JFXML initialization");
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
        logger.debug(">>> loadScenarioAction");
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        scenarioFile = fileChooser.showOpenDialog(null);
        EventBusHolder.post(EventType.SCENARIO_LOADED , scenarioFile);
        logger.debug("<<< loadScenarioAction");
    }

    public void startScenario() {
        logger.debug(">>> startScenario");

        if(scenarioFile == null) {
            Dialogs.create()
                    .owner(null)
                        .title("No scenario loaded")
                            .message("Cannot start without scenario file.")
                                .showWarning();
        }
        EventBusHolder.post(EventType.SIM_START_NODES, null);

        logger.debug("<<< startScenario");
    }

//    public SenseSimJFXController(MapApp app) {
//        this.app = app;
////        this.networkManager = NetworkManager.getInstance();
//        nodeViews = new HashMap<>();
//        edgeViews = new HashMap<>();
//        eventViews = new HashMap<>();
//        EventBusHolder.getEventBus().register(this);
////        registerEvents();
//    }

//    public void registerEvents(){
//        app.getPause().setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                logger.info("Pause nodes");
//                EventBusHolder.getEventBus().post(new InternalEvent(EventType.SIM_PAUSE_NODES, null));
//            }
//        });
//
//        app.getResume().setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                logger.info("Resume nodes");
//                EventBusHolder.getEventBus().post(new InternalEvent(EventType.SIM_RESUME_NODES, null));
//            }
//        });
//
//        app.getStart().setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                logger.info("Run nodes");
//                EventBusHolder.getEventBus().post(new InternalEvent(EventType.SIM_START_NODES, null));
//            }
//        });
//
//        app.getStop().setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                logger.info("Stop nodes");
//                EventBusHolder.getEventBus().post(new InternalEvent(EventType.SIM_STOP_NODES, null));
//            }
//        });
//
//        app.getMapContainer().addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(java.awt.event.MouseEvent e) {
//                if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
//                    app.getMapContainer().getAttribution().handleAttribution(e.getPoint(), true);
//                }
//            }
//        });
//
//        app.getMapContainer().addMouseMotionListener(new MouseAdapter() {
//            @Override
//            public void mouseMoved(java.awt.event.MouseEvent e) {
////                Point p = e.getPoint();
////                boolean cursorHand = app.getMapContainer().getAttribution().handleAttributionCursor(p);
////                if (cursorHand) {
////                    app.getMapContainer().setCursor(new Cursor(Cursor.HAND_CURSOR));
////                } else {
////                    app.getMapContainer().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
////                }
//            }
//        });
//
//    }

    public void addNode(int id, OSMNodeView node) {
        logger.debug(">> addNode");
        node.renderNode();
        nodeViews.put(id, node);
        TreeItem<String> treeItem = new TreeItem<>("Node " + id);
        this.nodesTree.getRoot().getChildren().add(treeItem);
        logger.debug("<< addNode");
    }

    public void addEvent(int id, OSMEventView event) {
        logger.debug(">> addEvent");
        event.renderEvent();
        eventViews.put(id, event);
        logger.debug("<< addEvent");
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
        logger.trace(">> removeEdge: " + graphEdge);
        if(edgeViews.containsKey(graphEdge)){
            logger.debug("edgeViews.containsKey(graphEdge="+graphEdge.toString()+")");
            GraphEdgeViewWrapper<OSMEdge> edgeViewWrapper = edgeViews.get(graphEdge);
            app.getMapContainer().removeMapPolygon(edgeViewWrapper.getView());
            edgeViews.remove(graphEdge);
        }
        logger.trace("<< removeEdge");
    }

    @Subscribe
    public synchronized void processEvent(InternalEvent event) {
        logger.debug(">> processEvent");
        GeoSensorNode sensorModelNode;
        switch(event.getEventType()){
            case NEW_NODE:
                logger.debug("NEW_NODE event");
                sensorModelNode = (GeoSensorNode) event.getPayload();
                OSMNodeView nodeView = new OSMNodeView(sensorModelNode, app.getMapContainer());
                addNode(sensorModelNode.getID(), nodeView);
                break;
            case NODE_POSITION_CHANGED:
                sensorModelNode = (GeoSensorNode) event.getPayload();
                nodeViews.get(sensorModelNode.getID()).relocate(sensorModelNode.getPosition());
                break;
            case EDGE_ADDED:
                logger.debug("EDGE_ADDED event");
                GraphEdge newEdge = (GraphEdge) event.getPayload();
                createEdge(newEdge);
                break;
            case EDGE_REMOVED:
                logger.debug("EDGE_REMOVED event");
                GraphEdge edge = (GraphEdge) event.getPayload();
                removeEdge(edge);
                break;
            case PHENOMENON_CREATED:
                logger.debug("PHENOMENON_CREATED event");
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
        logger.debug("<< processEvent");
    }


}
