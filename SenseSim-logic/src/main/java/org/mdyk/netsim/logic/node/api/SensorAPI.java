package org.mdyk.netsim.logic.node.api;

import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.SensorNode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Interface for sensor's API
 */
public interface SensorAPI<P extends Position> {

    public void setSimEntity(SensorSimEntity sensorSimEntity);

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
     * @param messageId
     *      id of the message. Should be unique
     * @param originSource
     *      sender of the message (will not change during hop by hop communication)
     * @param originDest
     *      receiver of the message (will not change during hop by hop communication)
     * @param content
     *      content of the message.
     * @param size
     *      size of the message. If null, the size will be calculated based on content (if that's possible).
     */
    public void api_sendMessage(int messageId, int originSource, int originDest, Object content , Integer size );

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
    // public List<SensorNode<P>> api_scanForNeighbors();
    public List<Integer> api_scanForNeighbors();

    /**
     * Returns node's current position.
     * @return
     *      node's position.
     */
    public P api_getPosition();

    /**
     * Allows to add handler which should be fired when sensor receives a message.
     */
    public void api_setOnMessageHandler(Function<Message , Object> handler);

    /**
     * Returns ID of the sensor
     * @return
     *      ID of the sensor.
     */
    public Integer api_getMyID();
}
