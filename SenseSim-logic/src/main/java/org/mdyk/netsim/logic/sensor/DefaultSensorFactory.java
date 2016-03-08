package org.mdyk.netsim.logic.sensor;

import org.mdyk.netsim.mathModel.sensor.SensorModel;



public class DefaultSensorFactory implements SensorFactory {

    @Override
    public SensorModel<?, ?> buildSensor(String className) {
        try {
            return (SensorModel<?, ?>) Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate class " + className , e);
        }
    }

}
