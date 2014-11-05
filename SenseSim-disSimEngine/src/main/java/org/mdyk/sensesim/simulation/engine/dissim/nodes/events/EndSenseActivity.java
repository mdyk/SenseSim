package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;
import org.apache.log4j.Logger;

public class EndSenseActivity extends BasicSimStateChange<DisSimRoutedSensorNodeEntity, Object> {

    private static final Logger LOG = Logger.getLogger(EndSenseActivity.class);

    private DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity;

    public EndSenseActivity(double delay, DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity) throws SimControlException {
        super(disSimRoutedSensorNodeEntity, delay);
        this.disSimRoutedSensorNodeEntity = disSimRoutedSensorNodeEntity;
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.trace(">> EndSenseActivity time" + simTime());

        disSimRoutedSensorNodeEntity.startSenseActivity = new StartSenseActivity(disSimRoutedSensorNodeEntity);

        LOG.trace("<< EndSenseActivity time");
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
