package org.mdyk.netsim.logic.node;


import org.mdyk.netsim.logic.node.geo.SensorLogic;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;

public interface SimEntityFactory {

    public SensorSimEntity buildSensorSimEntity(SensorLogic sensorLogic);

}
