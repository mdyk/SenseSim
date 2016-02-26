package org.mdyk.netsim.mathModel.sensor;

import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;

import java.util.List;
import java.util.Map;


/**
 Description of sensor based on mathematical mathModel
 */
public interface ISensorModel<P extends Position> {

    int getID();

    P getPosition();

    void setPosition(P position);

    void setPositionX(double x);

    void setPositionY(double y);

    double getVelocity();

    void setVelocity(double velocity);

    double getRadioRange();

    void setRadioRange(double radioRange);

    List<AbilityType> getAbilities();

    RoutingAlgorithm getRoutingAlgorithm();

    void setRoutingAlgorithm(RoutingAlgorithm routingAlgorithm);

    Map<AbilityType, List<PhenomenonValue>> getObservations();

    List<PhenomenonValue> getObservationsAtTime(AbilityType ability, Double time);

    void addObservation(AbilityType ability, Double time , PhenomenonValue value);

    void sense();

    /**
     * Returns bandwidth (measured in bits per second) of the sensor's wireless communication module.
     * Value is the same for in and out communication.
     * @return
     *      bandwith of the sensor's wireless module.
     */
    double getWirelessBandwith();

    /**
     * Method executed when sensor receives message from neighbour.
     * @param time
     *      time at which the message was received.
     * @param message
     *      message which was received by sensor.
     */
    void receiveMessage(double time, Message message);

    // TODO zasoby, programy, SNT

}



