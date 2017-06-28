package org.mdyk.netsim.mathModel.observer.rfid;

import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;


public class RfidConfigurationSpace extends ConfigurationSpace {

    private String rfidTagData;

    public RfidConfigurationSpace(String rfidTagData) {
        this.rfidTagData = rfidTagData;
    }

    public String getRfidData() {
        return rfidTagData;
    }

    @Override
    public String getStringValue() {
        return rfidTagData;
    }

    public String getRfidTagData() {
        return rfidTagData;
    }
}
