package org.mdyk.sensesim.simulation.engine.dissim.communication.events;

import dissim.simspace.core.BasicSimStateChange;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.node.statistics.event.DeviceStatisticsEvent;
import org.mdyk.netsim.mathModel.device.DeviceNode;

import java.util.List;

public class EndCommunicationActivity extends BasicSimStateChange<CommunicationProcessSimEntity , Object> {

    private static final Logger LOG = Logger.getLogger(EndCommunicationActivity.class);
    private DeviceNode sender;
    private DeviceNode receiver;
    private double delay;

    public EndCommunicationActivity(CommunicationProcessSimEntity entity, double delay) throws SimControlException {
        super(entity, delay);
        sender = (DeviceNode) entity.commProcess.getSender();
        receiver = (DeviceNode) entity.commProcess.getReceiver();
        this.delay = delay;
        EventBusHolder.getEventBus().register(this);
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.trace(">> EndCommunicationActivity.transition() [sender="+sender.getID()+" receiver="+receiver.getID()+"]");
        // Checking if receiver is still a neighbour for sender
        int communicationInterfaceId = getSimEntity().getCommunicationIntterfaceId();
        List<DeviceNode> neighbours = getSimEntity().wirelessChannel.scanForNeighbors(communicationInterfaceId,sender);

        if(getSimEntity().getCommunicationStatus(simTime()).equals(CommunicationStatus.SUCCESS)) {
            LOG.trace("Communication is successful");
            receiver.receiveMessage(simTime(),getSimEntity().getMessage());
        } else if(neighbours.contains(receiver)) {
            LOG.trace("Receiver is neighbour of a sender");
            // Both sender and receiver have the same communication interface type, which is assured by the scan for neighbors process
            double bandwidth = Math.min(sender.getCommunicationInterface(communicationInterfaceId).getOutputBandwidth() , receiver.getCommunicationInterface(communicationInterfaceId).getInputBandwidth());

            int sentBits = (int) Math.floor(bandwidth * delay);
            getSimEntity().addBitsSent(sentBits);

            getSimEntity().startCommunicationActivity = new StartCommunicationActivity(getSimEntity());
        } else {
            LOG.trace("Receiver is not neighbour of a sender");
            getSimEntity().processInterrupted();
        }
        EventBusHolder.getEventBus().post(new DeviceStatisticsEvent(DeviceStatisticsEvent.EventType.COMM_PROC_UPDATE , getSimEntity().commProcess));
        LOG.trace("<< EndCommunicationActivity.transition()");
    }

}
