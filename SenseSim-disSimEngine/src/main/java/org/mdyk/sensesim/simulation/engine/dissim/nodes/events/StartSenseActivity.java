package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.core.BasicSimStateChange;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.node.geo.DeviceLogic;

@Deprecated
public class StartSenseActivity extends BasicSimStateChange<DisSimNodeEntity, Object> {

    private DisSimNodeEntity disSimNodeEntity;

    private static final Logger LOG = Logger.getLogger(StartSenseActivity.class);

    private Environment environment;
    private DeviceLogic sensorNode;

    public StartSenseActivity(DisSimNodeEntity disSimNodeEntity) throws SimControlException {
        super(disSimNodeEntity);
        this.disSimNodeEntity = disSimNodeEntity;
        this.environment = disSimNodeEntity.environment;
        this.sensorNode = disSimNodeEntity.deviceLogic;
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.trace(">> StartSenseActivity time=" + simTime());
        EventBusHolder.getEventBus().post(EventFactory.startSenseEvent(disSimNodeEntity.getDeviceLogic()));
//        disSimNodeEntity.endSenseActivity = new EndSenseActivity(0.5, disSimNodeEntity);

        LOG.trace("<< StartSenseActivity");
    }

}
