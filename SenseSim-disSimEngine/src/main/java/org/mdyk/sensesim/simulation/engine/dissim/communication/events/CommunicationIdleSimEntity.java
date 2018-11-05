package org.mdyk.sensesim.simulation.engine.dissim.communication.events;

import dissim.simspace.core.BasicSimEntity;
import dissim.simspace.core.SimControlException;
import dissim.simspace.core.SimModel;

public class CommunicationIdleSimEntity extends BasicSimEntity {

    
    enum IdleStatus {IDLE, FINISHED_IDLE}

    private IdleStatus status;

    CommunicationIdleActivity activity;

    public CommunicationIdleSimEntity(double time) {
        super(SimModel.getInstance().getCommonSimContext());
        status = IdleStatus.IDLE;

        try {
            activity = new CommunicationIdleActivity(this , time);
        } catch (SimControlException e) {
            // FIXME
            throw new RuntimeException(e);
        }
    }


    void setIdleFinish() {
        this.status = IdleStatus.FINISHED_IDLE;
    }

    public boolean isInIDLE() {

        boolean idle = true;

        switch (status){

            case FINISHED_IDLE:
                idle = false;
        }

        return idle;
    }

}
