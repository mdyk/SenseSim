package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.core.BasicSimStateChange;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.node.geo.SensorLogic;

public class StartSenseActivity extends BasicSimStateChange<DisSimNodeEntity, Object> {

    private DisSimNodeEntity disSimNodeEntity;

    private static final Logger LOG = Logger.getLogger(StartSenseActivity.class);

    private Environment environment;
    private SensorLogic sensorNode;

    public StartSenseActivity(DisSimNodeEntity disSimNodeEntity) throws SimControlException {
        super(disSimNodeEntity);
        this.disSimNodeEntity = disSimNodeEntity;
        this.environment = disSimNodeEntity.environment;
        this.sensorNode = disSimNodeEntity.sensorLogic;
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.trace(">> StartSenseActivity time=" + simTime());

        disSimNodeEntity.endSenseActivity = new EndSenseActivity(0.5, disSimNodeEntity);

        LOG.trace("<< StartSenseActivity");
    }

}
