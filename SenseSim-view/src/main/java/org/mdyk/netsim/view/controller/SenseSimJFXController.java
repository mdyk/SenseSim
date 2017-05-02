package org.mdyk.netsim.view.controller;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import org.apache.log4j.Logger;
import org.controlsfx.dialog.Dialogs;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.event.EventType;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.node.geo.GeoDeviceNode;
import org.mdyk.netsim.logic.scenario.Scenario;
import org.mdyk.netsim.logic.scenario.xml.XMLScenario;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.network.GraphEdge;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.view.edge.OSMEdge;
import org.mdyk.netsim.view.event.OSMEventView;
import org.mdyk.netsim.view.map.MapApp;
import org.mdyk.netsim.view.node.GraphEdgeViewWrapper;
import org.mdyk.netsim.view.node.OSMNodeView;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class SenseSimJFXController implements Initializable {

    private static final Logger LOG = Logger.getLogger(SenseSimJFXController.class);
    private HashMap<Integer , OSMNodeView> nodeViews;
    private HashMap<GraphEdge<GeoPosition>, GraphEdgeViewWrapper<OSMEdge>> edgeViews;
    private HashMap<Integer , OSMEventView> eventViews;
    private HashMap<Integer , TabPane> nodesAbilities;

    private File scenarioFile = null;

    @FXML
    private TreeView<String> nodesTree;
    @FXML
    private SwingNode swingMapNode;
    @FXML
    private Pane observationsPane;
    @FXML
    private TextField details_nodeId;
    @FXML
    private TextField nodeLatitude;
    @FXML
    private TextField nodeLongitude;

    private MapApp app;

    private OSMNodeView selectedNode;

    private Stage informationNeedConsole;

    private Stage programConsole;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LOG.debug("JFXML initialization");
        app = new MapApp();
        app.setPreferredSize(new Dimension(950,750));
        createSwingContent(swingMapNode);
        clearGUI();
        EventBusHolder.getEventBus().register(this);
    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(app));
    }

    public void openConsole() {

        if (selectedNode == null) {
            Dialogs.create()
                    .owner(null)
                    .title("No device seleceted")
                    .message("Cannot open console. Select node first.")
                    .showWarning();
            return;
        }

        selectedNode.showConsole();
    }

    public void processInformationNeed() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/InformationNeedConsole.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            informationNeedConsole = new Stage();
            informationNeedConsole.initModality(Modality.NONE);
            informationNeedConsole.initStyle(StageStyle.DECORATED);
            informationNeedConsole.setScene(new Scene(parent));
            InformationNeedConsoleController controller = fxmlLoader.getController();
            controller.populateDevices(nodeViews);
            informationNeedConsole.show();
        } catch (IOException e) {
            LOG.error(e.getMessage() , e);
        }
    }

    public void loadProgram() {
        LOG.debug(">> loadProgram");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ProgramConsole.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            programConsole = new Stage();
            programConsole.initModality(Modality.NONE);
            programConsole.initStyle(StageStyle.DECORATED);
            programConsole.setScene(new Scene(parent));
            ProgramConsoleController controller = fxmlLoader.getController();
            controller.populateDevices(nodeViews);
            programConsole.show();
        } catch (IOException e) {
            LOG.error(e.getMessage() , e);
        }

        LOG.debug("<< loadProgram");
    }

    private void clearGUI() {

        app.getMapContainer().removeAllMapMarkers();
        app.getMapContainer().removeAllMapPolygons();
        app.getMapContainer().removeAllMapRectangles();

        nodeViews = new HashMap<>();
        eventViews = new HashMap<>();
        edgeViews = new HashMap<>();
        nodesAbilities = new HashMap<>();

        nodesTree.setRoot(new TreeItem<>("Nodes"));
        nodesTree.getRoot().setExpanded(true);

        nodesTree.getSelectionModel().selectedItemProperty().addListener((observableValue, stringTreeItem, stringTreeItem2) -> {
            Platform.runLater(() -> {
                details_nodeId.setText(observableValue.getValue().getValue());
                selectedNode = nodeViews.get(Integer.parseInt(observableValue.getValue().getValue()));

                if(selectedNode != null) {
                    nodeLatitude.setText(String.valueOf(selectedNode.getLat()));
                    nodeLongitude.setText(String.valueOf(selectedNode.getLon()));
                }
                observationsPane.getChildren().clear();
                observationsPane.getChildren().add(nodesAbilities.get(selectedNode.getID()));
            });
        });

    }

    public void loadScenarioAction() {
        LOG.debug(">>> loadScenarioAction");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        scenarioFile = fileChooser.showOpenDialog(null);
        EventBusHolder.post(EventType.SCENARIO_FILE_LOADED, scenarioFile);
        LOG.debug("<<< loadScenarioAction");
    }

    public void reloadScenarioAction() {
        LOG.debug(">>> reloadScenarioAction");
        clearGUI();
        File newScenarioFile = new File(scenarioFile.getAbsolutePath());
        scenarioFile = newScenarioFile;
        EventBusHolder.post(EventType.SCENARIO_FILE_RELOADED, scenarioFile);
        LOG.debug("<<< reloadScenarioAction");   }

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
        TreeItem<String> treeItem = new TreeItem<>(""+id);
        this.nodesTree.getRoot().getChildren().add(treeItem);

        TabPane abilityPane = new TabPane();

        this.nodesAbilities.put(id , abilityPane);

        for(AbilityType ability : node.getAbilities()) {
            Tab abilityTab = new Tab(ability.name());
            TableView<PhenomenonValue> abilityTable = new TableView<>();


            TableColumn<PhenomenonValue, String> timeColumn = new TableColumn<>("Time");
            TableColumn<PhenomenonValue, Object> valueColumn = new TableColumn<>("Value");

            timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

            abilityTab.setContent(abilityTable);

            abilityTable.getColumns().addAll(timeColumn, valueColumn);
            nodesAbilities.get(id).getTabs().add(abilityTab);
        }

        LOG.debug("<< addNode");
    }

    public void addEvent(int id, OSMEventView event) {
        LOG.debug(">> addEvent");
        event.renderEvent();
        eventViews.put(id, event);
        LOG.debug("<< addEvent");
    }

    public void createEdge(int commIntId, GraphEdge<GeoPosition> graphEdge) {
        // Api JMapViewer wymaga podania trzech punktów, stąd dla linii
        // druga współrzędna jest powtórzona.
        OSMEdge polygon = new OSMEdge("", nodeViews.get(graphEdge.idA.getID()).getMapMaker(),
                                          nodeViews.get(graphEdge.idB.getID()).getMapMaker(),
                                          nodeViews.get(graphEdge.idB.getID()).getMapMaker());

        switch (commIntId) {
            case 1:
                polygon.setColor(Color.RED);
                break;

            case 2:
                polygon.setColor(Color.BLUE);
                break;

            case 3:
                polygon.setColor(Color.GREEN);
                break;

            case 4:
                polygon.setColor(Color.YELLOW);
                break;

            case 5:
                polygon.setColor(Color.GRAY);
                break;

            default:
                polygon.setColor(Color.BLACK);
                break;
        }

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

    @SuppressWarnings("unchecked")
    private void process_NODE_END_SENSE(GeoDeviceNode sensor) {
        LOG.trace(">> process_NODE_END_SENSE id: " + sensor.getID());
        nodeViews.get(sensor.getID()).stopSense();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(selectedNode != null && selectedNode.getID() == sensor.getID()) {
                    for(Tab tab : nodesAbilities.get(sensor.getID()).getTabs()) {
                        TableView<PhenomenonValue> abilityTable = (TableView<PhenomenonValue>) tab.getContent();
                        List<PhenomenonValue> observations = sensor.old_getObservations().get(AbilityType.valueOf(tab.getText()));

                        if(observations != null) {

                            List<PhenomenonValue> observationsSublist;
                            if(observations.size() > 50) {
                                observationsSublist = observations.subList(observations.size()-49 , observations.size()-1);
                            } else {
                                observationsSublist = observations;
                            }

                            observationsSublist.sort(new PhenomenonValue.DescTimeComparator());
                            ObservableList<PhenomenonValue> observationsData = FXCollections.observableArrayList();

                            observationsData.addAll(observationsSublist);
                            abilityTable.setItems(observationsData);
                        }
                    }
                }
            }
        });

        LOG.trace("<< process_NODE_END_SENSE id: " + sensor.getID());
    }

    @Subscribe
    public synchronized void processEvent(InternalEvent event) {
        try {
            GeoDeviceNode sensorModelNode;
            switch (event.getEventType()) {
                case SCENARIO_LOADED:
                    LOG.debug("SCENARIO_LOADED event");
                    Scenario scenario = (Scenario) event.getPayload();
                    app.getMapContainer().setDisplayPositionByLatLon(scenario.getScenarioRegionPoints().get(0).getLatitude(),
                            scenario.getScenarioRegionPoints().get(0).getLongitude(), 15);

                    if (scenario instanceof XMLScenario) {
                        XMLScenario xmlScenario = (XMLScenario) scenario;
                        this.scenarioFile = xmlScenario.getScenarioFile();
                    }

                    break;
                case NEW_NODE:
                    LOG.debug("NEW_NODE event");
                    sensorModelNode = (GeoDeviceNode) event.getPayload();
                    OSMNodeView nodeView = new OSMNodeView(sensorModelNode, app.getMapContainer());
                    addNode(sensorModelNode.getID(), nodeView);
                    break;
                case NODE_POSITION_CHANGED:
                    sensorModelNode = (GeoDeviceNode) event.getPayload();
                    OSMNodeView node = nodeViews.get(sensorModelNode.getID());
                    node.relocate(sensorModelNode.getPosition());
                    actualizePositionLabel();
                    break;
                case EDGE_ADDED:
                    LOG.debug("EDGE_ADDED event");
                    HashMap<Integer, GraphEdge> payload = (HashMap<Integer, GraphEdge>) event.getPayload();
                    int commIntId = payload.keySet().iterator().next();
//                GraphEdge newEdge = (GraphEdge) event.getPayload();
                    createEdge(commIntId,payload.get(commIntId));
                    break;
                case EDGE_REMOVED:
                    LOG.debug("EDGE_REMOVED event");
                    payload = (HashMap<Integer, GraphEdge>) event.getPayload();
                    commIntId = payload.keySet().iterator().next();
                    removeEdge(payload.get(commIntId));
                    break;
                case PHENOMENON_CREATED:
                    LOG.debug("PHENOMENON_CREATED event");
                    OSMEventView envEvent = new OSMEventView((PhenomenonModel<GeoPosition>) event.getPayload(), app.getMapContainer(), "");
                    addEvent(1, envEvent);
                    break;
                case NODE_START_SENSE:
                    sensorModelNode = (GeoDeviceNode) event.getPayload();
                    nodeViews.get(sensorModelNode.getID()).startSense();
                    break;
                case NODE_END_SENSE:
                    sensorModelNode = (GeoDeviceNode) event.getPayload();
                    process_NODE_END_SENSE(sensorModelNode);
                    break;
            }
        } catch(Throwable tr){
            LOG.error(tr.getMessage() , tr);
        }

    }

    private void actualizePositionLabel() {
        Platform.runLater(() -> {
            if(selectedNode != null) {
                nodeLatitude.setText(String.valueOf(selectedNode.getLat()));
                nodeLongitude.setText(String.valueOf(selectedNode.getLon()));
            }
        });
    }

}
