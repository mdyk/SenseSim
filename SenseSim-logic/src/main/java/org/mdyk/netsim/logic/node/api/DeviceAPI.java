package org.mdyk.netsim.logic.node.api;

import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Interface for device's API
 */
public interface DeviceAPI<P extends Position> {

    void setSimEntity(DeviceSimEntity deviceSimEntity);

    /**
     * Sets device's route.
     * @param route
     *      list of points which define route.
     */
    void api_setRoute(List<P> route);

    /**
     * Starts device's move according the given route.
     */
    void api_startMove();

    /**
     * Stops device's move.
     */
    void api_stopMove();

    /**
     * Sends message to device with given id. Method does not ensure delivery.
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
    @Deprecated
    void api_sendMessage(int messageId, int originSource, int originDest, Object content , Integer size );

    /**
     * Sends message to device with given id. Method does not ensure delivery.
     * @param messageId
     *      id of the message. Should be unique
     * @param originSource
     *      sender of the message (will not change during hop by hop communication)
     * @param originDest
     *      receiver of the message (will not change during hop by hop communication)
     * @param communicationInterfaceId
     *      id of the communication interface used for sending a message.
     * @param content
     *      content of the message.
     * @param size
     *      size of the message. If null, the size will be calculated based on content (if that's possible).
     */
    void api_sendMessage(int messageId, int originSource, int originDest, int communicationInterfaceId, Object content , Integer size);

    @Deprecated
    /**
     * Returns list of device's observations.
     * @return
     *      list of observations.
     */
    Map<AbilityType, List<PhenomenonValue>> api_getObservations();

    /**
     * Sets routing algorithm for device
     * @param routingAlgorithm
     *      implementation of routing algorithm
     */
    void api_setRoutingAlgorithm(RoutingAlgorithm<?> routingAlgorithm);

    /**
     * Orders a device to scan for its neighbours.
     * @return
     *      list of found neighbours.
     */
    // public List<DeviceNode<P>> api_scanForNeighbors();
    @Deprecated
    List<Integer> api_scanForNeighbors();

    /**
     * Orders a device to scan for its neighbours.
     * @param communicationInterfaceId
     *      id of communication interface used for scanning
     * @return
     *      list of found neighbours.
     */
    List<Integer> api_scanForNeighbors(int communicationInterfaceId);

    /**
     * Returns node's current position.
     * @return
     *      node's position.
     */
    P api_getPosition();

    /**
     * Allows to add handler which should be fired when device receives a message.
     */
    void api_setOnMessageHandler(Function<Message , Object> handler);

    /**
     * Returns ID of the device
     * @return
     *      ID of the device.
     */
    Integer api_getMyID();


    PhenomenonValue api_getCurrentObservation(AbilityType abilityType);

    List<SensorModel<?,?>> api_getSensorsList();

    ConfigurationSpace api_getSensorCurrentObservation(SensorModel sensor);

}
