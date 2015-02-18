package org.mdyk.netsim.logic.node.api;

import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.SensorNode;

import java.util.List;
import java.util.Map;

/**
 * Interface for sensor's API
 */
public interface SensorAPI<P extends Position> {

    /**
     * Sets sensor's route.
     * @param route
     *      list of points which define route.
     */
    public void api_setRoute(List<P> route);

    /**
     * Starts sensor's move according the given route.
     */
    public void api_startMove();

    /**
     * Stops sensor's move.
     */
    public void api_stopMove();

    /**
     * Sends message to sensor with given id. Method does not ensure delivery.
     * @param id
     *      id of the destination sensor.
     * @param message
     *      message to be sent.
     */
    public void api_sendMessage(int id, Message<?> message);

    /**
     * Returns list of sensor's observations.
     * @return
     *      list of observations.
     */
    public Map<AbilityType, List<PhenomenonValue>> api_getObservations();


    /**
     * Sets routing algorithm for sensor
     * @param routingAlgorithm
     *      implementation of routing algorithm
     */
    public void api_setRoutingAlgorithm(RoutingAlgorithm<?> routingAlgorithm);

    /**
     * Orders a sensor to scan for its neighbours.
     * @return
     *      list of found neighbours.
     */
    public List<SensorNode<P>> api_scanForNeighbors();

    public P api_getPosition();

}
