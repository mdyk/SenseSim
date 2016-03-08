package org.mdyk.netsim.logic.node.statistics;

import com.google.inject.Singleton;
import org.mdyk.netsim.logic.node.DeviceStatisticsFactory;


@Singleton
public class DefaultStatisticsFactory implements DeviceStatisticsFactory {

    @Override
    public DeviceStatistics buildSensorStatistics() {
        return new DefaultStatistics();
    }
}
