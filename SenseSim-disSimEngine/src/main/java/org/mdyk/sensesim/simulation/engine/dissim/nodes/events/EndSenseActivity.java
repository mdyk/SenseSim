package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;

public class EndSenseActivity extends BasicSimStateChange<DisSimRoutedSensorNodeEntity, Object> {

    private static final Logger LOG = Logger.getLogger(EndSenseActivity.class);

    private DisSimRoutedSensorNodeEntity sensorEntity;
    private DisSimRoutedSensorNode sensorNode;

    public EndSenseActivity(double delay, DisSimRoutedSensorNodeEntity sensorEntity) throws SimControlException {
        super(sensorEntity, delay);
        this.sensorEntity = sensorEntity;
        this.sensorNode = this.sensorEntity.wrapper;
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.trace(">> EndSenseActivity time" + simTime());

        sensorEntity.startSenseActivity = new StartSenseActivity(sensorEntity);

        EventBusHolder.getEventBus().post(EventFactory.endSenseEvent(sensorNode));

        LOG.trace("<< EndSenseActivity time");
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
