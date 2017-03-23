package org.mdyk.netsim.mathModel.observer.ecg;

import org.apache.log4j.Logger;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpaceFactory;


public class EcgConfigurationSpaceFactory implements ConfigurationSpaceFactory  {

    private static final Logger LOG = Logger.getLogger(EcgConfigurationSpaceFactory.class);

    @Override
    public ConfigurationSpace buildConfigurationSpace(String value) {
        LOG.trace(">> buildConfigurationSpace[value="+value+"]");
        double milivolts = Double.parseDouble(value);
        LOG.trace("<< buildConfigurationSpace");
        return new EcgConfigurationSpace(milivolts);
    }

}
