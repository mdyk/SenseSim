package org.mdyk.sensesim.simulation.engine.dissim.nodes;

import org.mdyk.netsim.logic.node.APIFactory;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.api.DisSimSensorAPI;

import javax.inject.Singleton;

@Singleton
public class DisSimAPIFactory implements APIFactory {

    @Override
    public SensorAPI buildSensorAPI(DeviceSimEntity deviceSimEntity) {
        return new DisSimSensorAPI(deviceSimEntity);
    }

}
