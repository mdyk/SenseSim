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
    void api_sendMessage(long messageId, int originSource, int originDest, int communicationInterfaceId, Object content , Integer size);

    @Deprecated
    /**
     * Returns list of device's observations.
     * @return
     *      list of observations.
     */
    Map<AbilityType, List<PhenomenonValue>> api_getObservations();

    /**
     * Returns given number of samples
     * @param configurationSpace
     *      type of sensor response
     * @param samplesCount
     *      number of samples to return
     * @return
     */
    List<ConfigurationSpace> api_getObservations(Class<? extends ConfigurationSpace> configurationSpace, int samplesCount);

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

    // TODO powinno być synchroniczne
    /**
     * Orders a device to scan for its neighbours.
     * @param communicationInterfaceId
     *      id of communication interface used for scanning
     * @return
     *      list of found neighbours.
     */
    List<Integer> api_scanForNeighbors(int communicationInterfaceId);

    /**
     * Lists communication interfaces installed on device.
     * @return
     *      map in which keys are id's and values names of the comm interface
     */
    Map<Integer , String> api_listCommunicationInterfaces();

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

    /**
     * Returns name of the device
     * @return
     *      String object containing name of the device.
     */
    String api_getName();

    PhenomenonValue api_getCurrentObservation(AbilityType abilityType);

    List<SensorModel<?,?>> api_getSensorsList();

    ConfigurationSpace api_getSensorCurrentObservation(SensorModel sensor);

    /**
     * Device stays in IDLE mode, but may receive incoming messages
     * @param time
     *      defines how long the device should stay in IDLE
     * @return
     *      false when IDLE is finished
     *      true when should stay in IDLE
     *
     */
    void api_stayIdleFor(double time);

}
