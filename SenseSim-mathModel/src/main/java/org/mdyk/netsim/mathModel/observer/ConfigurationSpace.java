package org.mdyk.netsim.mathModel.observer;

/**
 * Represents configuration space of the observer
 */
public abstract class ConfigurationSpace {

    public abstract String getStringValue();

    @Override
    final public String toString() {
        return getStringValue();
    }

}
