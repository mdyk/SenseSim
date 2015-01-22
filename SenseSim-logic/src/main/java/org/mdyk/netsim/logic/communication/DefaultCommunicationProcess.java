package org.mdyk.netsim.logic.communication;

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
    }


    @Override
    public int getID() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CommunicationStatus getCommunicationStatus() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getStartTime() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getETA() {
        return eta;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private double calculateExpectedDuration() {
        double expectedDuration = Double.NaN;
        double communicationBandwith = Math.min(sender.getWirelessBandwith() , receiver.getWirelessBandwith());
        int messageBits = message.getSize() * 8;

        expectedDuration = messageBits / communicationBandwith;

        return expectedDuration;
    }
}
