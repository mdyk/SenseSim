package org.mdyk.netsim.mathModel.sensor;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.mathModel.observer.ObserverModel;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class DefaultSensorModel<P extends Position> implements ISensorModel<P> {

    private static final Logger LOG = Logger.getLogger(DefaultSensorModel.class);

    protected int           id;
    protected P             position;
    protected double        radioRange;
    protected double        velocity;
    protected double        bandwith = 5000; // bity
    // TODO ujednolicenie do jednej listy
    protected Map<AbilityType, Map<Double, List<PhenomenonValue>>> observations;
    protected List<AbilityType> abilities;
    protected List<ObserverModel> observers;
    protected Map<Double, List<Message>> messagesMap;
    protected RoutingAlgorithm routingAlgorithm;

    protected DefaultSensorModel(int id, P position, int radioRange ,int bandwidth , double velocity, ObserverModel ... observers) {
        this.id = id;
        this.position = position;
        this.radioRange = radioRange;
        this.bandwith = bandwidth;
        this.velocity = velocity;
        this.observations = new HashMap<>();
        this.messagesMap = new HashMap<>();
        this.observers = new ArrayList<>();
    }

    @Deprecated
    protected DefaultSensorModel(int id, P position, int radioRange ,int bandwidth , double velocity, List<AbilityType> abilities) {
        this.id = id;
        this.position = position;
        this.radioRange = radioRange;
        this.bandwith = bandwidth;
        this.velocity = velocity;
        this.abilities = abilities;
        this.observations = new HashMap<>();
        this.messagesMap = new HashMap<>();
    }

//    public abstract void sense();

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

            List<PhenomenonValue> valuesCopy = new ArrayList<>(values);

            observationsByAbilities.put(ability , valuesCopy);
        }
        return observationsByAbilities;
    }

    @Override
    public List<PhenomenonValue> getObservationsAtTime(AbilityType ability, Double time) {
        return observations.get(ability).get(time);
    }

    @Override
    public void addObservation(AbilityType ability, Double time, PhenomenonValue value) {
        LOG.debug("Adding observation [ability=" + ability + " time=" + time + " , value=" + value + "]");
        List<PhenomenonValue> observationsAtTime;

        if(value == null || value.getValueClass().equals(PhenomenonValue.NullPhenomenonValue.class)) {
            LOG.trace("[sensor "+ this.getID() +"] No value at time + " + time);
            return;
        }

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

    @Override
    public double getWirelessBandwith() {
        return this.bandwith;
    }

    @Override
    public void receiveMessage(double time, Message message) {
        LOG.debug(">> receiveMessage[time="+time+"]");
        if(!this.messagesMap.containsKey(time)) {
            this.messagesMap.put(time, new ArrayList<>());
        }
        this.messagesMap.get(time).add(message);
        onMessage(time, message);
        LOG.debug("<< receiveMessage");
    }

    protected abstract void onMessage(double time, Message message);

    @Override
    public RoutingAlgorithm getRoutingAlgorithm() {
        return routingAlgorithm;
    }

    @Override
    public void setRoutingAlgorithm(RoutingAlgorithm routingAlgorithm) {
        this.routingAlgorithm = routingAlgorithm;
    }
}