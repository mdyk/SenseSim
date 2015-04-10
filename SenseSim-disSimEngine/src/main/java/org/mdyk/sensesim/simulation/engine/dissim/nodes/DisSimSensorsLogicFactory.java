package org.mdyk.sensesim.simulation.engine.dissim.nodes;


import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.SensorLogicFactory;
import org.mdyk.netsim.logic.node.SimEntityFactory;
import org.mdyk.netsim.logic.node.geo.SensorLogic;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.sensesim.simulation.engine.dissim.DisSimEngine;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimSensorLogic;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;


@Singleton
public class DisSimSensorsLogicFactory implements SensorLogicFactory {

    @Inject
    private Environment environment;

    @Inject
    private WirelessChannel wirelessChannel;

    @Inject
    private DisSimEngine disSimEngine;

    @Inject
    private CommunicationProcessFactory communicationProcessFactory;

    @Override
    public SensorLogic buildSensorLogic(int id, GeoPosition position, int radioRange, double velocity, List<AbilityType> abilities) {
        return new DisSimSensorLogic(id, position, radioRange, velocity, abilities, environment, wirelessChannel, communicationProcessFactory);
    }
}
