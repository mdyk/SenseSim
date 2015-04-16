package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.core.BasicSimStateChange;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;

public class EndProgramExecution extends BasicSimStateChange<DisSimNodeEntity , Object> {

    private static final Logger LOG = Logger.getLogger(EndProgramExecution.class);

    private int PID;

    public EndProgramExecution(DisSimNodeEntity entity, int PID) throws SimControlException {
        super(entity);
        this.PID = PID;
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.debug(">> EndProgramExecution.transition");
        EventBusHolder.getEventBus().post(EventFactory.endProgramExecutionEvent(this.PID));
        LOG.debug("<< EndProgramExecution.transition");
    }

}
