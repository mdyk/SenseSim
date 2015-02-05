package org.mdyk.netsim.logic.communication.process;

import org.mdyk.netsim.logic.communication.message.Message;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

/**
 * Represents process of communication between two nodes.
 */
public class DefaultCommunicationProcess implements CommunicationProcess {

    private CommunicationStatus communicationStatus;
    private ISensorModel<?> sender;
    private ISensorModel<?> receiver;
    private double startTime;
    private int id;
    private double eta;
    private double messageBits;
    private double alreadySent;

    // TODO opakować w dedykowaną strukturę
    private Message message;

    public DefaultCommunicationProcess(int id, ISensorModel<?> sender, ISensorModel<?> receiver, double startTime, Message message) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.startTime = startTime;
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

        if(communicationStatus == CommunicationStatus.FAILURE){
            return communicationStatus;
        }

        if(time <= eta) {
            if(alreadySent >= messageBits) {
                communicationStatus = CommunicationStatus.SUCCESS;
            } else if (alreadySent < messageBits) {
                communicationStatus = CommunicationStatus.DURING_COMM;
            }
        } else {
            if(alreadySent >= messageBits) {
                communicationStatus = CommunicationStatus.SUCCESS;
            } else {
                communicationStatus = CommunicationStatus.FAILURE;
            }
        }

        return communicationStatus;
    }

    @Override
    public double getStartTime() {
        return startTime;
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

    public ISensorModel<?> getSender() {
        return sender;
    }

    public ISensorModel<?> getReceiver() {
        return receiver;
    }

    private double calculateExpectedDuration() {
        double communicationBandwith = Math.min(sender.getWirelessBandwith() , receiver.getWirelessBandwith());
        messageBits = message.getSize() * 8;
        return messageBits / communicationBandwith;
    }
}
