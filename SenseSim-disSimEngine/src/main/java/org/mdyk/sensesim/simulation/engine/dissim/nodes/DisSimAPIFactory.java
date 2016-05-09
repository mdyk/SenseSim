package org.mdyk.sensesim.simulation.engine.dissim.nodes;

import org.mdyk.netsim.logic.node.APIFactory;
import org.mdyk.netsim.logic.node.api.DeviceAPI;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.api.DisSimDeviceAPI;

import javax.inject.Singleton;

@Singleton
public class DisSimAPIFactory implements APIFactory {

    @Override
    public DeviceAPI buildSensorAPI(DeviceSimEntity deviceSimEntity) {
        return new DisSimDeviceAPI(deviceSimEntity);
    }

}
