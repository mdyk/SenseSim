package org.mdyk.netsim.logic.node.geo;

import org.mdyk.netsim.logic.node.RoutedNode;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.util.GeoPosition;


public interface ProgrammableNode extends GeoSensorNode, RoutedNode<GeoPosition> {

    public SensorAPI<GeoPosition> getAPI();

}
