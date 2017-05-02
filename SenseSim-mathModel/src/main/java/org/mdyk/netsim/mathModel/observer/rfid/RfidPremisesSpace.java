package org.mdyk.netsim.mathModel.observer.rfid;

import org.mdyk.netsim.mathModel.observer.PresmisesSpace;


// TODO do zmiany kiedy zostanie opracowany model obserwacji sygnału RFID
public class RfidPremisesSpace extends PresmisesSpace {

    private String rfidTagData;

    public RfidPremisesSpace(String rfidTagData) {
        this.rfidTagData = rfidTagData;
    }

    public String getRfidTagData() {
        return rfidTagData;
    }
}
