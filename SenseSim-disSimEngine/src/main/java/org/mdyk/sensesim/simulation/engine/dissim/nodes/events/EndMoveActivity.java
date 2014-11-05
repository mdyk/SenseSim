package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.util.GeoPosition;


public class EndMoveActivity extends BasicSimStateChange<DisSimRoutedSensorNodeEntity, StartMoveActivity> {

    private static final Logger LOG = Logger.getLogger(EndMoveActivity.class);

    private DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity;

    public EndMoveActivity(double delay, DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity, StartMoveActivity startMoveActivity) throws SimControlException {
        super(disSimRoutedSensorNodeEntity, delay);
        this.disSimRoutedSensorNodeEntity = disSimRoutedSensorNodeEntity;
    }

    @Override
    protected void transition() throws SimControlException {
        System.out.println(">>>> EndMoveActivity.transition");

        System.out.println("-------- Koniec ruchu [" + simTime() + "] -------");

        LOG.debug(">> move node: " + disSimRoutedSensorNodeEntity.getWrapper().getID());
        GeoPosition newPosition = disSimRoutedSensorNodeEntity.getWrapper().currentMovementAlg.nextPositionToCheckpoint(disSimRoutedSensorNodeEntity.getWrapper().getPosition(), disSimRoutedSensorNodeEntity.getWrapper().getVelocity());
        LOG.debug(String.format("moveing from position %s to %s ", disSimRoutedSensorNodeEntity.getWrapper().getPosition().toString(), newPosition.toString()));
        disSimRoutedSensorNodeEntity.getWrapper().setPosition(newPosition);
        EventBusHolder.getEventBus().post(EventFactory.createNodePositionChangedEvent(disSimRoutedSensorNodeEntity.getWrapper()));
        LOG.debug("<< move node: " + disSimRoutedSensorNodeEntity.getWrapper().getID());

        disSimRoutedSensorNodeEntity.startMoveActivity = new StartMoveActivity(disSimRoutedSensorNodeEntity);

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
