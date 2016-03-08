package org.mdyk.netsim.logic.node;


import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;

public interface APIFactory {

    public SensorAPI buildSensorAPI(DeviceSimEntity deviceSimEntity);

}
