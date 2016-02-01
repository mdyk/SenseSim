package org.mdyk.netsim.mathModel.observer;

/**
 * Represents an observer
 */
public interface ObserverModel<X extends ConfigurationSpace, Y extends PresmisesSpace> {

    // TODO prawdopodobnie do usunięci
    X getConfigurationSpace();

    /**
     * Represents PI function from the observer model
     * @param event
     * @param parameters
     * @return
     */
    Y getPremises(X event, Object ... parameters);


    /**
     * Represents conclusion kernel of the observer
     * @param premises
     * @return
     */
    X getConclusion(Y premises);

    String getName();

}
