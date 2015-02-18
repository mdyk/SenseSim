package org.mdyk.netsim.logic.node.geo;

import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.SensorNodeFactory;
import org.mdyk.netsim.logic.simEngine.thread.GeoSensorNodeThread;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class SensorNodeFactoryImpl implements SensorNodeFactory {


    @Inject
    private Environment environment;

    @Inject
    private WirelessChannel wirelessChannel;

    @Override
    public ProgrammableNode createGeoSensorNode(int id, GeoPosition position, int radioRange, double velocity, List<AbilityType> abilities) {
        return new GeoSensorNodeThread(id, position, radioRange, velocity, abilities, environment, wirelessChannel);
    }

}
