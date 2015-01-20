package org.mdyk.sensesim.simulation.engine.dissim.nodes;


import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.SensorNodeFactory;
import org.mdyk.netsim.logic.node.geo.RoutedGeoSensorNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.sensesim.simulation.engine.dissim.DisSimEngine;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimRoutedNode;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;


@Singleton
public class DisSimSensorNodeFactory implements SensorNodeFactory {

    @Inject
    private Environment environment;

    @Inject
    private WirelessChannel wirelessChannel;

    @Inject
    private DisSimEngine disSimEngine;

    @Override
    public RoutedGeoSensorNode createGeoSensorNode(int id, GeoPosition position, int radioRange, double velocity, List<AbilityType> abilities) {
        return new DisSimRoutedNode( id, position, radioRange, velocity, abilities, environment, wirelessChannel);
    }
}