package org.mdyk.netsim.mathModel.sensor;


import org.mdyk.netsim.mathModel.observer.ObserverModel;

/**
 * Represents sensor which can be attached to device
 */
public interface SensorModel<O extends ObserverModel<?,?>> {

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

}
