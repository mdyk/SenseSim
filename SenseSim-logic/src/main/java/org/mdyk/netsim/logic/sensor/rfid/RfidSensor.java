package org.mdyk.netsim.logic.sensor.rfid;


import org.mdyk.netsim.mathModel.observer.rfid.RfidConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.rfid.RfidObserver;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

public class RfidSensor extends SensorModel<RfidObserver , RfidConfigurationSpace> {

    private RfidObserver rfidObserver;

    public RfidSensor() {
        this.rfidObserver = new RfidObserver();
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return "RFID reader";
    }

    @Override
    public RfidObserver getObserver() {
        return rfidObserver;
    }

    @Override
    public double samplingFrequency() {
        return 10;
    }

    @Override
    public double sensingTime() {
        return 2;
    }

    @Override
    public RfidConfigurationSpace getObservation(PhenomenonModel phenomenonModel, double time, double distance) {
        //FIXME poprawne uwzględnienie odległości
        if(distance > 0 ) {
            return null;
        }

        RfidConfigurationSpace event = (RfidConfigurationSpace) phenomenonModel.getEventValue(RfidConfigurationSpace.class , time);
        if(event != null) {

            String newData = event.getRfidData().replace(',','\n');
            RfidConfigurationSpace newConfSpace = new RfidConfigurationSpace(newData);
            return rfidObserver.getConclusion(rfidObserver.getPremises(newConfSpace));
        }
        else {
            return null;
        }
    }

    @Override
    public Class getConfigurationSpaceClass() {
        return RfidConfigurationSpace.class;
    }

    @Override
    public String unitName() {
        return "EPC";
    }
    
}
