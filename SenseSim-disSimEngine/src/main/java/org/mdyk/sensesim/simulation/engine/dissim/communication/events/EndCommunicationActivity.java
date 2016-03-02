package org.mdyk.sensesim.simulation.engine.dissim.communication.events;

import dissim.simspace.core.BasicSimStateChange;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.node.statistics.event.StatisticsEvent;
import org.mdyk.netsim.mathModel.device.SensorNode;

import java.util.List;

public class EndCommunicationActivity extends BasicSimStateChange<CommunicationProcessSimEntity , Object> {

    private static final Logger LOG = Logger.getLogger(EndCommunicationActivity.class);
    private SensorNode sender;
    private SensorNode receiver;
    private double delay;

    public EndCommunicationActivity(CommunicationProcessSimEntity entity, double delay) throws SimControlException {
        super(entity, delay);
        sender = (SensorNode) entity.commProcess.getSender();
        receiver = (SensorNode) entity.commProcess.getReceiver();
        this.delay = delay;
        EventBusHolder.getEventBus().register(this);
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.trace(">> EndCommunicationActivity.transition() [sender="+sender.getID()+" receiver="+receiver.getID()+"]");
        // Checking if receiver is still a neighbour for sender
        List<SensorNode> neighbours = getSimEntity().wirelessChannel.scanForNeighbors(sender);

        if(getSimEntity().getCommunicationStatus(simTime()).equals(CommunicationStatus.SUCCESS)) {
            LOG.trace("Communication is successful");
            receiver.receiveMessage(simTime(),getSimEntity().getMessage());
        } else if(neighbours.contains(receiver)) {
            LOG.trace("Receiver is neighbour of a sender");
            double bandwidth = Math.min(sender.getWirelessBandwith() , receiver.getWirelessBandwith());

            int sentBits = (int) Math.floor(bandwidth * delay);
            getSimEntity().addBitsSent(sentBits);

            getSimEntity().startCommunicationActivity = new StartCommunicationActivity(getSimEntity());
        } else {
            LOG.trace("Receiver is not neighbour of a sender");
            getSimEntity().processInterrupted();
        }
        EventBusHolder.getEventBus().post(new StatisticsEvent(StatisticsEvent.EventType.COMM_PROC_UPDATE , getSimEntity().commProcess));
        LOG.trace("<< EndCommunicationActivity.transition()");
    }

}
