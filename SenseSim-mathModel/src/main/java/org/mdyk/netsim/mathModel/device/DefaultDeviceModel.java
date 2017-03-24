package org.mdyk.netsim.mathModel.device;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.mathModel.device.connectivity.CommunicationInterface;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

import java.util.*;


public abstract class DefaultDeviceModel<P extends Position> implements IDeviceModel<P> {

    private static final Logger LOG = Logger.getLogger(DefaultDeviceModel.class);

    protected int           id;
    protected String        name;
    protected P             position;
    protected double        velocity;

    @Deprecated
    protected double        radioRange;
    protected List<CommunicationInterface> communicationInterfaces;
    @Deprecated
    protected Map<AbilityType, Map<Double, List<PhenomenonValue>>> observations;
    /**
     * Holds device observations
     */
    protected Map<Class<? extends ConfigurationSpace>, TreeMap<Double, List<ConfigurationSpace>>> observationsFromObserver;
    @Deprecated
    protected List<AbilityType> abilities;
    protected Map<Double, List<Message>> messagesMap;
    protected RoutingAlgorithm routingAlgorithm;
    protected List<SensorModel<?,?>> sensors;
    @Deprecated
    private double          bandwith = 5000; // bity

    @Deprecated
    protected DefaultDeviceModel(int id, P position, int radioRange , int bandwidth , double velocity, List<AbilityType> abilities) {
        this.id = id;
        this.position = position;
        this.radioRange = radioRange;
        this.bandwith = bandwidth;
        this.velocity = velocity;
        this.abilities = abilities;
        this.observations = new HashMap<>();
        this.messagesMap = new HashMap<>();
        observationsFromObserver = new HashMap<>();
    }

    @Deprecated
    protected DefaultDeviceModel(int id, String name, P position, int radioRange , int bandwidth , double velocity, List<AbilityType> abilities , List<SensorModel<?,?>> sensors) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.radioRange = radioRange;
        this.bandwith = bandwidth;
        this.velocity = velocity;
        this.abilities = abilities;
        this.observations = new HashMap<>();
        this.messagesMap = new HashMap<>();
        this.sensors = sensors;
        observationsFromObserver = new HashMap<>();
    }

    protected DefaultDeviceModel(int id, String name, P position, int radioRange , int bandwidth ,
                                 double velocity, List<AbilityType> abilities , List<SensorModel<?,?>> sensors,
                                 List<CommunicationInterface> communicationInterfaces) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.radioRange = radioRange;
        this.bandwith = bandwidth;
        this.velocity = velocity;
        this.abilities = abilities;
        this.observations = new HashMap<>();
        this.messagesMap = new HashMap<>();
        this.sensors = sensors;
        this.observationsFromObserver = new HashMap<>();
        this.communicationInterfaces = communicationInterfaces;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getName() {
        return this.name;
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
    public P getPosition() {
        return position;
    }

    @Override
    public void setPosition(P position) {
        this.position = position;
    }

    @Override
    public List<AbilityType> getAbilities() {
        return abilities;
    }

    public List<SensorModel<?, ?>> getSensors() {
        return sensors;
    }



    @Override
    public Map<Class<? extends ConfigurationSpace>, TreeMap<Double, List<ConfigurationSpace>>> getObservations() {
        return observationsFromObserver;
    }

    @Override
    public Map<AbilityType, List<PhenomenonValue>> old_getObservations() {
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
    public void addObservation(Class<? extends ConfigurationSpace> configurationSpaceClass, Double time, ConfigurationSpace value) {
        LOG.debug("Adding observation [confClass=" + configurationSpaceClass.getName() + " time=" + time + " , value=" + value + "]");

        List<ConfigurationSpace> observationsAtTime;

        if(value == null ) {
            LOG.trace("[device "+ this.getID() +"] No value at time + " + time);
            return;
        }

        if(!observationsFromObserver.containsKey(configurationSpaceClass)) {
            observationsAtTime = new ArrayList<>();
            observationsAtTime.add(value);
            TreeMap<Double , List<ConfigurationSpace>> valueMap = new TreeMap<>();
            valueMap.put(time , observationsAtTime);
            observationsFromObserver.put(configurationSpaceClass, valueMap);
        }
        else if(!observationsFromObserver.get(configurationSpaceClass).containsKey(time)) {
            observationsAtTime = new ArrayList<>();
            observationsAtTime.add(value);
            observationsFromObserver.get(configurationSpaceClass).put(time , observationsAtTime);
        }
        else {
            observationsFromObserver.get(configurationSpaceClass).get(time).add(value);
        }

    }

    @Override
    public double getWirelessBandwith() {
        return this.bandwith;
    }

//    @Override
//    @Deprecated
//    public void receiveMessage(double time, Message message) {
//        LOG.debug(">> receiveMessage[time="+time+"]");
//        if(!this.messagesMap.containsKey(time)) {
//            this.messagesMap.put(time, new ArrayList<>());
//        }
//        this.messagesMap.get(time).add(message);
//        onMessage(time, message);
//        LOG.debug("<< receiveMessage");
//    }

    @Override
    public void receiveMessage(double time, int communicationInterfaceId, Message message) {
        LOG.debug(">> receiveMessage[time="+time+"]");
        if(!this.messagesMap.containsKey(time)) {
            this.messagesMap.put(time, new ArrayList<>());
        }
        this.messagesMap.get(time).add(message);
        onMessage(time, communicationInterfaceId, message);
        LOG.debug("<< receiveMessage");
    }

    @Deprecated
    protected abstract void onMessage(double time, Message message);

    protected abstract void onMessage(double time, int communicationInterfaceId, Message message);

    @Override
    public RoutingAlgorithm getRoutingAlgorithm() {
        return routingAlgorithm;
    }

    @Override
    public void setRoutingAlgorithm(RoutingAlgorithm routingAlgorithm) {
        this.routingAlgorithm = routingAlgorithm;
    }

    @Override
    public List<CommunicationInterface> getCommunicationInterfaces() {
        return this.communicationInterfaces;
    }

    @Override
    public CommunicationInterface getCommunicationInterface(int commIntId){

        CommunicationInterface communicationInterface = null;

        for(CommunicationInterface commInt : this.communicationInterfaces) {
            if(commInt.getId() == commIntId) {
                communicationInterface = commInt;
            }
        }

        return communicationInterface;
    }

}