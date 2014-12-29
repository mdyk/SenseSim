package org.mdyk.netsim.mathModel.sensor;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class DefaultSensorModel<P extends Position> implements ISensorModel<P> {

    private static final Logger LOG = Logger.getLogger(DefaultSensorModel.class);

    protected int           id;
    protected P             position;
    protected double        radioRange = 20;
    protected double        velocity = 10;
    // TODO ujednolicenie do jednej listy
    protected Map<AbilityType, Map<Double, List<PhenomenonValue>>> observations;
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
    public Map<AbilityType, List<PhenomenonValue>> getObservations() {

        HashMap<AbilityType, List<PhenomenonValue>> observationsByAbilities = new HashMap<>();

        for(AbilityType ability : observations.keySet()) {
            Map<Double, List<PhenomenonValue>> valuesMap = observations.get(ability);
            List<PhenomenonValue> values = new ArrayList<>();

            for(Double time : valuesMap.keySet()) {
                values.addAll(valuesMap.get(time));
            }
        }
        return observationsByAbilities;
    }

    @Override
    public List<PhenomenonValue> getObservationsAtTime(AbilityType ability, Double time) {
        return observations.get(ability).get(time);
    }

    @Override
    public void addObservation(AbilityType ability, Double time, PhenomenonValue value) {
        LOG.info("Adding observation [ability=" + ability + " time=" + time + " , value=" + value + "]");
        List<PhenomenonValue> observationsAtTime;

        if(!observations.containsKey(ability)) {
            observationsAtTime = new ArrayList<>();
            observationsAtTime.add(value);
            HashMap<Double , List<PhenomenonValue>> valueMap = new HashMap<>();
            valueMap.put(time , observationsAtTime);
            observations.put(ability, valueMap);
        }
        else if(!observations.get(ability).containsKey(time)) {
            observationsAtTime = new ArrayList<>();
            observationsAtTime.add(value);
            observations.get(ability).put(time , observationsAtTime);
        }
        else {
            observations.get(ability).get(time).add(value);
        }
    }


}