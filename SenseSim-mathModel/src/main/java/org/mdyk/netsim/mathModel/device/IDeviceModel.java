package org.mdyk.netsim.mathModel.device;

import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

import java.util.List;
import java.util.Map;


/**
 Description of device based on mathematical mathModel
 */
public interface IDeviceModel<P extends Position> {

    int getID();

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

    Map<AbilityType, List<PhenomenonValue>> getObservations();

    List<PhenomenonValue> getObservationsAtTime(AbilityType ability, Double time);

    void addObservation(AbilityType ability, Double time , PhenomenonValue value);

    void sense();

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



