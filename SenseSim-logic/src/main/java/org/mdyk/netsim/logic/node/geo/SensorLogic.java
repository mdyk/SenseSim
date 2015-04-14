package org.mdyk.netsim.logic.node.geo;

import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;
import org.mdyk.netsim.logic.util.GeoPosition;

public interface SensorLogic extends GeoSensorNode, RoutedNode<GeoPosition> {

    public void setSimEntity(SensorSimEntity simEntity);

}
