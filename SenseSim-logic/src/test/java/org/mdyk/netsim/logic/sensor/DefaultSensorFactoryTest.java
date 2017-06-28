package org.mdyk.netsim.logic.sensor;

import junit.framework.TestCase;
import org.junit.Test;
import org.mdyk.netsim.logic.sensor.temp.PT100Sensor;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

public class DefaultSensorFactoryTest {

    @Test
    public void testBuildSensor() throws Exception {
        DefaultSensorFactory sensorFactory = new DefaultSensorFactory();

        SensorModel sensorModel = sensorFactory.buildSensor(PT100Sensor.class.getName());


        TestCase.assertNotNull(sensorModel);
        TestCase.assertTrue(sensorModel instanceof PT100Sensor);

        boolean error = false;
        try {
            sensorFactory.buildSensor("AAA");
        } catch (Exception exc) {
            error = true;
        }

        TestCase.assertTrue(error);

    }
}