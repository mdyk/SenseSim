package org.mdyk.netsim.view.controller;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.view.node.OSMNodeView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class SensorConsoleController implements Initializable {

    private static final Logger LOG = Logger.getLogger(SensorConsoleController.class);

    private OSMNodeView nodeView;

    @FXML
    private TabPane consoleTabPane;

    @FXML
    private TextField nodeBandwidth;

    @FXML
    private TextField latitude;

    @FXML
    private TextField longitude;

    @FXML
    private TextField velocity;

    @FXML
    private Label consoleLabel;

    @FXML
    private TextField radioRange;

    @FXML
    private ComboBox abilityChooser;

    @FXML
    private TableView<PhenomenonValue> abilityTable;

    @FXML
    private TableColumn<PhenomenonValue, String> simTimeColumn;

    @FXML
    private TableColumn<PhenomenonValue, Object> observationsColumn;


    private ObservableList<PhenomenonValue> observationsData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBusHolder.getEventBus().register(this);
//        abilityTable.
//        simTimeColumn = new TableColumn<>();
//        observationsColumn = new TableColumn<>();

        simTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        observationsColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        abilityTable.setItems(observationsData);

//        abilityTable.getColumns().addAll(simTimeColumn, observationsColumn);


    }


    @SuppressWarnings("unchecked")
    public void fillGui() {
        consoleLabel.setText("Sensor " + nodeView.getID());
        nodeBandwidth.setText(Double.toString(nodeView.getNode().getWirelessBandwith()));
        velocity.setText(Double.toString(nodeView.getNode().getVelocity()));
        radioRange.setText(Double.toString(nodeView.getNode().getRadioRange()));

        ObservableList<String> abilities = FXCollections.observableArrayList();
        abilities.addAll(nodeView.getAbilitesNames());
        abilityChooser.setItems(abilities);

        actualizePositionLabel();
    }

    public void setNodeView(OSMNodeView nodeView) {
        this.nodeView = nodeView;
    }


    @Subscribe
    public synchronized void processEvent(InternalEvent event) {
        LOG.debug(">> processEvent");
        switch(event.getEventType()){
            case NODE_POSITION_CHANGED:
                actualizePositionLabel();
                break;
            case NODE_END_SENSE:
                showObservationsForAbility();
                break;
        }
        LOG.debug("<< processEvent");
    }

    public void showObservationsForAbility() {

        String abilityName = (String) abilityChooser.getValue();
        List<PhenomenonValue> observations = nodeView.getNode().getObservations().get(AbilityType.valueOf(abilityName));

//        if(abilityTable.getColumns() != null) {
//            abilityTable.getColumns().clear();
//        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(observations != null) {

                    List<PhenomenonValue> observationsSublist;
                    if(observations.size() > 50) {
                        observationsSublist = observations.subList(observations.size()-49 , observations.size()-1);
                    } else {
                        observationsSublist = observations;
                    }

                    observationsSublist.sort(new PhenomenonValue.DescTimeComparator());

                    observationsData.clear();
                    observationsData.addAll(observationsSublist);
//                    abilityTable.setItems(observationsData);
                }
            }
        });

    }

    private void actualizePositionLabel() {
        OSMNodeView osmNodeView = (OSMNodeView) nodeView;
        Platform.runLater(() -> {
            latitude.setText(Double.toString(osmNodeView.getLat()));
            longitude.setText(Double.toString(osmNodeView.getLon()));
        });
    }

}
