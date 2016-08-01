package org.mdyk.netsim.logic.infon.message;



public class InformationNeedContent {

    private int askingNodeId;
    private String informationNeedString;

    public InformationNeedContent(int askingNodeId, String informationNeedString) {
        this.askingNodeId = askingNodeId;
        this.informationNeedString = informationNeedString;
    }

    public int getAskingNodeId() {
        return askingNodeId;
    }

    public String getInformationNeedString() {
        return informationNeedString;
    }
}
