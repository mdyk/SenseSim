package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.core.BasicSimStateChange;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;


public class StartProgramExecution extends BasicSimStateChange<DisSimNodeEntity , Object> {

    private static final Logger LOG = Logger.getLogger(StartProgramExecution.class);

    private int PID;

    public StartProgramExecution(DisSimNodeEntity entity, int PID) throws SimControlException {
        super(entity);
        this.PID = PID;
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.debug(">> StartProgramExecution.transition");
        EventBusHolder.getEventBus().post(EventFactory.startProgramExecutionEvent(this.PID));
        LOG.debug("<< StartProgramExecution.transition");
    }
}
