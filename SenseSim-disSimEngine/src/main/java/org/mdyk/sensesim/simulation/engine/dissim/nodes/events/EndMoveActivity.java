package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.core.BasicSimStateChange;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.aop.statistics.SaveStatistics;


public class EndMoveActivity extends BasicSimStateChange<DisSimNodeEntity, StartMoveActivity>{

    private static final Logger LOG = Logger.getLogger(EndMoveActivity.class);

    private DisSimNodeEntity disSimNodeEntity;

    public EndMoveActivity(double delay, DisSimNodeEntity disSimNodeEntity) throws SimControlException {
        super(disSimNodeEntity, delay);
        this.disSimNodeEntity = disSimNodeEntity;
    }

    @Override
    @SaveStatistics
    protected void transition() throws SimControlException {
        LOG.debug(">> EndMoveActivity.transition");
        disSimNodeEntity.startMoveActivity = new StartMoveActivity(disSimNodeEntity);
        disSimNodeEntity.getProgrammableNode().move();
        disSimNodeEntity.notifyObservers(EndMoveActivity.class);
        LOG.debug("<< EndMoveActivity.transition");
    }

    @Override
    protected void onTermination() throws SimControlException {
        /* EMPTY */
    }

    @Override
    protected void onInterruption() throws SimControlException {
        getSimEntity().getProgrammableNode().stopMoveing();
    }
}
