package org.mdyk.netsim.logic.node;


import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;

public interface SimEntityFactory {

    public SensorSimEntity buildSensorSimEntity(DeviceLogic deviceLogic);

}
