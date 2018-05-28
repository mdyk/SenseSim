package org.mdyk.netsim.logic.infon.message;


import org.mdyk.netsim.logic.infon.Infon;

@Deprecated
/**
 * w zastępsitwie użyć wiadomości związanych z potrzebą informacyjną
 */
public class InformationNeedContent {

    private int askingNodeId;
    private String informationNeedString;
    private Infon infon;

    public InformationNeedContent(int askingNodeId, String informationNeedString) {
        this.askingNodeId = askingNodeId;
        this.informationNeedString = informationNeedString;
        this.infon = new Infon(informationNeedString);
    }

    public int getAskingNodeId() {
        return askingNodeId;
    }

    public String getInformationNeedString() {
        return informationNeedString;
    }

    public Infon getInfon() {
        return infon;
    }


    @Override
    public String toString() {
        return informationNeedString;
    }
}
