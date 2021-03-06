package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import com.google.inject.assistedinject.Assisted;
import dissim.simspace.core.SimModel;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.movement.geo.GeoMovementAlgorithm;
import org.mdyk.netsim.logic.movement.geo.GeoRouteMovementAlgorithm;
import org.mdyk.netsim.logic.network.NetworkManager;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.logic.node.statistics.DeviceStatistics;
import org.mdyk.netsim.mathModel.device.DefaultDeviceModel;
import org.mdyk.netsim.mathModel.device.DeviceNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.device.connectivity.CommunicationInterface;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class DisSimDeviceLogic extends DefaultDeviceModel<GeoPosition> implements DeviceLogic {


    private static final Logger LOG = Logger.getLogger(DisSimDeviceLogic.class);
    // FIXME do zmiany
    public WirelessChannel wirelessChannel;
    public NetworkManager networkManager;
    // FIXME do zmiany
    public Function<Message, Object> onMessageHandler;
    protected List<GeoPosition> route;
    protected GeoMovementAlgorithm currentMovementAlg;
    protected Environment environment;
    protected CommunicationProcessFactory communicationProcessFactory;
    private DeviceSimEntity deviceSimEntity;
    private DeviceStatistics deviceStatistics;
    private boolean isMoveing;


    @Inject
    public DisSimDeviceLogic(@Assisted("id") int id, @Assisted("name") String name, @Assisted GeoPosition position,
                             @Assisted("radioRange") int radioRange, int bandwidth,
                             @Assisted double velocity, @Assisted List<AbilityType> abilities, List<SensorModel<?,?>> sensors, List<CommunicationInterface> communicationInterfaces,
                             Environment environment, WirelessChannel wirelessChannel, NetworkManager networkManager, CommunicationProcessFactory communicationProcessFactory) {
        super(id,name, position, radioRange, bandwidth, velocity, abilities, sensors, communicationInterfaces);

        this.currentMovementAlg = new GeoRouteMovementAlgorithm();
        this.environment = environment;
        this.wirelessChannel = wirelessChannel;
        this.communicationProcessFactory = communicationProcessFactory;
        this.networkManager = networkManager;
        this.isMoveing = true;
    }

    @Deprecated
    @Inject
    public DisSimDeviceLogic(@Assisted("id") int id, @Assisted("name") String name, @Assisted GeoPosition position,
                             @Assisted("radioRange") int radioRange, int bandwidth,
                             @Assisted double velocity, @Assisted List<AbilityType> abilities, List<SensorModel<?,?>> sensors,
                             Environment environment, WirelessChannel wirelessChannel, CommunicationProcessFactory communicationProcessFactory) {
        super(id,name, position, radioRange, bandwidth, velocity, abilities, sensors);

        this.currentMovementAlg = new GeoRouteMovementAlgorithm();
        this.environment = environment;
        this.wirelessChannel = wirelessChannel;
        this.communicationProcessFactory = communicationProcessFactory;
        this.isMoveing = true;
    }

    @Inject
    @Deprecated
    public DisSimDeviceLogic(@Assisted("id") int id, @Assisted GeoPosition position,
                             @Assisted("radioRange") int radioRange, int bandwidth,
                             @Assisted double velocity, @Assisted List<AbilityType> abilities,
                             Environment environment, WirelessChannel wirelessChannel, CommunicationProcessFactory communicationProcessFactory) {
        super(id, position, radioRange, bandwidth, velocity, abilities);

        this.currentMovementAlg = new GeoRouteMovementAlgorithm();
        this.environment = environment;
        this.wirelessChannel = wirelessChannel;
        this.communicationProcessFactory = communicationProcessFactory;
        this.isMoveing = true;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Deprecated
    protected void onMessage(double time, Message message) {
        // TODO execute program

//        if(onMessageHandler != null) {
//            onMessageHandler.apply(message);
//        }
//
//        if(message.getMessageDest() != id) {
//            List<DeviceNode<GeoPosition>> neighbors = wirelessChannel.scanForNeighbors(this);
//            List<DeviceNode<GeoPosition>> hopTargets = routingAlgorithm.getNodesToHop(this.id, message.getMessageDest(),message,neighbors);
//
//            startCommunication(message,hopTargets.toArray(new DeviceNode[hopTargets.size()]));
//
//        }

    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onMessage(double time, int communicationInterfaceId, Message message) {
        try {
            if (onMessageHandler != null) {
                onMessageHandler.apply(message);
            }
        } catch (Exception exc) {
            LOG.error(exc.getMessage() , exc);
        }

//        if(message.getMessageDest() != id) {
//            List<DeviceNode<GeoPosition>> neighbors = wirelessChannel.scanForNeighbors(communicationInterfaceId, this);
//            List<DeviceNode<GeoPosition>> hopTargets = routingAlgorithm.getNodesToHop(this.id, message.getMessageDest(),message,neighbors);
//
//            HashMap<Integer , List<DeviceNode<GeoPosition>>> receivers = new HashMap<>();
//            receivers.put(communicationInterfaceId , hopTargets);
//
//            startCommunication(message,receivers);
//
//        }
    }

    @Override
    public double getLatitude() {
        return position.getLatitude();
    }

    @Override
    public void setLatitude(double latitude) {
        this.position.setLongitude(latitude);
    }

    @Override
    public double getLongitude() {
        return position.getLongitude();
    }

    @Override
    public void setLongitude(double longitude) {
        position.setLongitude(longitude);
    }

    @Override
    public List<GeoPosition> getRoute() {
        return route;
    }

    @Override
    public void setRoute(List<GeoPosition> route) {
        this.route = route;
        currentMovementAlg.setRoute(route);
    }

    @Override
    public void startNode() {
        this.deviceSimEntity.startEntity();
    }

    @Override
    public void stopNode() {
        // unused
    }

    @Override
    public void pauseNode() {
        // unused
    }

    @Override
    public void resumeNode() {
        // unused
    }

    @Override
    @SuppressWarnings("unchecked")
    public void work() {

    }

    @Override
    public void move() {
        try {
//            LOG.trace(">> move node: " + getID());
            if (!isMoveing || route == null || route.size() == 0) return;
            // TODO założenie że pędkość podawana jest w kilomwtrach. Trzeba to przenieść do konfiguracji.
            double velocityMetersPerSec = this.velocity / 3.6;
            LOG.trace("Velocity in km/h: " + velocity + " velocity in m/sec: " + velocityMetersPerSec);

            GeoPosition newPosition = currentMovementAlg.nextPositionToCheckpoint(this.position, velocityMetersPerSec * MoveActivity.END_MOVE_DELAY);
            LOG.trace(String.format("moving from position %s to %s ", this.getPosition().toString(), newPosition.toString()));
            this.setPosition(newPosition);
            this.networkManager.actualizeNaighbours(this);
            EventBusHolder.getEventBus().post(EventFactory.createNodePositionChangedEvent(this));
//            LOG.trace("<< move node: " + getID());
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
    }

    @SafeVarargs
    @Override
    @Deprecated
    public final void startCommunication(Message message, DeviceNode<GeoPosition>... receivers) {
//        for(DeviceNode<GeoPosition> receiver : receivers) {
//            communicationProcessFactory.createCommunicationProcess(this, receiver, deviceSimEntity.getSimTime(), message);
//        }

    }

    @Override
    public void startCommunication(Message message, HashMap<Integer, List<DeviceNode<GeoPosition>>> receivers){

        for (Integer commInterface : receivers.keySet()) {
            List<DeviceNode<GeoPosition>> devicesToSend = receivers.get(commInterface);

            for(DeviceNode<GeoPosition> device : devicesToSend) {
                communicationProcessFactory.createCommunicationProcess(this, device, commInterface, deviceSimEntity.getSimTime(), message);
            }

        }
    }


    @Override
    public void messageSentFinished(int communicationInterface, Message message) {
        this.freeOutboundBandwith(communicationInterface , message.getSize());
    }

    // TODO metody do usunięcia w sytuacji kiedy interrupt będzie działać poprawnie
    public void stopMoveing() {
        this.isMoveing = false;
    }

    public void startMoveing() {
        this.isMoveing = true;
    }

    @Override
    public void setSimEntity(DeviceSimEntity simEntity) {
        this.deviceSimEntity = simEntity;
    }

    public void setDeviceStatistics(DeviceStatistics statistics) {
        this.deviceStatistics = statistics;
    }
}
