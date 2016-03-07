package org.mdyk.sensesim.simulation.engine.dissim.phenomena.events;

import dissim.simspace.core.BasicSimEntity;
import dissim.simspace.core.SimModel;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.SimplePhenomenon;


public class PhenomenonSimEntity extends BasicSimEntity {

    protected SimplePhenomenon simplePhenomenon;

    public PhenomenonSimEntity(PhenomenonModel<GeoPosition> simplePhenomenon) {
        super(SimModel.getInstance().getCommonSimContext());

        // TODO rzutowanie do poprwy, najlepiej używać tylko interfejsu
        this.simplePhenomenon = (SimplePhenomenon) simplePhenomenon;
    }

    public SimplePhenomenon getSimplePhenomenon() {
        return simplePhenomenon;
    }

}
