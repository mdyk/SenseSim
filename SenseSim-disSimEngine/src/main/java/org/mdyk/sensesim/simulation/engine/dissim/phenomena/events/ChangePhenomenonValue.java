package org.mdyk.sensesim.simulation.engine.dissim.phenomena.events;

import dissim.simspace.core.BasicSimStateChange;
import dissim.simspace.core.SimControlException;
import org.mdyk.sensesim.simulation.engine.dissim.phenomena.PhenomenonSimEntity;


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

}
