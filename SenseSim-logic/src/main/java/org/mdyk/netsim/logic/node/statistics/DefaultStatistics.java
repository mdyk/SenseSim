package org.mdyk.netsim.logic.node.statistics;


import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.process.CommunicationProcess;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.node.Device;
import org.mdyk.netsim.logic.node.program.SensorProgram;
import org.mdyk.netsim.logic.node.statistics.event.DeviceStatisticsEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultStatistics implements DeviceStatistics {

    private static final Logger LOG = Logger.getLogger(DefaultStatistics.class);

    private Device device;
    List<CommunicationProcess> incomingComms;
    List<CommunicationProcess> outgoingComms;
    List<SensorProgram> sensorPrograms;

    public DefaultStatistics() {
        this.incomingComms = new ArrayList<>();
        this.outgoingComms = new ArrayList<>();
        this.sensorPrograms = new ArrayList<>();
        EventBusHolder.getEventBus().register(this);
    }

    @Override
    public void addCommunication(CommunicationProcess communicationProcess) {
        LOG.trace(">> addCommunication");
        if(communicationProcess.getSender().getID() == device.getDeviceLogic().getID()) {
            LOG.debug("adding process " + communicationProcess.getID() + " as outgoing");
            addCommunication(communicationProcess, outgoingComms);
        } else if (communicationProcess.getReceiver().getID() == device.getDeviceLogic().getID()) {
            LOG.debug("adding process " + communicationProcess.getID() + " as incoming");
            addCommunication(communicationProcess, incomingComms);
        }
        LOG.trace("<< addCommunication");
    }

    private void addCommunication(CommunicationProcess newCommunicationProcess, List<CommunicationProcess> commList) {
        boolean newProcess = true;
        for(CommunicationProcess process : commList) {
            if (process.getID() == newCommunicationProcess.getID()) {
                newProcess = false;
            }
        }
        if(newProcess) {
            commList.add(newCommunicationProcess);
        }
    }

    @Override
    public List<CommunicationProcess> getIncomingCommunication() {
        return this.incomingComms;
    }

    @Override
    public List<CommunicationProcess> getOutgoingCommunication() {
        return this.outgoingComms;
    }

    @Override
    public void addProgram(HashMap<Integer , SensorProgram>  sensorProgramMap) {
        LOG.trace(">> addProgram");
        if(sensorProgramMap.containsKey(this.getSensorId())) {
            this.sensorPrograms.add(sensorProgramMap.get(this.getSensorId()));
        }
        LOG.trace("<< addProgram");
    }


    @Override
    public List<SensorProgram> getSensorPrograms() {
        return this.sensorPrograms;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public int getSensorId() {
        return this.device.getDeviceLogic().getID();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleEvents(DeviceStatisticsEvent event){
        try {
            switch (event.getEventType()) {
                case COMM_PROC_UPDATE:
                    CommunicationProcess communicationProcess = (CommunicationProcess) event.getPayload();
                    addCommunication(communicationProcess);
                    EventBusHolder.getEventBus().post(new DeviceStatisticsEvent(DeviceStatisticsEvent.EventType.GUI_UPDATE_STATISTICS, this));
                    break;

                case PROGRAM_LOADED:
                    HashMap<Integer , SensorProgram>  sensorProgramMap = (HashMap<Integer, SensorProgram>) event.getPayload();
                    addProgram(sensorProgramMap);
                    EventBusHolder.getEventBus().post(new DeviceStatisticsEvent(DeviceStatisticsEvent.EventType.GUI_UPDATE_STATISTICS, this));
                    break;

                case PROGRAM_UPDATED:
                    EventBusHolder.getEventBus().post(new DeviceStatisticsEvent(DeviceStatisticsEvent.EventType.GUI_UPDATE_STATISTICS, this));
                    break;
            }

        } catch (Exception exc) {
            LOG.error(exc.getMessage() , exc);
        }
    }

}
