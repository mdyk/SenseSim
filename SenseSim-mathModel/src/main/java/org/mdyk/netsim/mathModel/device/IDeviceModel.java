package org.mdyk.netsim.mathModel.device;

import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
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

    double getRadioRange();

    void setRadioRange(double radioRange);

    @Deprecated
    List<AbilityType> getAbilities();

    List<SensorModel<?, ?>> getSensors();

    RoutingAlgorithm getRoutingAlgorithm();

    void setRoutingAlgorithm(RoutingAlgorithm routingAlgorithm);

    Map<Class<? extends ConfigurationSpace>, TreeMap<Double, List<ConfigurationSpace>>> getObservations();

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
    double getWirelessBandwith();

    /**
     * Method executed when device receives message from neighbour.
     * @param time
     *      time at which the message was received.
     * @param message
     *      message which was received by device.
     */
    void receiveMessage(double time, Message message);

    // TODO zasoby, programy, SNT

}



