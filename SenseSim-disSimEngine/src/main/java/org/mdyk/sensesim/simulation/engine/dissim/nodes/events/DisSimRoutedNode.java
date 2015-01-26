package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import com.google.inject.assistedinject.Assisted;
import dissim.simspace.SimModel;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.movement.geo.GeoMovementAlgorithm;
import org.mdyk.netsim.logic.movement.geo.GeoRouteMovementAlgorithm;
import org.mdyk.netsim.logic.network.WirelessChannel;
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


    @Inject
    public DisSimRoutedNode(@Assisted("id") int id, @Assisted GeoPosition position,
                            @Assisted("radioRange") int radioRange,
                            @Assisted double velocity, @Assisted List<AbilityType> abilities,
                            Environment environment, WirelessChannel wirelessChannel) {
        super(id, position, radioRange, velocity, abilities);

        this.currentMovementAlg = new GeoRouteMovementAlgorithm();
        this.environment = environment;
        this.wirelessChannel = wirelessChannel;

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
    }

    @Override
    public void pauseNode() {
    }

    @Override
    public void resumeNode() {
    }

    @Override
    public void work() {
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

    @Override
    public void startCommunication(Object message, ISensorModel<GeoPosition>... receivers) {
        // TODO powołanie obiektu symulacyjnego odpowiedzialnego za komunikację
    }

    @Override
    public CommunicationStatus getCommunicationStatus() {
        return null;
    }



}
