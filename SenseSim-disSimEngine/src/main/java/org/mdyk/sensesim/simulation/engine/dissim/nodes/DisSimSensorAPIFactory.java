package org.mdyk.sensesim.simulation.engine.dissim.nodes;

import org.mdyk.netsim.logic.node.SensorAPIFactory;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.api.DisSimSensorAPI;

import javax.inject.Singleton;

@Singleton
public class DisSimSensorAPIFactory implements SensorAPIFactory {

    @Override
    public SensorAPI buildSensorAPI(SensorSimEntity sensorSimEntity) {
        return new DisSimSensorAPI(sensorSimEntity);
    }

}
