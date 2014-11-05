package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.GeoPosition;

public class StartSenseActivity extends BasicSimStateChange<DisSimRoutedSensorNodeEntity, Object> {

    private DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity;

    private static final Logger LOG = Logger.getLogger(StartSenseActivity.class);

    public StartSenseActivity(DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity) throws SimControlException {
        super(disSimRoutedSensorNodeEntity);
        this.disSimRoutedSensorNodeEntity = disSimRoutedSensorNodeEntity;
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.trace(">> StartSenseActivity time=" + simTime());

        disSimRoutedSensorNodeEntity.endSenseActivity = new EndSenseActivity(0.1, disSimRoutedSensorNodeEntity);

        System.out.println(disSimRoutedSensorNodeEntity.wrapper.environment.getEventValue(disSimRoutedSensorNodeEntity.wrapper.getPosition(),
                        simTime(), disSimRoutedSensorNodeEntity.wrapper.getAbilities().get(0)));

        LOG.trace("<< StartSenseActivity");
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
