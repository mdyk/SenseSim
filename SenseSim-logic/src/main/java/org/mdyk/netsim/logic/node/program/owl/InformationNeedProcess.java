package org.mdyk.netsim.logic.node.program.owl;


import org.mdyk.netsim.logic.node.program.owl.messages.InformationNeedAskMessage;
import org.mdyk.netsim.logic.node.program.owl.messages.InformationNeedRespMessage;

public class InformationNeedProcess {

    /**
     * Message which is source of the need
     */
    private InformationNeedAskMessage inam;
    private InformationNeedRespMessage inrm;
    private boolean answered;
    private INStatus status;

    public InformationNeedProcess(InformationNeedAskMessage inam) {
        this.inam = inam;
        this.answered = false;
        this.status = INStatus.RECEIVED;
    }

    public void setAnswer(InformationNeedRespMessage inrm) {
        this.inrm = inrm;
        this.status = INStatus.ANSWERED;
//        this.answered = true;
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

    public enum INStatus {
        RECEIVED,
        FOR_PROCESSING,
        ANSWERED
    }

}
