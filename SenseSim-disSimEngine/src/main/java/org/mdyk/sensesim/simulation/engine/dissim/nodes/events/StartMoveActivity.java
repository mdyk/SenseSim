package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.core.BasicSimStateChange;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;

@Deprecated
public class StartMoveActivity extends BasicSimStateChange<DisSimNodeEntity, Object> {

    private static final Logger LOG = Logger.getLogger(StartMoveActivity.class);
    /**
     * Delay of EndMoveActivity occurrence in seconds.
     */
    public static final double END_MOVE_DELAY = 0.1;

    private DisSimNodeEntity disSimNodeEntity;

    public StartMoveActivity(DisSimNodeEntity disSimNodeEntity) throws SimControlException {
        super(disSimNodeEntity);
        this.disSimNodeEntity = disSimNodeEntity;
    }

    public StartMoveActivity(DisSimNodeEntity disSimNodeEntity, double delay) throws SimControlException {
        super(disSimNodeEntity,delay);
        this.disSimNodeEntity = disSimNodeEntity;
    }


    @Override
    protected void transition() throws SimControlException {
        LOG.debug(">> StartMoveActivity.transition");
        disSimNodeEntity.endMoveActivity = new EndMoveActivity(END_MOVE_DELAY, disSimNodeEntity);
        this.terminate();
        LOG.debug("<< StartMoveActivity.transition");
    }

    @Override
    protected void onTermination() throws SimControlException {
        // EMPTY
    }

    @Override
    protected void onInterruption() throws SimControlException {
        getSimEntity().getProgrammableNode().stopMoveing();
    }
}
