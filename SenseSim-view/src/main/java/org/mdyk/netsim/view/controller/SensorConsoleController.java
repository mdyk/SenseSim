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
import org.mdyk.netsim.logic.communication.process.CommunicationProcess;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.node.program.SensorProgram;
import org.mdyk.netsim.logic.node.statistics.SensorStatistics;
import org.mdyk.netsim.logic.node.statistics.event.StatisticsEvent;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.view.controlls.table.CommunicationStatistics;
import org.mdyk.netsim.view.controlls.table.ProgramStatistics;
import org.mdyk.netsim.view.node.OSMNodeView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class SensorConsoleController implements Initializable {

    private static final Logger LOG = Logger.getLogger(SensorConsoleController.class);

    private enum CommType {Incoming, Outgoing}

    private OSMNodeView nodeView;

    private SensorStatistics statistics;

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
    private ComboBox commTypChooser;

    @FXML
    private TableView<PhenomenonValue> abilityTable;

    @FXML
    private TableColumn<PhenomenonValue, String> simTimeColumn;

    @FXML
    private TableColumn<PhenomenonValue, Object> observationsColumn;

    @FXML
    private TableView<CommunicationStatistics> commTable;

    @FXML
    private TableColumn<CommunicationStatistics, Integer> commId;

    @FXML
    private TableColumn<CommunicationStatistics, String> commReceiver;

    @FXML
    private TableColumn<CommunicationStatistics, String> commStatus;

    @FXML
    private TableColumn<CommunicationStatistics, String> commStartTime;

    @FXML
    private TableColumn<CommunicationStatistics, String> commEndTime;

    @FXML
    private TableColumn<CommunicationStatistics, String> commMessageSize;

    @FXML
    private TableView<ProgramStatistics> programsTable;

    @FXML
    private TableColumn<ProgramStatistics , String> pidColumn;

    @FXML
    private TableColumn<ProgramStatistics , String> programStatus;

    private ObservableList<PhenomenonValue> observationsData = FXCollections.observableArrayList();
    private ObservableList<CommunicationStatistics> commStatistics = FXCollections.observableArrayList();
    private ObservableList<ProgramStatistics> programStatistics = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBusHolder.getEventBus().register(this);

        simTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        observationsColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        abilityTable.setItems(observationsData);

        commId.setCellValueFactory(new PropertyValueFactory<>("commId"));
        commReceiver.setCellValueFactory(new PropertyValueFactory<>("receiverId"));
        commStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        commStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        commEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        commMessageSize.setCellValueFactory(new PropertyValueFactory<>("messageSize"));
        commTable.setItems(commStatistics);

        pidColumn.setCellValueFactory(new PropertyValueFactory<>("PID"));
        programStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        programsTable.setItems(programStatistics);

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

        ObservableList<String> commTypes = FXCollections.observableArrayList();
        commTypes.add(CommType.Incoming.name());
        commTypes.add(CommType.Outgoing.name());

        commTypChooser.setItems(commTypes);
        commTypChooser.setValue(CommType.Outgoing.name());

        actualizePositionLabel();
    }

    public void setNodeView(OSMNodeView nodeView) {
        this.nodeView = nodeView;
    }


    @Subscribe
    public synchronized void processEvent(InternalEvent event) {
        LOG.debug(">> processEvent");
        try {
            switch (event.getEventType()) {
                case NODE_POSITION_CHANGED:
                    actualizePositionLabel();
                    break;
                case NODE_END_SENSE:
                    showObservationsForAbility();
                    break;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
        LOG.debug("<< processEvent");
    }

    @Subscribe
    public void processStatisticsEvent(StatisticsEvent statisticsEvent) {
        LOG.trace(">> processStatisticsEvent");
        try{
            switch (statisticsEvent.getEventType()) {
                case GUI_UPDATE_STATISTICS:
                    this.statistics = (SensorStatistics) statisticsEvent.getPayload();
                    showCommunication(this.statistics, CommType.Incoming);
                    showPrograms();
                    break;
            }
        } catch (Exception exc) {
            LOG.error(exc.getMessage() , exc);
        }
        LOG.trace("<< processStatisticsEvent");
    }

    private void showPrograms() {

        Platform.runLater(()-> {
            this.programStatistics.clear();
            for (SensorProgram program : this.statistics.getSensorPrograms()) {
                this.programStatistics.add(new ProgramStatistics(program));
            }
        });

    }

    private void showCommunication(SensorStatistics sensorStatistics, CommType commType) {
        if (sensorStatistics.getSensorId() == this.nodeView.getID()) {
            List<CommunicationStatistics> communicationStatistics = new ArrayList<>();
            switch (commType) {
                case Incoming:
                    for(CommunicationProcess process : sensorStatistics.getIncomingCommunication()) {
                        communicationStatistics.add(new CommunicationStatistics(process));
                    }
                    break;

                case Outgoing:
                    for(CommunicationProcess process : sensorStatistics.getOutgoingCommunication()) {
                        communicationStatistics.add(new CommunicationStatistics(process));
                    }
                    break;
            }

            Platform.runLater(()-> {
                this.commStatistics.clear();
                this.commStatistics.addAll(communicationStatistics);
            });

        }
    }

    public void showCommunicationByType() {
        String commTypeString = (String) this.commTypChooser.getValue();

        if(commTypeString != null && statistics != null) {
            showCommunication(statistics , CommType.valueOf(commTypeString));
        }

    }

    public void showObservationsForAbility() {

        String abilityName = (String) abilityChooser.getValue();

        if(abilityName == null) {
            return;
        }

        List<PhenomenonValue> observations = nodeView.getNode().getObservations().get(AbilityType.valueOf(abilityName));

        Platform.runLater(() -> {
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
            }
        });

    }

    private void actualizePositionLabel() {
        Platform.runLater(() -> {
            latitude.setText(Double.toString(nodeView.getLat()));
            longitude.setText(Double.toString(nodeView.getLon()));
        });
    }

}
