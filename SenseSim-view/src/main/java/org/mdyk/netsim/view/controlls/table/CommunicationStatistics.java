package org.mdyk.netsim.view.controlls.table;


import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.process.CommunicationProcess;

public class CommunicationStatistics {

    private int commId;
    private String receiverId;
    private String status;
    private String startTime;
    private String endTime;
    private double messageSize;
    private Message message;

    public CommunicationStatistics(int commId, String receiverId, String status, String startTime, String endTime, double messageSize) {
        this.commId = commId;
        this.receiverId = receiverId;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.messageSize = messageSize;
    }

    public CommunicationStatistics(CommunicationProcess communicationProcess) {
        this.commId = communicationProcess.getID();
        this.receiverId = String.valueOf(communicationProcess.getReceiver().getID());
        this.status = communicationProcess.getCommunicationStatus().name();
        this.startTime = String.valueOf(communicationProcess.getStartTime());
        this.endTime = String.valueOf(communicationProcess.getEndTime());
        this.messageSize = communicationProcess.getMessage().getSize();
        this.message = communicationProcess.getMessage();
    }

    public int getCommId() {
        return commId;
    }

    public void setCommId(int commId) {
        this.commId = commId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(double messageSize) {
        this.messageSize = messageSize;
    }

    public Message getMessage() {
        return message;
    }
}


