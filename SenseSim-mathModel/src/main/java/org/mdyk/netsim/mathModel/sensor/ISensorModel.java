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

    public int getID();

    public P getPosition();

    public void setPosition(P position);

    public void setPositionX(double x);

    public void setPositionY(double y);

    public double getVelocity();

    public void setVelocity(double velocity);

    public double getRadioRange();

    public void setRadioRange(double radioRange);

    public List<AbilityType> getAbilities();

    public RoutingAlgorithm getRoutingAlgorithm();

    public void setRoutingAlgorithm(RoutingAlgorithm routingAlgorithm);

    public Map<AbilityType, List<PhenomenonValue>> getObservations();

    public List<PhenomenonValue> getObservationsAtTime(AbilityType ability, Double time);

    public void addObservation(AbilityType ability, Double time , PhenomenonValue value);

    /**
     * Returns bandwidth (measured in bits per second) of the sensor's wireless communication module.
     * Value is the same for in and out communication.
     * @return
     *      bandwith of the sensor's wireless module.
     */
    public double getWirelessBandwith();

    /**
     * Method executed when sensor receives message from neighbour.
     * @param time
     *      time at which the message was received.
     * @param message
     *      message which was received by sensor.
     */
    public void receiveMessage(double time, Message message);

    // TODO zasoby, programy, SNT

}



