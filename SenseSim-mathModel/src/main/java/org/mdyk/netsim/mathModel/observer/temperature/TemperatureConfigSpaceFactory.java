package org.mdyk.netsim.mathModel.observer.temperature;

import org.apache.log4j.Logger;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpaceFactory;


public class TemperatureConfigSpaceFactory implements ConfigurationSpaceFactory {

    private static final Logger LOG = Logger.getLogger(TemperatureConfigSpaceFactory.class);

    @Override
    public ConfigurationSpace buildConfigurationSpace(String value) {
        LOG.trace(">> buildConfigurationSpace[value="+value+"]");
        double tempValue = Double.parseDouble(value);
        LOG.trace("<< buildConfigurationSpace");
        return new TemperatureConfigurationSpace(tempValue);
    }

}
