package org.mdyk.netsim.logic.node;


import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;

public interface SimEntityFactory {

    public DeviceSimEntity buildSensorSimEntity(DeviceLogic deviceLogic);

}
