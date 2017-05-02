package org.mdyk.netsim.mathModel.observer.rfid;

import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpaceFactory;


public class RfidConfigurationSpaceFactory implements ConfigurationSpaceFactory {

    @Override
    public ConfigurationSpace buildConfigurationSpace(String value) {
        return new RfidConfigurationSpace(value);
    }
}
