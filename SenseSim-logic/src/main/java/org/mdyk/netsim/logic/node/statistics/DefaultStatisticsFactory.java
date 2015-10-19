package org.mdyk.netsim.logic.node.statistics;

import com.google.inject.Singleton;
import org.mdyk.netsim.logic.node.SensorStatisticsFactory;


@Singleton
public class DefaultStatisticsFactory implements SensorStatisticsFactory {

    @Override
    public SensorStatistics buildSensorStatistics() {
        return new DefaultStatistics();
    }
}
