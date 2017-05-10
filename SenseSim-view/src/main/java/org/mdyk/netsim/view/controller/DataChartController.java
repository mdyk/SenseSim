package org.mdyk.netsim.view.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.sensor.SensorModel;
import org.mdyk.netsim.view.node.OSMNodeView;

import java.net.URL;
import java.util.*;

/**
 * Created by Michal on 2017-05-10.
 */
public class DataChartController implements Initializable {

    @FXML
    private NumberAxis xAxis;


    @FXML
    private LineChart<Number, Number> chart;

    private SensorModel abilityName;
    private OSMNodeView nodeView;
    private ObservableList<XYChart.Series<Number,Number>> lineChartData;
    XYChart.Series<Number , Number> series;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setData(SensorModel abilityName, OSMNodeView nodeView) {
        this.abilityName = abilityName;
        this.nodeView = nodeView;
        xAxis.setAutoRanging(false);

        lineChartData = FXCollections.observableArrayList();
        series = new XYChart.Series<>();
        series.setName(abilityName.unitName());
        lineChartData.add(series);
        chart.setAnimated(false);
        chart.setData(lineChartData);
        chart.setCreateSymbols(false);

        update();

    }

    public void update() {
        if(abilityName == null) {
            return;
        }

        Platform.runLater(() -> {

            TreeMap<Double, List<ConfigurationSpace>> sourceObservations = (TreeMap<Double, List<ConfigurationSpace>>) nodeView.getNode().getObservations(abilityName.getConfigurationSpaceClass() , 2000);




            xAxis.setLowerBound(sourceObservations.firstKey());
            xAxis.setUpperBound(sourceObservations.lastKey());

            Map<Double, List<ConfigurationSpace>> observations = new HashMap<>(sourceObservations);

            if(observations != null) {

                for (Double time : observations.keySet()) {
                    for (ConfigurationSpace configurationSpace : observations.get(time)) {

                        if (series.getData().size() == 0) {
                            series.getData().add(new XYChart.Data(time ,Double.parseDouble(configurationSpace.getStringValue())));
                        } else if (!(series.getData().get(series.getData().size()-1)).getXValue().equals(time)) {
                            if(series.getData().size() >= 2000) {
                                series.getData().remove(0, 2000);
                            }

                            series.getData().add(new XYChart.Data(time ,Double.parseDouble(configurationSpace.getStringValue())));
                        }

                    }
                }

            }
        });

    }

    public void start() {

        new Thread(){
            @Override
            public void run() {
                while (true) {
                    update();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

    }

}
