package org.mdyk.sensesim.simulation.engine.dissim.communication.events;

import dissim.simspace.core.BasicSimStateChange;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;

public class StartCommunicationActivity extends BasicSimStateChange<CommunicationProcessSimEntity, Object> {

    private static final Logger LOG = Logger.getLogger(StartCommunicationActivity.class);

    public StartCommunicationActivity(CommunicationProcessSimEntity entity) throws SimControlException {
        super(entity, 0.0);
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.trace(">> StartCommunicationActivity.transition()");
        getSimEntity().endCommunicationActivity = new EndCommunicationActivity(getSimEntity() , 0.1);
        LOG.trace("<< StartCommunicationActivity.transition()");
    }

    @Override
    protected void onTermination() throws SimControlException {
        // Empty
    }

    @Override
    protected void onInterruption() throws SimControlException {
        // Empty
    }
}
