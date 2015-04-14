package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.core.SimControlException;
import dissim.simspace.process.BasicSimAction;
import org.apache.log4j.Logger;

public class IdleActivity extends BasicSimAction<DisSimNodeEntity, Object> {

    private static final Logger LOG = Logger.getLogger(IdleActivity.class);

    public IdleActivity(DisSimNodeEntity entity, double duration) throws SimControlException {
        super(entity, duration, null);
    }

    @Override
    protected void transitionOnStart() throws SimControlException {
        LOG.trace(">< transitionOnStart");
    }

    @Override
    protected void transitionOnFinish() throws SimControlException {
        LOG.trace(">> transitionOnFinish");
        getSimEntity().middleware.getPrograms();
        LOG.trace("<< transitionOnFinish");
    }
}
