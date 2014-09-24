package org.mdyk.netsim.logic.communication;

import org.mdyk.netsim.logic.communication.message.Message;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

/**
 * Represents process of communication between two nodes.
 */
public class CommunicationProcess {

    private CommunicationStatus communicationStatus;
    private ISensorModel<?> sender;
    private ISensorModel<?> receiver;
    private double startTime;

    // TODO opakować w dedykowaną strukturę
    private Message message;

    public CommunicationProcess(ISensorModel<?> sender, ISensorModel<?> receiver, double startTime, Message message) {
        this.sender = sender;
        this.sender = receiver;
        this.message = message;
        this.startTime = startTime;
        this.communicationStatus = CommunicationStatus.DURING_COMM;
    }




}
