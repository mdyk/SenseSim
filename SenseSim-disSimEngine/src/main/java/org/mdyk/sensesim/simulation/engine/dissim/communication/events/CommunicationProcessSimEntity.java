package org.mdyk.sensesim.simulation.engine.dissim.communication.events;

import dissim.simspace.core.BasicSimEntity;
import dissim.simspace.core.SimControlException;
import dissim.simspace.core.SimModel;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.process.CommunicationProcess;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.communication.process.DefaultCommunicationProcess;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.mathModel.device.IDeviceModel;

/**
 * DeviceSimEntity which represents state of communication process
 */
public class CommunicationProcessSimEntity extends BasicSimEntity implements CommunicationProcess {

    private static final Logger LOG = Logger.getLogger(CommunicationProcessSimEntity.class);

    protected DefaultCommunicationProcess commProcess;
    protected WirelessChannel wirelessChannel;
    protected StartCommunicationActivity startCommunicationActivity;
    protected EndCommunicationActivity endCommunicationActivity;

    protected CommunicationActivity communicationActivity;


    public CommunicationProcessSimEntity(int id, IDeviceModel<?> sender, IDeviceModel<?> receiver, int communicationInterfaceId, double startTime, Message message, WirelessChannel wirelessChannel) {
        super(SimModel.getInstance().getCommonSimContext());
        this.wirelessChannel = wirelessChannel;
        commProcess = new DefaultCommunicationProcess(id,sender,receiver, communicationInterfaceId,startTime,message);
        try {
//            startCommunicationActivity = new StartCommunicationActivity(this);
            communicationActivity = new CommunicationActivity(this , 0.1);
        } catch (SimControlException e) {
            LOG.error(e.getMessage(), e);
        }
    }


    @Override
    public int getID() {
        return commProcess.getID();
    }

    @Override
    public CommunicationStatus getCommunicationStatus(double time) {
        return commProcess.getCommunicationStatus(time);
    }

    @Override
    public CommunicationStatus getCommunicationStatus() {
        return commProcess.getCommunicationStatus();
    }

    @Override
    public double getStartTime() {
        return commProcess.getStartTime();
    }

    @Override
    public double getEndTime() {
        return commProcess.getEndTime();
    }

    @Override
    public double getETA() {
        return commProcess.getETA();
    }

    @Override
    public Message getMessage() {
        return commProcess.getMessage();
    }

    @Override
    public void bitsSent(int bits) {
        commProcess.bitsSent(bits);
    }

    @Override
    public void addBitsSent(int bits) {
        commProcess.addBitsSent(bits);
    }

    @Override
    public void processInterrupted() {
        commProcess.processInterrupted();
    }

    @Override
    public IDeviceModel<?> getSender() {
        return commProcess.getSender();
    }

    @Override
    public IDeviceModel<?> getReceiver() {
        return commProcess.getReceiver();
    }



    @Override
    public int getCommunicationInterfaceId() {
        return commProcess.getCommunicationInterfaceId();
    }
}
