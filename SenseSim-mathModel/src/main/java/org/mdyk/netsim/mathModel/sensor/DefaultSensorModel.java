package org.mdyk.netsim.mathModel.sensor;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class DefaultSensorModel<P extends Position> implements ISensorModel<P> {

    private static final Logger logger = Logger.getLogger(DefaultSensorModel.class);

    protected int           id;
    protected P             position;
    protected double        radioRange = 20;
    protected double        velocity = 10;
    protected Map<Double, List<Object>> observations;
    protected List<AbilityType> abilities;

    protected DefaultSensorModel(int id, P position, int radioRange, double velocity, List<AbilityType> abilities) {
        this.id = id;
        this.position = position;
        this.radioRange = radioRange;
        this.velocity = velocity;
        this.abilities = abilities;
        this.observations = new HashMap<>();
    }

    public abstract void sense();

    @Override
    public int getID() {
        return id;
    }

    @Override
    public double getRadioRange() {
        return radioRange;
    }

    @Override
    public void setRadioRange(double radioRange) {
        this.radioRange = radioRange;
    }

    @Override
    public void setPositionX(double x) {
        position.setPositionX(x);
    }

    @Override
    public void setPositionY(double y) {
        position.setPositionY(y);
    }

    @Override
    public double getVelocity() {
        return this.velocity;
    }

    @Override
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public void setPosition(P position) {
        this.position = position;
    }

    @Override
    public P getPosition() {
        return position;
    }

    @Override
    public List<AbilityType> getAbilities() {
        return abilities;
    }

    @Override
    public Map<Double, List<Object>> getObservations() {
        return observations;
    }

    @Override
    public List<Object> getObservationsAtTime(Double time) {
        return observations.get(time);
    }

    @Override
    public void addObservation(Double time, Object value) {
        logger.info("Adding observation [time="+time+" , value="+value+"]");
        List<Object> observationsAtTime;
        if(!observations.containsKey(time)) {
            observationsAtTime = new ArrayList<>();
            observationsAtTime.add(value);
            observations.put(time , observationsAtTime);
        }
        else {
            observations.get(time).add(value);
        }
    }


}