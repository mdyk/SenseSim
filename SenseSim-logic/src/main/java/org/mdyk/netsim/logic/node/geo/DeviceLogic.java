package org.mdyk.netsim.logic.node.geo;

import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;
import org.mdyk.netsim.logic.node.statistics.SensorStatistics;
import org.mdyk.netsim.logic.util.GeoPosition;

public interface DeviceLogic extends GeoDeviceNode, RoutedNode<GeoPosition> {

    void setSimEntity(SensorSimEntity simEntity);

    void setSensorStatistics(SensorStatistics statistics);

}
