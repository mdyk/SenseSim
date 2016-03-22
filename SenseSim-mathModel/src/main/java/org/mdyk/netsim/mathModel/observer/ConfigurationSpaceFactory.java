package org.mdyk.netsim.mathModel.observer;


public interface ConfigurationSpaceFactory {

    /**
     * Builds configuration space basing on its string value
     * @param value
     *      value of the configuration space as string
     * @return
     *      instance of the configuration space
     */
    //TODO określić rzucany wyjątek
    ConfigurationSpace buildConfigurationSpace(String value);

}
