package org.mdyk.netsim.logic.sensor;


import org.mdyk.netsim.mathModel.sensor.SensorModel;

public interface SensorFactory {

    /**
     * Builds sensor basing on the name of its class.
     * @param className
     *      sensor class name.
     * @return
     */
    SensorModel<?,?> buildSensor(String className);



    /**
     * Builds sensor basing on the name of its class.
     * @param className
     *      sensor class name.
     * @param sensorId
     *      id of a sensor to create
     * @return
     */
    SensorModel<?,?> buildSensor(String className, int sensorId);
}
