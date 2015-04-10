package org.mdyk.netsim.logic.node;


import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;

public interface SensorAPIFactory {

    public SensorAPI buildSensorAPI(SensorSimEntity sensorSimEntity);

}
