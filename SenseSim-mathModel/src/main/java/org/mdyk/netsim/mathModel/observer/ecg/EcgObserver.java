package org.mdyk.netsim.mathModel.observer.ecg;

import org.mdyk.netsim.mathModel.observer.ObserverModel;

/**
 * Simplified implementation of an ECG observer
 */
public class EcgObserver implements ObserverModel<EcgConfigurationSpace , EcgPresmisesSpace> {

    private final String name = "ECG";

    @Override
    public Class<EcgConfigurationSpace> getConfigurationSpaceClass() {
        return EcgConfigurationSpace.class;
    }

    @Override
    public EcgPresmisesSpace getPremises(EcgConfigurationSpace event, Object... parameters) {
        return new EcgPresmisesSpace(event.getMilivolts());
    }

    @Override
    public EcgPresmisesSpace getPremises(EcgConfigurationSpace event, double distance, Object... parameters) {
        return getPremises(event,parameters);
    }

    @Override
    public EcgConfigurationSpace getConclusion(EcgPresmisesSpace premises) {
        return new EcgConfigurationSpace(premises.getMilivolts());
    }

    @Override
    public String getName() {
        return name;
    }
}
