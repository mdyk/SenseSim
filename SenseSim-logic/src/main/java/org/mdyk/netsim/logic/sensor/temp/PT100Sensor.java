package org.mdyk.netsim.logic.sensor.temp;

import org.mdyk.netsim.mathModel.observer.temperature.PT100Observer;
import org.mdyk.netsim.mathModel.observer.temperature.TemperatureConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.sensor.SensorModel;


public class PT100Sensor extends SensorModel<PT100Observer , TemperatureConfigurationSpace> {


    private PT100Observer observer;
    private double frequency = 0.3; // milisekundy
    private double sensingTime = 0.05; // milisekundy


    public PT100Sensor() {
        this.observer = new PT100Observer();
    }


    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return "TemperatureSensor";
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

        //FIXME poprawne uwzględnienie odległości
        if(distance > 0 ) {
            return null;
        }

        TemperatureConfigurationSpace event = (TemperatureConfigurationSpace) phenomenonModel.getEventValue(TemperatureConfigurationSpace.class , time);
        // Event might be null if there is no value for given phenomenon at a given time
        if(event != null) {
            return observer.getConclusion(observer.getPremises(event));
        }
        else {
            return null;
        }
    }

    @Override
    public Class getConfigurationSpaceClass() {
        return TemperatureConfigurationSpace.class;
    }

    @Override
    public String unitName() {
        return "Celsius degree (C)";
    }

}
