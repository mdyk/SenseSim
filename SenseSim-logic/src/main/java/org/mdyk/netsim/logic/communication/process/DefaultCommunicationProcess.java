package org.mdyk.netsim.logic.communication.process;

import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.mathModel.device.IDeviceModel;

/**
 * Represents process of communication between two nodes.
 */
public class DefaultCommunicationProcess implements CommunicationProcess {

    private CommunicationStatus communicationStatus;
    private IDeviceModel<?> sender;
    private IDeviceModel<?> receiver;
    private int communicationInterfaceId;
    private double startTime;
    private double endTime;
    private int id;
    private double eta;
    private double messageBits;
    private double alreadySent;

    private Message message;

    public DefaultCommunicationProcess(int id, IDeviceModel<?> sender, IDeviceModel<?> receiver, int communicationInterfaceId, double startTime, Message message) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.startTime = startTime;
        this.endTime = Double.NaN;
        this.communicationInterfaceId = communicationInterfaceId;
        this.communicationStatus = CommunicationStatus.DURING_COMM;
        eta = startTime + calculateExpectedDuration();
        alreadySent = 0;
    }


    @Override
    public int getID() {
        return id;
    }

    @Override
    public CommunicationStatus getCommunicationStatus(double time) {
        // Possible only by external interruption
        if(communicationStatus == CommunicationStatus.FAILURE){
            endTime = time;
            return communicationStatus;
        }

        if(alreadySent < messageBits) {
            communicationStatus = CommunicationStatus.DURING_COMM;
        }
        else {
            endTime = time;
            communicationStatus = CommunicationStatus.SUCCESS;
        }

        return communicationStatus;
    }

    @Override
    public CommunicationStatus getCommunicationStatus() {
        return communicationStatus;
    }

    @Override
    public double getStartTime() {
        return startTime;
    }

    @Override
    public double getEndTime() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getETA() {
        return eta;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public void bitsSent(int bits) {
        alreadySent = bits;
    }

    @Override
    public void addBitsSent(int bits) {
        alreadySent += bits;
    }

    @Override
    public void processInterrupted() {
        this.communicationStatus = CommunicationStatus.FAILURE;
    }

    public IDeviceModel<?> getSender() {
        return sender;
    }

    public IDeviceModel<?> getReceiver() {
        return receiver;
    }

    @Override
    public int getCommunicationInterfaceId() {
        return this.communicationInterfaceId;
    }

    private double calculateExpectedDuration() {
        double communicationBandwith = Math.min(sender.getCommunicationInterface(communicationInterfaceId).getOutputBandwidth() , receiver.getCommunicationInterface(communicationInterfaceId).getInputBandwidth());
        messageBits = message.getSize() * 8;
        return messageBits / communicationBandwith;
    }
}
