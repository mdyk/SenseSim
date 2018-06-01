package org.mdyk.sensesim.simulation.engine.dissim.communication.events;

import dissim.simspace.core.SimControlException;
import dissim.simspace.process.BasicSimAction;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.node.statistics.event.DeviceStatisticsEvent;
import org.mdyk.netsim.logic.reporting.CommunicationReport;
import org.mdyk.netsim.logic.reporting.CommunicationReportBean;
import org.mdyk.netsim.mathModel.device.DeviceNode;

import java.util.List;

/**
 * Created by Michal on 2017-05-11.
 */
public class CommunicationActivity extends BasicSimAction<CommunicationProcessSimEntity , Object> {

    private static final Logger LOG = Logger.getLogger(CommunicationActivity.class);
    private static  final double duration = 0.1;
    CommunicationReportBean crb;
    private DeviceNode sender;
    private DeviceNode receiver;
    private double period;

    public CommunicationActivity(CommunicationProcessSimEntity entity, double period) throws SimControlException {
        super(entity,0.0 , period, duration);
        sender = (DeviceNode) entity.commProcess.getSender();
        receiver = (DeviceNode) entity.commProcess.getReceiver();
        this.period = period;
        crb = new CommunicationReportBean();
        EventBusHolder.getEventBus().register(this);
    }

    @Override
    protected void transitionOnStart() throws SimControlException {
        LOG.trace(">< CommunicationActivity.transitionOnStart() [sender="+sender.getID()+" receiver="+receiver.getID()+"]");

        //TODO zalogowanie rozpoczęcia z czasem sim

        double startTime = simTime();
        String messageContent = getSimEntity().getMessage().getMessageString();
        int messageSize = getSimEntity().getMessage().getSize();
        long messageId = getSimEntity().getMessage().getID();
        int commProcId = getSimEntity().getID();
        CommunicationStatus commStatus = getSimEntity().getCommunicationStatus();


        crb.setSender(sender.getID());
        crb.setSender(receiver.getID());
        crb.setSimTimeStart(startTime);
        crb.setSimTimeEnd(-1);
        crb.setMessageContent(messageContent);
        crb.setMessageSize(messageSize);
        crb.setMessageId(messageId);
        crb.setCommProcId(commProcId);
        crb.setCommStatus(commStatus.name());

        CommunicationReport.updateCommReport(crb);
    }

    @Override
    protected void transitionOnFinish() throws SimControlException {
        LOG.trace(">> CommunicationActivity.transitionOnFinish() [sender="+sender.getID()+" receiver="+receiver.getID()+"]");
        // Checking if receiver is still a neighbour for sender
        int communicationInterfaceId = getSimEntity().getCommunicationInterfaceId();

        List<DeviceNode> neighbours = getSimEntity().wirelessChannel.scanForNeighbors(communicationInterfaceId,sender);

        if(getSimEntity().getCommunicationStatus(simTime()).equals(CommunicationStatus.SUCCESS)) {
            LOG.trace("Communication is successful");
            receiver.receiveMessage(simTime(),communicationInterfaceId,getSimEntity().getMessage());

            this.terminate();
            this.deactivateRepetition();

            crb.setSimTimeEnd(simTime());
            crb.setCommStatus(CommunicationStatus.SUCCESS.name());
            CommunicationReport.updateCommReport(crb);

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

        } else {
            LOG.trace("Receiver is not neighbour of a sender");
            getSimEntity().processInterrupted();
            this.terminate();
            this.deactivateRepetition();

            crb.setSimTimeEnd(simTime());
            crb.setCommStatus(CommunicationStatus.FAILURE.name());
            CommunicationReport.updateCommReport(crb);
        }
        EventBusHolder.getEventBus().post(new DeviceStatisticsEvent(DeviceStatisticsEvent.EventType.COMM_PROC_UPDATE , getSimEntity().commProcess));
        LOG.trace("<< CommunicationActivity.transitionOnFinish()");
    }
}
