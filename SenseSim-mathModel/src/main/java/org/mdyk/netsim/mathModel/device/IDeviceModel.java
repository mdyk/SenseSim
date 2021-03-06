package org.mdyk.netsim.mathModel.device;

import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.device.connectivity.CommunicationInterface;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 Description of device based on mathematical mathModel
 */
public interface IDeviceModel<P extends Position> {

    int getID();

    String getName();

    P getPosition();

    void setPosition(P position);

    void setPositionX(double x);

    void setPositionY(double y);

    double getVelocity();

    void setVelocity(double velocity);

    @Deprecated
    double getRadioRange();

    @Deprecated
    void setRadioRange(double radioRange);

    @Deprecated
    List<AbilityType> getAbilities();

    List<SensorModel<?, ?>> getSensors();

    RoutingAlgorithm getRoutingAlgorithm();

    void setRoutingAlgorithm(RoutingAlgorithm routingAlgorithm);

    Map<Class<? extends ConfigurationSpace>, Map<Double, List<ConfigurationSpace>>> getObservations();

    Map<Double, List<ConfigurationSpace>> getObservations(Class<? extends ConfigurationSpace> configurationSpace, int samplesCount);

    @Deprecated
    Map<AbilityType, List<PhenomenonValue>> old_getObservations();

    @Deprecated
    List<PhenomenonValue> getObservationsAtTime(AbilityType ability, Double time);

    void addObservation(Class<? extends ConfigurationSpace> configurationSpaceClass, Double time, ConfigurationSpace value);

    /**
     * Returns bandwidth (measured in bits per second) of the device's wireless communication module.
     * Value is the same for in and out communication.
     * @return
     *      bandwith of the device's wireless module.
     */
    @Deprecated
    double getWirelessBandwith();

//    /**
//     * Method executed when device receives message from neighbour.
//     * @param time
//     *      time at which the message was received.
//     * @param message
//     *      message which was received by device.
//     */
//    @Deprecated
//    void receiveMessage(double time, Message message);

    /**
     * Method executed when device receives message from neighbour.
     * @param time
     *      time at which the message was received.
     * @param communicationInterfaceId
     *      id of communication interface via which the message is sent
     * @param message
     *      message which was received by device.
     */
    void receiveMessage(double time, int communicationInterfaceId, Message message);

    List<CommunicationInterface> getCommunicationInterfaces();

    CommunicationInterface getCommunicationInterface(int commIntId);

    double getInboundBandwithCapacity(int commIntId);

    double getOutboundBandwithCapacity(int commIntId);

    /**
     *
     * @param commIntId
     * @param bits
     * @return
     *      true if given amout can be reserved, false otherwise
     */
    boolean reserveInboundBandwith(int commIntId , double bits);

    void freeInboundBandwith(int commIntId , double bits);

    /**
     *
     * @param commIntId
     * @param bits
     * @return
     *      true if given amout can be reserved, false otherwise
     */
    boolean reserveOutboundBandwith(int commIntId , double bits);

    void freeOutboundBandwith(int commIntId , double bits);

    // TODO zasoby, programy, SNT

}



