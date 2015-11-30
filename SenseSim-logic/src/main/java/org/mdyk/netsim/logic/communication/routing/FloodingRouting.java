package org.mdyk.netsim.logic.communication.routing;

import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.logic.communication.process.CommunicationProcess;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.node.statistics.SensorStatistics;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.sensor.SensorNode;

import java.util.*;


/**
 * Implementation of flooding routing algorithm.
 */
public class FloodingRouting implements RoutingAlgorithm<GeoPosition> {

    private Map<Integer , List<SensorNode<GeoPosition>>> sentMessage;

    private SensorStatistics statistics;

    public FloodingRouting(SensorStatistics sensor) {
        sentMessage = new HashMap<>();
        this.statistics = sensor;
    }

    // FIXME konieczne jest zablokowanie możliwości wysyłania wiadomości do węzłów od których ją otrzylamiśmy
    @Override
    public List<SensorNode<GeoPosition>> getNodesToHop(int sender, int destination, Message message, List<SensorNode<GeoPosition>> knownSensors) {
        List<SensorNode<GeoPosition>> sensorsToHop = new ArrayList<>(knownSensors);

        for(CommunicationProcess comm : statistics.getIncomingCommunication()) {
            SensorNode<GeoPosition> sensor = (SensorNode<GeoPosition>) comm.getSender();
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
