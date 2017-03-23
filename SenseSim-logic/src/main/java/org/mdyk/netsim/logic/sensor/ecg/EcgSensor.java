package org.mdyk.netsim.logic.sensor.ecg;

import org.mdyk.netsim.mathModel.observer.ecg.EcgConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.ecg.EcgObserver;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

/**
 * Created by michal on 22.03.2017.
 */
public class EcgSensor extends SensorModel<EcgObserver, EcgConfigurationSpace> {

    private EcgObserver observer;

    public EcgSensor() {
        this.observer = new EcgObserver();
    }

    @Override
    public String getName() {
        return "EcgSensor";
    }

    @Override
    public EcgObserver getObserver() {
        return observer;
    }

    @Override
    public double samplingFrequency() {
        return 0.01;
    }

    @Override
    public double sensingTime() {
        return 0.001;
    }

    @Override
    public EcgConfigurationSpace getObservation(PhenomenonModel phenomenonModel, double time, double distance) {

        //FIXME poprawne uwzględnienie odległości
        if(distance > 0 ) {
            return null;
        }

        EcgConfigurationSpace event = (EcgConfigurationSpace) phenomenonModel.getEventValue(EcgConfigurationSpace.class , time);
        if(event != null) {
            return observer.getConclusion(observer.getPremises(event));
        }
        else {
            return null;
        }
    }

    @Override
    public Class getConfigurationSpaceClass() {
        return EcgConfigurationSpace.class;
    }
}
