package org.mdyk.netsim.mathModel.sensor;


import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.ObserverModel;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;

/**
 * Represents sensor which can be attached to device
 */
public interface SensorModel<O extends ObserverModel<?,?> , R extends ConfigurationSpace> {

    /**
     * Returns observer associated with sensor.
     * @return
     */
    O getObserver();

    //TODO macierz obrotu

    /**
     * Describes sampling frequency of the sensor in milliseconds.
     * @return
     */
    double samplingFrequency();

    /**
     * Defines how much time takes one sampling.
     * @return
     */
    double sensingTime();

    /**
     * Calculates
     * @param phenomenonModel
     * @param distance
     * @return
     */
    R getObservation(PhenomenonModel phenomenonModel , double distance);

}
