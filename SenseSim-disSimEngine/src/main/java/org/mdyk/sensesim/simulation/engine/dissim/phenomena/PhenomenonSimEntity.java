package org.mdyk.sensesim.simulation.engine.dissim.phenomena;

import dissim.broker.observer.Observable;
import dissim.broker.observer.Observer;
import dissim.simspace.core.BasicSimEntity;
import dissim.simspace.core.SimModel;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.phenomena.SimplePhenomenon;

import java.util.List;


public class PhenomenonSimEntity extends BasicSimEntity implements PhenomenonModel, Observer {

    protected SimplePhenomenon simplePhenomenon;

    public PhenomenonSimEntity(PhenomenonModel<GeoPosition> simplePhenomenon) {
        super(SimModel.getInstance().getCommonSimContext());

        // TODO rzutowanie do poprwy, najlepiej używać tylko interfejsu
        this.simplePhenomenon = (SimplePhenomenon) simplePhenomenon;
    }

    public SimplePhenomenon getSimplePhenomenon() {
        return simplePhenomenon;
    }

    @Override
    public void notification(Class eventClass, Observable observable) {

        switch (eventClass.getSimpleName()) {
            case "EndMoveActivity":

                break;
        }
    }

    @Override
    public String getName() {
        return simplePhenomenon.getName();
    }

    @Override
    public List getPhenomenonRegionPoints() {
        return simplePhenomenon.getPhenomenonRegionPoints();
    }

    @Override
    public PhenomenonValue getPhenomenonValue(AbilityType ability, double time) {
        return simplePhenomenon.getPhenomenonValue(ability ,time);
    }

    @Override
    public boolean hasAbility(AbilityType ability) {
        return simplePhenomenon.hasAbility(ability);
    }

    @Override
    public ConfigurationSpace getEventValue(Class configurationSpaceClass, double time) {
        return getEventValue(configurationSpaceClass , time);
    }

    @Override
    public boolean hasConfigurationSpace(Class configurationSpaceClass) {
        return simplePhenomenon.hasConfigurationSpace(configurationSpaceClass);
    }
}
