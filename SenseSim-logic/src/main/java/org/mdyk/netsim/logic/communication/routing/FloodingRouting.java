package org.mdyk.netsim.logic.communication.routing;

import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.logic.communication.process.CommunicationProcess;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.node.statistics.DeviceStatistics;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.device.DeviceNode;

import java.util.*;


/**
 * Implementation of flooding routing algorithm.
 */
public class FloodingRouting implements RoutingAlgorithm<GeoPosition> {

    private Map<Long , List<DeviceNode<GeoPosition>>> sentMessage;

    private DeviceStatistics statistics;

    public FloodingRouting(DeviceStatistics sensor) {
        sentMessage = new HashMap<>();
        this.statistics = sensor;
    }

    @Override
    public List<DeviceNode<GeoPosition>> getNodesToHop(int sender, int destination, Message message, List<DeviceNode<GeoPosition>> knownSensors) {
        List<DeviceNode<GeoPosition>> sensorsToHop = new ArrayList<>(knownSensors);

        for(CommunicationProcess comm : statistics.getIncomingCommunication()) {
            DeviceNode<GeoPosition> sensor = (DeviceNode<GeoPosition>) comm.getSender();
            Message sentMessage = comm.getMessage();

            if(sentMessage.getID() == message.getID() && comm.getCommunicationStatus().equals(CommunicationStatus.SUCCESS)) {
                sensorsToHop.remove(sensor);
            }
        }

        // Filtering nodes to which the message was already sent.
        if(sentMessage.containsKey(message.getID())) {
            sensorsToHop.removeAll(sentMessage.get(message.getID()));
            sentMessage.get(message.getID()).addAll(sensorsToHop);
        } else {
            sentMessage.put(message.getID(), sensorsToHop);
        }

        return sensorsToHop;
    }

}
