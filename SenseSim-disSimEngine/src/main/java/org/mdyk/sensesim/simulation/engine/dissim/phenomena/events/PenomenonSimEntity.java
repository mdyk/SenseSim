package org.mdyk.sensesim.simulation.engine.dissim.phenomena.events;

import dissim.simspace.BasicSimEntity;
import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimModel;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.event.IPhenomenonModel;
import org.mdyk.netsim.mathModel.event.SimplePhenomenon;


public class PenomenonSimEntity extends BasicSimEntity {

    private SimplePhenomenon simplePhenomenon;

    public PenomenonSimEntity(IPhenomenonModel<GeoPosition> simplePhenomenon) {
        super(SimModel.getInstance().getCommonSimContext());

        // TODO rzutowanie do poprwy, najlepiej używać tylko interfejsu
        this.simplePhenomenon = (SimplePhenomenon) simplePhenomenon;
    }

    @Override
    public void reflect(BasicSimStateChange<?, ?> basicSimStateChange) {
        // EMPTY
    }

    public SimplePhenomenon getSimplePhenomenon() {
        return simplePhenomenon;
    }
}
