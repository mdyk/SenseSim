package org.mdyk.netsim.logic.node.geo;

import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.logic.node.statistics.DeviceStatistics;
import org.mdyk.netsim.logic.util.GeoPosition;

public interface DeviceLogic extends GeoDeviceNode, RoutedNode<GeoPosition> {

    void setSimEntity(DeviceSimEntity simEntity);

    void setDeviceStatistics(DeviceStatistics statistics);

}
