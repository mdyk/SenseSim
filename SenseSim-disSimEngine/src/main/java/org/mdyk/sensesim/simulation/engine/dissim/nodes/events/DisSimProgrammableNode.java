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
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.geo.ProgrammableNode;
import org.mdyk.netsim.mathModel.sensor.SensorNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.DefaultSensorModel;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Function;


public class DisSimProgrammableNode extends DefaultSensorModel<GeoPosition> implements ProgrammableNode {


    private static final Logger LOG = Logger.getLogger(DisSimProgrammableNode.class);

    protected List<GeoPosition> route;
    protected GeoMovementAlgorithm currentMovementAlg;
    protected Environment environment;
    protected WirelessChannel wirelessChannel;
    protected DisSimNodeEntity disSimNodeEntity;
    protected CommunicationProcessFactory communicationProcessFactory;

    protected Function<Message<?>, Object> onMessageHandler;

    private int commProcIdx = 0;

    private boolean isMoveing;


    @Inject
    public DisSimProgrammableNode(@Assisted("id") int id, @Assisted GeoPosition position,
                                  @Assisted("radioRange") int radioRange,
                                  @Assisted double velocity, @Assisted List<AbilityType> abilities,
                                  Environment environment, WirelessChannel wirelessChannel, CommunicationProcessFactory communicationProcessFactory) {
        super(id, position, radioRange, velocity, abilities);

        this.currentMovementAlg = new GeoRouteMovementAlgorithm();
        this.environment = environment;
        this.wirelessChannel = wirelessChannel;
        this.communicationProcessFactory = communicationProcessFactory;
        this.isMoveing = true;
        this.disSimNodeEntity = new DisSimNodeEntity(SimModel.getInstance().getCommonSimContext() , this);
    }

    @Override
    public void sense() {

        for(AbilityType ability : getAbilities()) {
            PhenomenonValue phenomenonValue = environment.getEventValue(getPosition(),SimModel.getInstance().simTime(), ability);
            this.addObservation(ability, SimModel.getInstance().simTime(), phenomenonValue);
        }

        EventBusHolder.getEventBus().post(EventFactory.startSenseEvent(this));

    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onMessage(double time, Message message) {
        // TODO execute program
        if(message.getMessageDest() != id) {
            List<SensorNode<GeoPosition>> neighbors = wirelessChannel.scanForNeighbors(this);
            List<SensorNode<GeoPosition>> hopTargets = routingAlgorithm.getNodesToHop(this.id, message.getMessageDest(),message,neighbors);

            startCommunication(message,hopTargets.toArray(new SensorNode[hopTargets.size()]));

        }

        if(onMessageHandler != null) {
            onMessageHandler.apply(message);
        }

    }

    @Override
    public void setLatitude(double latitude) {
        this.position.setLongitude(latitude);
    }

    @Override
    public double getLatitude() {
        return position.getLatitude();
    }

    @Override
    public void setLongitude(double longitude) {
        position.setLongitude(longitude);
    }

    @Override
    public double getLongitude() {
        return position.getLongitude();
    }

    @Override
    public List<GeoPosition> getRoute() {
        return route;
    }

    @Override
    public void setRoute(List<GeoPosition> route) {
        this.route = route;
        currentMovementAlg.setRoute(getRoute());
    }

    @Override
    public void startNode() {
        this.disSimNodeEntity.startNode();
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
        LOG.debug(">> move node: " + getID());
        if(!isMoveing || route == null || route.size() == 0) return;
        // TODO założenie że pędkość podawana jest w kilomwtrach. Trzeba to przenieść do konfiguracji.
        double velocityMetersPerSec = this.velocity / 3.6;
        LOG.trace("Velocity in km/h: " + velocity + " velocity in m/sec: " +velocityMetersPerSec);

        GeoPosition newPosition = currentMovementAlg.nextPositionToCheckpoint(this.position, velocityMetersPerSec*StartMoveActivity.END_MOVE_DELAY);
        LOG.debug(String.format("moveing from position %s to %s ", this.getPosition().toString(), newPosition.toString()));
        this.setPosition(newPosition);
        EventBusHolder.getEventBus().post(EventFactory.createNodePositionChangedEvent(this));
        LOG.debug("<< move node: " + getID());
    }

    @SafeVarargs
    @Override
    public final void startCommunication(Message message, SensorNode<GeoPosition>... receivers) {
        for(SensorNode<GeoPosition> receiver : receivers) {
            communicationProcessFactory.createCommunicationProcess(commProcIdx++, this, receiver, SimModel.getInstance().simTime(), message);
        }

    }

    @Override
    public SensorAPI<GeoPosition> getAPI() {
        return disSimNodeEntity;
    }

    // TODO metody do usunięcia w sytuacji kiedy interrupt będzie działać poprawnie
    public void stopMoveing() {
        this.isMoveing = false;
    }

    public void startMoveing() {
        this.isMoveing = true;
    }

}
