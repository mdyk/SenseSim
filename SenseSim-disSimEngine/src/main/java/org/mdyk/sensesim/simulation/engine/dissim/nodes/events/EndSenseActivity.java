package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.core.BasicSimStateChange;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.node.geo.SensorLogic;


public class EndSenseActivity extends BasicSimStateChange<DisSimNodeEntity, Object> {

    private static final Logger LOG = Logger.getLogger(EndSenseActivity.class);

    private DisSimNodeEntity sensorEntity;
    private SensorLogic sensorNode;

    public EndSenseActivity(double delay, DisSimNodeEntity sensorEntity) throws SimControlException {
        super(sensorEntity, delay);
        this.sensorEntity = sensorEntity;
        this.sensorNode = this.sensorEntity.sensorLogic;
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.trace(">> EndSenseActivity time" + simTime());

        sensorEntity.startSenseActivity = new StartSenseActivity(sensorEntity);
        sensorNode.sense();
        EventBusHolder.getEventBus().post(EventFactory.endSenseEvent(sensorEntity.getSensorLogic()));
        LOG.trace("<< EndSenseActivity time");
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
