package org.mdyk.sensesim.simulation.engine.dissim.communication.events;


import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.node.SensorNode;

import java.util.List;

public class EndCommunicationActivity extends BasicSimStateChange<CommunicationProcessSimEntity , Object> {

    private static final Logger LOG = Logger.getLogger(EndCommunicationActivity.class);
    private SensorNode sender;
    private SensorNode receiver;
    private double delay;

    public EndCommunicationActivity(CommunicationProcessSimEntity entity, double delay) throws SimControlException {
        super(entity, delay);
        sender = (SensorNode) entity.commProcess.getSender();
        receiver = (SensorNode) entity.commProcess.getReceiver();
        this.delay = delay;
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.trace(">> EndCommunicationActivity.transition()");
        // Checking if receiver is still neighbour for sender
        List<SensorNode> neighbours = getSimEntity().wirelessChannel.scanForNeighbors(sender);

        if(neighbours.contains(receiver)) {
            LOG.trace("Receiver is neighbour of a sender");
            double bandwidth = Math.min(sender.getWirelessBandwith() , receiver.getWirelessBandwith());

            int sentBits = (int) Math.floor(bandwidth * delay);
            getSimEntity().addBitsSent(sentBits);

            getSimEntity().startCommunicationActivity = new StartCommunicationActivity(getSimEntity());
        }
        else {
            LOG.trace("Receiver is not neighbour of a sender");
            getSimEntity().processInterrupted();
        }
        LOG.trace("<< EndCommunicationActivity.transition()");
    }

    @Override
    protected void onTermination() throws SimControlException {
        // Unused
    }

    @Override
    protected void onInterruption() throws SimControlException {
        // Unused
    }
}
