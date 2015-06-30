package org.mdyk.netsim.logic.node;


import org.mdyk.netsim.logic.node.geo.SensorLogic;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;

import java.util.List;

public interface SensorLogicFactory {

    public SensorLogic buildSensorLogic(int id, GeoPosition position, int radioRange, int bandwidth, double velocity, List<AbilityType> abilities);

}
