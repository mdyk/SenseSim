package org.mdyk.netsim.logic.sensor;

import org.mdyk.netsim.mathModel.observer.temperature.PT100Observer;
import org.mdyk.netsim.mathModel.observer.temperature.TemperatureConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.sensor.SensorModel;


public class PT100Sensor implements SensorModel<PT100Observer , TemperatureConfigurationSpace> {


    private PT100Observer observer;
    private double frequency = 0.3; // milisekundy
    private double sensingTime = 0.05; // milisekundy


    public PT100Sensor() {
        this.observer = new PT100Observer();
    }


    @Override
    public PT100Observer getObserver() {
        return observer;
    }

    @Override
    public double samplingFrequency() {
        return this.frequency;
    }

    @Override
    public double sensingTime() {
        return sensingTime;
    }

    @Override
    public TemperatureConfigurationSpace getObservation(PhenomenonModel phenomenonModel, double time, double distance) {

        TemperatureConfigurationSpace event = (TemperatureConfigurationSpace) phenomenonModel.getEventValue(TemperatureConfigurationSpace.class , time);

        return observer.getConclusion(observer.getPremises(event));
    }

}
