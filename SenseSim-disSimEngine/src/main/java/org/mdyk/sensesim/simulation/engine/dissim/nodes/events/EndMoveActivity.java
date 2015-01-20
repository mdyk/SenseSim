package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;
import org.apache.log4j.Logger;


public class EndMoveActivity extends BasicSimStateChange<DisSimNodeEntity, StartMoveActivity> {

    private static final Logger LOG = Logger.getLogger(EndMoveActivity.class);

    private DisSimNodeEntity disSimNodeEntity;

    public EndMoveActivity(double delay, DisSimNodeEntity disSimNodeEntity) throws SimControlException {
        super(disSimNodeEntity, delay);
        this.disSimNodeEntity = disSimNodeEntity;
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.debug(">> EndMoveActivity.transition");
        disSimNodeEntity.startMoveActivity = new StartMoveActivity(disSimNodeEntity);
        disSimNodeEntity.getRoutedNode().move();
        LOG.debug("<< EndMoveActivity.transition");
    }

    @Override
    protected void onTermination() throws SimControlException {
        /* EMPTY */
    }

    @Override
    protected void onInterruption() throws SimControlException {
        /* EMPTY */
    }
}
