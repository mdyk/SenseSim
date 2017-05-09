package org.mdyk.netsim.mathModel.observer.so2;

import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpaceFactory;



public class SaturationConfigurationSpaceFactory implements ConfigurationSpaceFactory {

    @Override
    public ConfigurationSpace buildConfigurationSpace(String value) {

        double saturation = Double.parseDouble(value);

        return new SaturationConfigurationSpace(saturation);
    }
    
}
