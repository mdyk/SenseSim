package org.mdyk.netsim.logic.node.program.owl;


import org.mdyk.netsim.logic.node.program.owl.messages.InformationNeedAskMessage;
import org.mdyk.netsim.logic.node.program.owl.messages.InformationNeedRespMessage;

import java.util.ArrayList;

public class InformationNeedProcess {

    /**
     * Message which is source of the need
     */
    private InformationNeedAskMessage inam;
    private InformationNeedRespMessage inrm;
    private boolean answered;
    private INStatus status;

    private ArrayList<Integer> resendedTo;

    public InformationNeedProcess(InformationNeedAskMessage inam) {
        this.inam = inam;
        this.answered = false;
        this.status = INStatus.RECEIVED;
        this.resendedTo = new ArrayList<>();

    }

    public void setAnswer(InformationNeedRespMessage inrm) {
        this.inrm = inrm;
        this.status = INStatus.ANSWERED;
//        this.answered = true;
    }

    public void addSentTo(int nodeId) {
        resendedTo.add(nodeId);
    }

    public boolean wasSentTo(int nodeId) {
        return resendedTo.contains(nodeId);
    }


    public void setAnswered() {
        this.answered = true;
    }

    public InformationNeedAskMessage getInam() {
        return inam;
    }

    public InformationNeedRespMessage getInrm() {
        return inrm;
    }

    public INStatus getStatus() {
        return status;
    }

    public boolean isAnswered() {
        return this.answered;
    }

    public int getId () {
        return this.inam.getId();
    }

    public boolean wasProcessedBy(int nodeId) {
        return inam.wasProcessedBy(nodeId);
    }



    public enum INStatus {
        RECEIVED,
        FOR_PROCESSING,
        ANSWERED
    }

}
