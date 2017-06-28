package org.mdyk.sensesim.simulation.engine.dissim.communication.events;

import dissim.simspace.core.SimControlException;
import dissim.simspace.process.BasicSimAction;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.node.statistics.event.DeviceStatisticsEvent;
import org.mdyk.netsim.mathModel.device.DeviceNode;

import java.util.List;

/**
 * Created by Michal on 2017-05-11.
 */
public class CommunicationActivity extends BasicSimAction<CommunicationProcessSimEntity , Object> {

    private static final Logger LOG = Logger.getLogger(CommunicationActivity.class);
    private DeviceNode sender;
    private DeviceNode receiver;
    private double period;
    private static  final double duration = 0.1;

    public CommunicationActivity(CommunicationProcessSimEntity entity, double period) throws SimControlException {
        super(entity,0.0 , period, duration);
        sender = (DeviceNode) entity.commProcess.getSender();
        receiver = (DeviceNode) entity.commProcess.getReceiver();
        this.period = period;
        EventBusHolder.getEventBus().register(this);
    }

    @Override
    protected void transitionOnStart() throws SimControlException {
        
    }

    @Override
    protected void transitionOnFinish() throws SimControlException {
        LOG.trace(">> EndCommunicationActivity.transition() [sender="+sender.getID()+" receiver="+receiver.getID()+"]");
        // Checking if receiver is still a neighbour for sender
        int communicationInterfaceId = getSimEntity().getCommunicationIntterfaceId();
        List<DeviceNode> neighbours = getSimEntity().wirelessChannel.scanForNeighbors(communicationInterfaceId,sender);

        if(getSimEntity().getCommunicationStatus(simTime()).equals(CommunicationStatus.SUCCESS)) {
            LOG.trace("Communication is successful");
            receiver.receiveMessage(simTime(),communicationInterfaceId,getSimEntity().getMessage());
            this.terminate();
            this.deactivateRepetition();
        } else if(neighbours.contains(receiver)) {
            LOG.trace("Receiver is neighbour of a sender");
            // Both sender and receiver have the same communication interface type, which is assured by the scna
            double bandwidth = Math.min(sender.getCommunicationInterface(communicationInterfaceId).getOutputBandwidth() , receiver.getCommunicationInterface(communicationInterfaceId).getInputBandwidth());

            int sentBits = (int) Math.floor(bandwidth * (period + duration));
            getSimEntity().addBitsSent(sentBits);

            if(getSimEntity().getCommunicationStatus(simTime()).equals(CommunicationStatus.SUCCESS)) {
                receiver.receiveMessage(simTime(),communicationInterfaceId,getSimEntity().getMessage());
                this.terminate();
                this.deactivateRepetition();
            }

//            getSimEntity().startCommunicationActivity = new StartCommunicationActivity(getSimEntity());
        } else {
            LOG.trace("Receiver is not neighbour of a sender");
            getSimEntity().processInterrupted();
            this.terminate();
            this.deactivateRepetition();
        }
        EventBusHolder.getEventBus().post(new DeviceStatisticsEvent(DeviceStatisticsEvent.EventType.COMM_PROC_UPDATE , getSimEntity().commProcess));
        LOG.trace("<< EndCommunicationActivity.transition()");
    }
}
