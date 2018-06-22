package org.mdyk.netsim.logic.reporting;


import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class CommunicationReportBean {

    @CsvBindByName
    @CsvBindByPosition(position = 0)
    private Double simTimeStart;
    @CsvBindByName
    @CsvBindByPosition(position = 1)
    private Double simTimeEnd;
    @CsvBindByName
    @CsvBindByPosition(position = 2)
    private Integer sender;
    @CsvBindByName
    @CsvBindByPosition(position = 3)
    private Integer receiver;
//    @CsvBindByName
//    @CsvBindByPosition(position = 4)
//    private String messageType;
    @CsvBindByName
    @CsvBindByPosition(position = 4)
    private String messageContent;
    @CsvBindByName
    @CsvBindByPosition(position = 5)
    private Long messageId;
    @CsvBindByName
    @CsvBindByPosition(position = 6)
    private Integer messageSize;
    @CsvBindByName
    @CsvBindByPosition(position = 7)
    private Integer commProcId;
    @CsvBindByName
    @CsvBindByPosition(position = 8)
    private String commStatus;


    public CommunicationReportBean() {
    }

    public CommunicationReportBean(CommunicationReportBean crb) {
        this.simTimeStart = crb.simTimeStart;
        this.simTimeEnd = crb.simTimeEnd;
        this.sender = crb.sender;
        this.receiver = crb.receiver;
//        this.messageType = crb.messageType;
        this.messageContent = crb.messageContent;
        this.messageId = crb.messageId;
        this.messageSize = crb.messageSize;
        this.commProcId = crb.commProcId;
        this.commStatus = crb.commStatus;
    }

    public Double getSimTimeStart() {
        return simTimeStart;
    }

    public void setSimTimeStart(Double simTimeStart) {
        this.simTimeStart = simTimeStart;
    }

    public Double getSimTimeEnd() {
        return simTimeEnd;
    }

    public void setSimTimeEnd(Double simTimeEnd) {
        this.simTimeEnd = simTimeEnd;
    }

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

//    public String getMessageType() {
//        return messageType;
//    }
//
//    public void setMessageType(String messageType) {
//        this.messageType = messageType;
//    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Integer getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(Integer messageSize) {
        this.messageSize = messageSize;
    }

    public Integer getCommProcId() {
        return commProcId;
    }

    public void setCommProcId(Integer commProcId) {
        this.commProcId = commProcId;
    }

    public String getCommStatus() {
        return commStatus;
    }

    public void setCommStatus(String commStatus) {
        this.commStatus = commStatus;
    }
}
