package org.mdyk.netsim.logic.sensor.saturation;

import org.mdyk.netsim.mathModel.observer.so2.SaturationConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.so2.SaturationObserver;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.sensor.SensorModel;



public class SaturationSensor extends SensorModel<SaturationObserver , SaturationConfigurationSpace> {

    private SaturationObserver observer;
    private double frequency = 2.5;
    private double sensingTime = 1;

    public SaturationSensor() {
        this.observer = new SaturationObserver();
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return "Pulse oximeter";
    }

    @Override
    public SaturationObserver getObserver() {
        return this.observer;
    }

    @Override
    public double samplingFrequency() {
        return frequency;
    }

    @Override
    public double sensingTime() {
        return sensingTime;
    }

    @Override
    public SaturationConfigurationSpace getObservation(PhenomenonModel phenomenonModel, double time, double distance) {
        if(distance > 0 ) {
            return null;
        }

        SaturationConfigurationSpace event = (SaturationConfigurationSpace) phenomenonModel.getEventValue(SaturationConfigurationSpace.class , time);
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
        return SaturationConfigurationSpace.class;
    }

    @Override
    public String unitName() {
        return "SaO2";
    }
}
