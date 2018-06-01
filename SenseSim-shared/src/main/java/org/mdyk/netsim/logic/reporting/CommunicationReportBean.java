package org.mdyk.netsim.logic.reporting;


import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class CommunicationReportBean {

    @CsvBindByName
    @CsvBindByPosition(position = 0)
    private double simTimeStart;
    @CsvBindByName
    @CsvBindByPosition(position = 1)
    private double simTimeEnd;
    @CsvBindByName
    @CsvBindByPosition(position = 2)
    private int sender;
    @CsvBindByName
    @CsvBindByPosition(position = 3)
    private int receiver;
    @CsvBindByName
    @CsvBindByPosition(position = 4)
    private String messageType;
    @CsvBindByName
    @CsvBindByPosition(position = 5)
    private String messageContent;
    @CsvBindByName
    @CsvBindByPosition(position = 6)
    private long messageId;
    @CsvBindByName
    @CsvBindByPosition(position = 7)
    private int messageSize;
    @CsvBindByName
    @CsvBindByPosition(position = 8)
    private int commProcId;
    @CsvBindByName
    @CsvBindByPosition(position = 9)
    private String commStatus;


    public double getSimTimeStart() {
        return simTimeStart;
    }

    public void setSimTimeStart(double simTimeStart) {
        this.simTimeStart = simTimeStart;
    }

    public double getSimTimeEnd() {
        return simTimeEnd;
    }

    public void setSimTimeEnd(double simTimeEnd) {
        this.simTimeEnd = simTimeEnd;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public int getCommProcId() {
        return commProcId;
    }

    public void setCommProcId(int commProcId) {
        this.commProcId = commProcId;
    }

    public String getCommStatus() {
        return commStatus;
    }

    public void setCommStatus(String commStatus) {
        this.commStatus = commStatus;
    }
}
