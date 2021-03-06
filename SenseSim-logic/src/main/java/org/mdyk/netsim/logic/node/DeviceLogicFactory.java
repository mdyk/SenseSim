package org.mdyk.netsim.logic.node;


import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.device.connectivity.CommunicationInterface;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

import java.util.List;

public interface DeviceLogicFactory {

    @Deprecated
    DeviceLogic buildSensorLogic(int id, String name, GeoPosition position, int radioRange, int bandwidth, double velocity, List<AbilityType> abilities, List<SensorModel<?,?>> sensors);

    DeviceLogic buildSensorLogic(int id, String name, GeoPosition position, int radioRange, int bandwidth, double velocity,
                                 List<AbilityType> abilities, List<SensorModel<?,?>> sensors, List<CommunicationInterface> communicationInterfaces);

}
