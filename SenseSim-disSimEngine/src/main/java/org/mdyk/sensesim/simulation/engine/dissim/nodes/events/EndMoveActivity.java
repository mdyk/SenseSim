package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.util.GeoPosition;


public class EndMoveActivity extends BasicSimStateChange<EventsRoutedSensorNode, StartMoveActivity> {

    private static final Logger logger = Logger.getLogger(EndMoveActivity.class);

    private EventsRoutedSensorNode eventsRoutedSensorNode;

    public EndMoveActivity(double delay, EventsRoutedSensorNode eventsRoutedSensorNode, StartMoveActivity startMoveActivity) throws SimControlException {
        super(eventsRoutedSensorNode, delay);
        this.eventsRoutedSensorNode = eventsRoutedSensorNode;
    }

    @Override
    protected void transition() throws SimControlException {
        System.out.println(">>>> EndMoveActivity.transition");

        System.out.println("-------- Koniec ruchu [" + simTime() + "] -------");

        logger.debug(">> move node: "+ eventsRoutedSensorNode.wrapper.getID());
        GeoPosition newPosition = eventsRoutedSensorNode.wrapper.currentMovementAlg.nextPositionToCheckpoint(eventsRoutedSensorNode.wrapper.getPosition(), eventsRoutedSensorNode.wrapper.getVelocity());
        logger.debug(String.format("moveing from position %s to %s ",eventsRoutedSensorNode.wrapper.getPosition().toString(),newPosition.toString()));
        eventsRoutedSensorNode.wrapper.setPosition(newPosition);
        EventBusHolder.getEventBus().post(EventFactory.createNodePositionChangedEvent(eventsRoutedSensorNode.wrapper));
        logger.debug("<< move node: "+ eventsRoutedSensorNode.wrapper.getID());

        eventsRoutedSensorNode.startMoveActivity = new StartMoveActivity(eventsRoutedSensorNode, 1.0);

        System.out.println("<<<< EndMoveActivity.transition");
    }

    @Override
    protected void onTermination() throws SimControlException {
        /* EMPTY */
    }

    @Override
    protected void onInterruption() throws SimControlException {
        /* EMPTY */
    }
}
