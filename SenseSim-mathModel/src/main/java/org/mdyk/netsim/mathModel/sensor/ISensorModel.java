package org.mdyk.netsim.mathModel.sensor;

import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
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

    public Map<Double, List<PhenomenonValue>> getObservations();

    public List<PhenomenonValue> getObservationsAtTime(Double time);

    public void addObservation(Double time , PhenomenonValue value);


    // TODO zasoby, programy, SNT

}



