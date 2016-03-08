package org.mdyk.sensesim.simulation.engine.dissim.nodes;


import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.DeviceLogicFactory;
import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.sensor.SensorModel;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimDeviceLogic;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;


@Singleton
public class DisSimSensorsLogicFactory implements DeviceLogicFactory {

    @Inject
    private Environment environment;

    @Inject
    private WirelessChannel wirelessChannel;

    @Inject
    private CommunicationProcessFactory communicationProcessFactory;

    @Override
    public DeviceLogic buildSensorLogic(int id, GeoPosition position, int radioRange, int bandwidth , double velocity, List<AbilityType> abilities) {
        return new DisSimDeviceLogic(id, position, radioRange, bandwidth, velocity, abilities, environment, wirelessChannel, communicationProcessFactory);
    }

    @Override
    public DeviceLogic buildSensorLogic(int id, GeoPosition position, int radioRange, int bandwidth, double velocity, List<AbilityType> abilities, List<SensorModel<?, ?>> sensors) {
        return new DisSimDeviceLogic(id, position, radioRange, bandwidth, velocity, abilities, sensors, environment, wirelessChannel, communicationProcessFactory);
    }
}
