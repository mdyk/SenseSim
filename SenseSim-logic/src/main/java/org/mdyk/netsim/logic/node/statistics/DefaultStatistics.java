package org.mdyk.netsim.logic.node.statistics;


import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.process.CommunicationProcess;
import org.mdyk.netsim.logic.node.Sensor;

import java.util.ArrayList;
import java.util.List;

public class DefaultStatistics implements SensorStatistics {

    private static final Logger LOG = Logger.getLogger(DefaultStatistics.class);

    private Sensor sensor;
    List<CommunicationProcess> incomingComms;
    List<CommunicationProcess> outgoingComms;


    public DefaultStatistics() {
        this.incomingComms = new ArrayList<>();
        this.outgoingComms = new ArrayList<>();
    }

    @Override
    public void addCommunication(CommunicationProcess communicationProcess) {
        LOG.trace(">> addCommunication");
        if(communicationProcess.getMessage().getMessageSource() == sensor.getSensorLogic().getID()) {
            LOG.debug("adding process " + communicationProcess.getID() + " as outgoing");
            this.outgoingComms.add(communicationProcess);
        } else if (communicationProcess.getMessage().getMessageDest() == sensor.getSensorLogic().getID()) {
            LOG.debug("adding process " + communicationProcess.getID() + " as incoming");
            this.incomingComms.add(communicationProcess);
        }
        LOG.trace("<< addCommunication");
    }

    @Override
    public List<CommunicationProcess> getIncomingCommunication() {
        return this.incomingComms;
    }

    @Override
    public List<CommunicationProcess> getOutgoingCommunication() {
        return this.outgoingComms;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
