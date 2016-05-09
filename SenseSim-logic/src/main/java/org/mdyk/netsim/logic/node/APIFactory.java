package org.mdyk.netsim.logic.node;


import org.mdyk.netsim.logic.node.api.DeviceAPI;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;

public interface APIFactory {

    public DeviceAPI buildSensorAPI(DeviceSimEntity deviceSimEntity);

}
