package org.mdyk.netsim.logic.node;

import org.mdyk.netsim.logic.node.geo.ProgrammableNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;

import java.util.List;

/**
 * Factory which provides instances of sensor nodes
 */
public interface SensorNodeFactory {

   public ProgrammableNode createGeoSensorNode(int id, GeoPosition position, int radioRange, double velocity, List<AbilityType> abilities);

}
