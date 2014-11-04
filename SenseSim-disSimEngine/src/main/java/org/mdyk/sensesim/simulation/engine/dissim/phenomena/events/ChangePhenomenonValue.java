package org.mdyk.sensesim.simulation.engine.dissim.phenomena.events;

import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;


/**
 * Sim event which changes phenomenon's value during time.
 */
public class ChangePhenomenonValue extends BasicSimStateChange<PhenomenonSimEntity, Object> {

    private PhenomenonSimEntity phenomenonSimEntity;

    public ChangePhenomenonValue(double delay, PhenomenonSimEntity entity) throws SimControlException {
        super(entity, delay);
        this.phenomenonSimEntity = entity;

    }


    @Override
    protected void transition() throws SimControlException {

    }

    @Override
    protected void onTermination() throws SimControlException {
        // EMPTY
    }

    @Override
    protected void onInterruption() throws SimControlException {
        // EMPTY
    }
}
