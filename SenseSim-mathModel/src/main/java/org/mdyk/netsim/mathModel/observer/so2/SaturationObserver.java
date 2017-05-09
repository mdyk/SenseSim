package org.mdyk.netsim.mathModel.observer.so2;

import org.mdyk.netsim.mathModel.observer.ObserverModel;


public class SaturationObserver implements ObserverModel<SaturationConfigurationSpace , SaturationPremisesSpace> {

    private String name = "Pulse oximeter";

    @Override
    public Class<SaturationConfigurationSpace> getConfigurationSpaceClass() {
        return SaturationConfigurationSpace.class;
    }

    @Override
    public SaturationPremisesSpace getPremises(SaturationConfigurationSpace event, Object... parameters) {
        return new SaturationPremisesSpace(event.getSaturation());
    }

    @Override
    public SaturationPremisesSpace getPremises(SaturationConfigurationSpace event, double distance, Object... parameters) {
        return getPremises(event, parameters);
    }

    @Override
    public SaturationConfigurationSpace getConclusion(SaturationPremisesSpace premises) {
        return new SaturationConfigurationSpace(premises.getSaturation());
    }

    @Override
    public String getName() {
        return this.name;
    }
}
