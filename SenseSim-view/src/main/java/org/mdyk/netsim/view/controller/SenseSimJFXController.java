package org.mdyk.netsim.view.controller;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.apache.log4j.Logger;
import org.controlsfx.dialog.Dialogs;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.view.map.MapApp;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventType;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.node.geo.GeoSensorNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.phenomena.IPhenomenonModel;
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
import java.util.List;


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LOG.debug("JFXML initialization");
        app = new MapApp();
        app.setPreferredSize(new Dimension(950,750));
        createSwingContent(swingMapNode);
        nodeViews = new HashMap<>();
        eventViews = new HashMap<>();
        edgeViews = new HashMap<>();
        nodesAbilities = new HashMap<>();

        nodesTree.setRoot(new TreeItem<>("Nodes"));
        nodesTree.getRoot().setExpanded(true);

        nodesTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observableValue, TreeItem<String> stringTreeItem, TreeItem<String> stringTreeItem2) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        details_nodeId.setText(observableValue.getValue().getValue());
                        selectedNode = nodeViews.get(Integer.parseInt(observableValue.getValue().getValue()));
//                actualizePositionLabel();
                        if(selectedNode != null) {
                            nodeLatitude.setText(String.valueOf(selectedNode.getLat()));
                            nodeLongitude.setText(String.valueOf(selectedNode.getLon()));
                        }
                        observationsPane.getChildren().clear();
                        observationsPane.getChildren().add(nodesAbilities.get(selectedNode.getID()));
                    }
                });
            }
        });

        EventBusHolder.getEventBus().register(this);
    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(app));
    }

    public void loadProgram() {
        LOG.debug(">> loadProgram");
        Dialog<Pair<Integer, String>> dialog = new Dialog<>();
        dialog.setTitle("Write program for the network");
        dialog.setHeaderText("Here you can macroprogram you network.");
        ButtonType loginButtonType = new ButtonType("Send program", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ChoiceBox<Integer> nodeId = new ChoiceBox<>(FXCollections.observableArrayList(nodeViews.keySet()));
        CheckBox sendToAll = new CheckBox();
        TextArea codeArea = new TextArea();
        codeArea.setMinHeight(600);
        codeArea.setMinWidth(800);
        codeArea.setPromptText("Program");

        grid.add(new Label("Node's id:"), 0, 0);
        grid.add(nodeId, 1, 0);
        grid.add(new Label("Send program to all nodes"),0,1);
        grid.add(sendToAll,1,1);
        grid.add(new Label("Code:"), 0, 2);
        grid.add(codeArea, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(nodeId::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(nodeId.getValue(), codeArea.getText());
            }
            return null;
        });

        Optional<Pair<Integer, String>> result = dialog.showAndWait();

        result.ifPresent(program -> {
            EventBusHolder.getEventBus().post(EventFactory.loadProgram(program.getKey(),program.getValue()));
        });
        LOG.debug("<< loadProgram");
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

    @SuppressWarnings("unchecked")
    private void process_NODE_END_SENSE(GeoSensorNode sensor) {
        LOG.trace(">> process_NODE_END_SENSE id: " + sensor.getID());
        nodeViews.get(sensor.getID()).stopSense();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(selectedNode != null && selectedNode.getID() == sensor.getID()) {
                    for(Tab tab : nodesAbilities.get(sensor.getID()).getTabs()) {
                        TableView<PhenomenonValue> abilityTable = (TableView<PhenomenonValue>) tab.getContent();
                        List<PhenomenonValue> observations = sensor.getObservations().get(AbilityType.valueOf(tab.getText()));

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
                OSMNodeView node = nodeViews.get(sensorModelNode.getID());
                node.relocate(sensorModelNode.getPosition());
                actualizePositionLabel();
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
                process_NODE_END_SENSE(sensorModelNode);
                break;
        }
        LOG.debug("<< processEvent");
    }

    private void actualizePositionLabel() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(selectedNode != null) {
                    nodeLatitude.setText(String.valueOf(selectedNode.getLat()));
                    nodeLongitude.setText(String.valueOf(selectedNode.getLon()));
                }
            }
        });
    }

}
