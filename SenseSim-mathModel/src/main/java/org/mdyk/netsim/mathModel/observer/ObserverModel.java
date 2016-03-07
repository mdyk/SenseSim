package org.mdyk.netsim.mathModel.observer;

/**
 * Represents an observer
 */
public interface ObserverModel<X extends ConfigurationSpace, Y extends PresmisesSpace> {


    Class<X> getConfigurationSpaceClass();

    /**
     * Represents PI function from the observer model
     * @param event
     * @param parameters
     * @return
     */
    Y getPremises(X event, Object ... parameters);

    /**
     * Represents PI function from the observer model
     * @param event
     * @param parameters
     * @param distance
     *      describes distance between the observer and event
     * @return
     */
    Y getPremises(X event, double distance, Object ... parameters);

    /**
     * Represents conclusion kernel of the observer
     * @param premises
     * @return
     */
    X getConclusion(Y premises);

    String getName();

}
