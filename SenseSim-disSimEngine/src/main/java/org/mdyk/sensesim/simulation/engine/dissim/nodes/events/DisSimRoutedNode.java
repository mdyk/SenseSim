package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import com.google.inject.assistedinject.Assisted;
import dissim.simspace.SimModel;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.mathModel.communication.Message;
import org.mdyk.netsim.logic.communication.message.SimpleMessage;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.movement.geo.GeoMovementAlgorithm;
import org.mdyk.netsim.logic.movement.geo.GeoRouteMovementAlgorithm;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.SensorNode;
import org.mdyk.netsim.logic.node.geo.RoutedGeoSensorNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.DefaultSensorModel;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

import javax.inject.Inject;
import java.util.List;


public class DisSimRoutedNode extends DefaultSensorModel<GeoPosition> implements RoutedGeoSensorNode {


    private static final Logger LOG = Logger.getLogger(DisSimRoutedNode.class);

    protected List<GeoPosition> route;
    protected GeoMovementAlgorithm currentMovementAlg;
    protected Environment environment;
    protected WirelessChannel wirelessChannel;
    protected DisSimNodeEntity disSimNodeEntity;
    protected CommunicationProcessFactory communicationProcessFactory;

    private int commProcIdx = 0;

    @Inject
    public DisSimRoutedNode(@Assisted("id") int id, @Assisted GeoPosition position,
                            @Assisted("radioRange") int radioRange,
                            @Assisted double velocity, @Assisted List<AbilityType> abilities,
                            Environment environment, WirelessChannel wirelessChannel, CommunicationProcessFactory communicationProcessFactory) {
        super(id, position, radioRange, velocity, abilities);

        this.currentMovementAlg = new GeoRouteMovementAlgorithm();
        this.environment = environment;
        this.wirelessChannel = wirelessChannel;
        this.communicationProcessFactory = communicationProcessFactory;
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
    protected void onMessage(double time, Message message) {
        // TODO execute program
        if(!message.getMessageDest().equals(this)) {
            /* TODO wykonanie kolejnego skoku */
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
        disSimNodeEntity = new DisSimNodeEntity(SimModel.getInstance().getCommonSimContext() , this);
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
    public void work() {
        // TODO opracowanie podstawowych zadań węzła
        List<SensorNode> neighbours = wirelessChannel.scanForNeighbors(this);

        for (SensorNode node : neighbours) {
            Message m = new SimpleMessage(this, node, "message", 5000);
            startCommunication(m,node);
        }

    }

    @Override
    public void move() {
        LOG.debug(">> move node: " + getID());

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
    public final void startCommunication(Message message, ISensorModel<GeoPosition>... receivers) {
        // TODO powołanie obiektu symulacyjnego odpowiedzialnego za komunikację
        for(ISensorModel<GeoPosition> receiver : receivers) {
            communicationProcessFactory.createCommunicationProcess(commProcIdx++, this, receiver, SimModel.getInstance().simTime(), message);
        }

    }

    @Override
    public CommunicationStatus getCommunicationStatus() {
        return null;
    }

}
