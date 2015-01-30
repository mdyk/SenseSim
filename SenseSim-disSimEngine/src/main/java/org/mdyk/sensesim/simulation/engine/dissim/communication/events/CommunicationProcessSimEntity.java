package org.mdyk.sensesim.simulation.engine.dissim.communication.events;

import dissim.broker.IEvent;
import dissim.broker.IEventPublisher;
import dissim.simspace.BasicSimEntity;
import dissim.simspace.SimControlException;
import dissim.simspace.SimModel;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.process.CommunicationProcess;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.communication.process.DefaultCommunicationProcess;
import org.mdyk.netsim.logic.communication.message.Message;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

/**
 * SimEntity which represents state of communication process
 */
public class CommunicationProcessSimEntity extends BasicSimEntity implements CommunicationProcess {

    private static final Logger LOG = Logger.getLogger(CommunicationProcessSimEntity.class);

    protected DefaultCommunicationProcess commProcess;
    protected WirelessChannel wirelessChannel;
    protected StartCommunicationActivity startCommunicationActivity;
    protected EndCommunicationActivity endCommunicationActivity;


    public CommunicationProcessSimEntity(int id, ISensorModel<?> sender, ISensorModel<?> receiver, double startTime, Message message, WirelessChannel wirelessChannel) {
        super(SimModel.getInstance().getCommonSimContext());
        this.wirelessChannel = wirelessChannel;
        commProcess = new DefaultCommunicationProcess(id,sender,receiver,startTime,message);
        try {
            startCommunicationActivity = new StartCommunicationActivity(this);
        } catch (SimControlException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void reflect(IEvent event, IEventPublisher publisher) {
        // Empty
    }

    @Override
    public void reflect(IEvent event) {
        // Empty
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
    public double getStartTime() {
        return commProcess.getStartTime();
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
}
