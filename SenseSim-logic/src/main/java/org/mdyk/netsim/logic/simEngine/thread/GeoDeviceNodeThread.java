package org.mdyk.netsim.logic.simEngine.thread;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.event.EventType;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.movement.geo.GeoMovementAlgorithm;
import org.mdyk.netsim.logic.movement.geo.GeoRouteMovementAlgorithm;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.logic.node.statistics.DeviceStatistics;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;

import javax.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.mdyk.netsim.mathModel.device.DeviceNode;

import java.util.HashMap;
import java.util.List;

/**
 * Simple implementation for nodes with geo localization
 */
public class GeoDeviceNodeThread extends DeviceNodeThread<GeoPosition, GeoMovementAlgorithm> implements DeviceLogic {

    private static final Logger LOG = Logger.getLogger(GeoDeviceNodeThread.class);
    protected List<GeoPosition> route;
    protected Environment environment;
    protected WirelessChannel wirelessChannel;

    @Inject
    public GeoDeviceNodeThread(@Assisted("id") int id, @Assisted GeoPosition position,
                               @Assisted("radioRange") int radioRange,
                               @Assisted double velocity, @Assisted List<AbilityType> abilities,
                               Environment environment, WirelessChannel wirelessChannel) {

        super(id, position, radioRange, velocity, abilities);
        this.currentMovementAlg = new GeoRouteMovementAlgorithm();
        this.environment = environment;
        this.wirelessChannel = wirelessChannel;
    }

//    @Override
//    public void sense() {
//
//        if(environment.isNodeInEventRegion(getPosition())){
//            LOG.debug("NODE_START_SENSE");
//            EventBusHolder.getEventBus().post(new InternalEvent(EventType.NODE_START_SENSE,this));
//
////            for(AbilityType ability : getAbilities()) {
////            //    addObservation(1.0, environment.getEventValue(getPosition(), 2, ability));
////            }
//
//        }
//        else {
//            LOG.debug("NODE_END_SENSE");
//            EventBusHolder.getEventBus().post(new InternalEvent(EventType.NODE_END_SENSE,this));
//        }
//
//    }

    @Override
    protected void onMessage(double time, org.mdyk.netsim.logic.communication.Message message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onMessage(double time, int communicationInterfaceId, Message message) {
        
    }

    @Override
    protected void init() {
        currentMovementAlg.setRoute(getRoute());
    }

    @Override
    public void setLatitude(double latitude) {
        this.position.setLatitude(latitude);
    }

    @Override
    public double getLatitude() {
        return this.position.getLatitude();
    }

    @Override
    public void setLongitude(double longitude) {
        this.position.setLongitude(longitude);
    }

    @Override
    public double getLongitude() {
        return this.position.getLongitude();
    }

    @Override
    protected void initPosition() {
        // TODO init position

    }

    @Override
    public void work() {
//        sense();
        move();
        try {
            // TODO konfigurowanie prędkości węzłów
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }

    @Override
    public void move() {
        LOG.debug(">> move node: " + getID());
        GeoPosition newPosition = currentMovementAlg.nextPositionToCheckpoint(this.position, this.velocity);
        LOG.debug(String.format("moveing from position %s to %s ", this.getPosition().toString(), newPosition.toString()));
        this.setPosition(newPosition);
        EventBusHolder.getEventBus().post(EventFactory.createNodePositionChangedEvent(this));
        LOG.debug("<< move node: " + getID());
    }

    @Override
    public void startCommunication(Message message, DeviceNode<GeoPosition>... receivers) {
        // empty
    }

    @Override
    public void startCommunication(Message message, HashMap<Integer, List<DeviceNode<GeoPosition>>> receivers) {
        
    }

    @Override
    public void messageSentFinished(int communicationInterface, Message message) {

    }


    @Override
    public List<GeoPosition> getRoute() {
        return this.route;
    }

    @Override
    public void setRoute(List<GeoPosition> route) {
        this.route = route;
    }

    @Override
    public void setSimEntity(DeviceSimEntity simEntity) {
        //Empty
    }

    public void setDeviceStatistics(DeviceStatistics statistics) {

    }

}
