package org.mdyk.netsim.mathModel.observer;

/**
 * Represents an observer
 */
public interface Observer<X extends ConfigurationSpace, Y extends PresmisesSpace> {

    X getConfigurationSpace();

    Y getPremises(X event, Object ... parameters);

    X getConclusion(Y premises);

}
