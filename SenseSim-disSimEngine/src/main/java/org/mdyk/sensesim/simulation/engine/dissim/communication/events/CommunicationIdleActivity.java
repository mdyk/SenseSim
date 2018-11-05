package org.mdyk.sensesim.simulation.engine.dissim.communication.events;

import dissim.simspace.core.SimControlException;
import dissim.simspace.process.BasicSimAction;

public class CommunicationIdleActivity extends BasicSimAction<CommunicationIdleSimEntity , Object> {



    public CommunicationIdleActivity(CommunicationIdleSimEntity entity, double duration) throws SimControlException {
        super(entity, duration);
    }

    @Override
    protected void transitionOnStart() throws SimControlException {
        
    }

    @Override
    protected void transitionOnFinish() throws SimControlException {
        getSimEntity().setIdleFinish();
        this.terminate();
        this.deactivateRepetition();
    }
}
