package org.mdyk.netsim.view.controller;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.process.CommunicationProcess;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.node.program.SensorProgram;
import org.mdyk.netsim.logic.node.statistics.DeviceStatistics;
import org.mdyk.netsim.logic.node.statistics.event.DeviceStatisticsEvent;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.SensorModel;
import org.mdyk.netsim.view.controlls.table.CommunicationStatistics;
import org.mdyk.netsim.view.controlls.table.ProgramStatistics;
import org.mdyk.netsim.view.node.OSMNodeView;
import org.reactfx.util.FxTimer;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.*;


public class SensorConsoleController implements Initializable {

    private static final Logger LOG = Logger.getLogger(SensorConsoleController.class);
    private OSMNodeView nodeView;
    private DeviceStatistics statistics;
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
    private TableColumn<ProgramStatistics , String> programLog;

    @FXML
    private TableColumn<ProgramStatistics , String> programStatus;

    private ObservableList<PhenomenonValue> observationsData = FXCollections.observableArrayList();
    private Map<String , ObservableList<PhenomenonValue>> observationsChartData;
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
        programLog.setCellValueFactory(new PropertyValueFactory<>("output"));
        programsTable.setItems(programStatistics);


    }
//    private ObservableList<XYChart.Data> chartData = FXCollections.observableArrayList();

    @SuppressWarnings("unchecked")
    public void fillGui() {
        consoleLabel.setText("Device " + nodeView.getID());
        nodeBandwidth.setText(Double.toString(nodeView.getNode().getWirelessBandwith()));
        velocity.setText(Double.toString(nodeView.getNode().getVelocity()));
        radioRange.setText(Double.toString(nodeView.getNode().getRadioRange()));

        ObservableList<SensorModel> abilities = FXCollections.observableArrayList();
        abilities.addAll(nodeView.getNode().getSensors());
        abilityChooser.setItems(abilities);

        ObservableList<String> commTypes = FXCollections.observableArrayList();
        commTypes.add(CommType.Incoming.name());
        commTypes.add(CommType.Outgoing.name());

        commTypChooser.setItems(commTypes);
        commTypChooser.setValue(CommType.Outgoing.name());

        actualizePositionLabel();
        showPrograms();
        showCommunicationByType();
        showObservationsForAbility();

        observationsChartData = new HashMap<>();

        for(SensorModel sm : nodeView.getNode().getSensors()) {
            observationsChartData.put(sm.getName() , FXCollections.observableArrayList());
        }

        FxTimer.runPeriodically(
                Duration.ofMillis(1000),
                this::showObservationsForAbility);

    }

    public void setNodeView(OSMNodeView nodeView) {
        this.nodeView = nodeView;
    }

    @Subscribe
    public synchronized void processEvent(InternalEvent event) {
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
    }

    @Subscribe
    public void processStatisticsEvent(DeviceStatisticsEvent deviceStatisticsEvent) {
        try{
            switch (deviceStatisticsEvent.getEventType()) {
                case GUI_UPDATE_STATISTICS:
                    DeviceStatistics statistics = (DeviceStatistics) deviceStatisticsEvent.getPayload();
                    if(statistics.getSensorId() == this.nodeView.getID()) {
                        this.statistics = statistics;
                        showCommunication(this.statistics, CommType.Incoming);
                        showPrograms();
                    }
                    break;
            }
        } catch (Exception exc) {
            LOG.error(exc.getMessage() , exc);
        }
    }

    private void showPrograms() {
        if(statistics != null) {
            Platform.runLater(() -> {
                this.programStatistics.clear();
                for (SensorProgram program : this.statistics.getSensorPrograms()) {
                    this.programStatistics.add(new ProgramStatistics(program));
                }
            });
        }
    }

    private void showCommunication(DeviceStatistics deviceStatistics, CommType commType) {
        if (deviceStatistics.getSensorId() == this.nodeView.getID()) {
            List<CommunicationStatistics> communicationStatistics = new ArrayList<>();
            switch (commType) {
                case Incoming:
                    for(CommunicationProcess process : deviceStatistics.getIncomingCommunication()) {
                        communicationStatistics.add(new CommunicationStatistics(process));
                    }
                    break;

                case Outgoing:
                    for(CommunicationProcess process : deviceStatistics.getOutgoingCommunication()) {
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
        SensorModel abilityName = (SensorModel) abilityChooser.getValue();

        if(abilityName == null) {
            return;
        }



//        Platform.runLater(() -> {
            Map<Double, List<ConfigurationSpace>> observations = nodeView.getNode().getObservations(abilityName.getConfigurationSpaceClass() , 50);
            if(observations != null) {

                List<PhenomenonValue> observationsList = new ArrayList<>();

//                Map<Double, List<ConfigurationSpace>> observationsCopy = new HashMap<>(observations);
                Iterator<Double> it = observations.keySet().iterator();

                while (it.hasNext()) {
                    Double time = it.next();
                    for(ConfigurationSpace configurationSpace : observations.get(time)) {
                        observationsList.add(new PhenomenonValue(time , configurationSpace));
//                        chartData.add(new XYChart.Data(time,Double.parseDouble(configurationSpace.getStringValue())));
                    }
                }

                observationsList.sort(new PhenomenonValue.DescTimeComparator());
                List<PhenomenonValue> observationsSublist;
                if(observationsList.size() > 50) {
                    observationsSublist = observationsList.subList(0, 49);
                } else {
                    observationsSublist = observationsList;
                }
                Platform.runLater(() -> {
                    observationsData.clear();
                    observationsData.addAll(observationsSublist);
                });
            }
//        });
    }

    private void actualizePositionLabel() {
        Platform.runLater(() -> {
            latitude.setText(Double.toString(nodeView.getLat()));
            longitude.setText(Double.toString(nodeView.getLon()));
        });
    }

    public void showPlot() {

        SensorModel abilityName = (SensorModel) abilityChooser.getValue();

        if(abilityName == null) {
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/DataChart.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.NONE);
            stage.initStyle(StageStyle.DECORATED);
            stage.setScene(new Scene(parent));
            DataChartController controller = fxmlLoader.getController();
            controller.setData(abilityName, nodeView);
            controller.start();
            stage.show();

            stage.setTitle(nodeView.getNode().getName() + " " + abilityName.getName());

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    controller.stop();
                }
            });


        } catch (IOException e) {
            LOG.error(e.getMessage() , e);
        }

    }

    private enum CommType {Incoming, Outgoing}


}
