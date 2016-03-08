package org.mdyk.netsim.logic.node;


import org.mdyk.netsim.logic.node.statistics.DeviceStatistics;

public interface DeviceStatisticsFactory {

    public DeviceStatistics buildSensorStatistics();

}
