package org.mdyk.netsim.mathModel.observer.rfid;

import org.mdyk.netsim.mathModel.observer.ObserverModel;



public class RfidObserver implements ObserverModel<RfidConfigurationSpace , RfidPremisesSpace> {

    private String name = "RFID";

    @Override
    public Class<RfidConfigurationSpace> getConfigurationSpaceClass() {
        return RfidConfigurationSpace.class;
    }

    @Override
    public RfidPremisesSpace getPremises(RfidConfigurationSpace event, Object... parameters) {
        return new RfidPremisesSpace(event.getRfidTagData());
    }

    @Override
    public RfidPremisesSpace getPremises(RfidConfigurationSpace event, double distance, Object... parameters) {
        return new RfidPremisesSpace(event.getRfidTagData());
    }

    @Override
    public RfidConfigurationSpace getConclusion(RfidPremisesSpace premises) {
        return new RfidConfigurationSpace(premises.getRfidTagData());
    }

    @Override
    public String getName() {
        return this.name;
    }
}
