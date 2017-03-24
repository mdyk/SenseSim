package org.mdyk.netsim.mathModel.sensor;


import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.ObserverModel;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;

/**
 * Represents sensor which can be attached to device
 */
public abstract class SensorModel<O extends ObserverModel<?,?> , R extends ConfigurationSpace> {

    /**
     * Returns sensor name.
     * @return
     */
    public abstract String getName();

    /**
     * Returns observer associated with sensor.
     * @return
     */
    public abstract O getObserver();

    //TODO macierz obrotu

    /**
     * Describes sampling frequency of the sensor in milliseconds.
     * @return
     */
    public abstract double samplingFrequency();

    /**
     * Defines how much time takes one sampling.
     * @return
     */
    public abstract double sensingTime();

    /**
     * Calculates
     * @param phenomenonModel
     * @param distance
     * @return
     */
    public abstract R getObservation(PhenomenonModel phenomenonModel , double time , double distance);

    public abstract Class getConfigurationSpaceClass();


    final public String toString() {
        return getName();
    }

    public abstract String unitName();

}
