package org.mdyk.netsim.logic.communication.routing;

import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.sensor.SensorNode;

import java.util.*;


/**
 * Implementation of flooding routing algorithm.
 */
public class FloodingRouting implements RoutingAlgorithm<GeoPosition> {

    private Map<Integer , List<SensorNode<GeoPosition>>> sentMessage;

    public FloodingRouting() {
        sentMessage = new HashMap<>();
    }

    @Override
    public List<SensorNode<GeoPosition>> getNodesToHop(int sender, int destination, Message<?> message, List<SensorNode<GeoPosition>> knownSensors) {
        List<SensorNode<GeoPosition>> sensorsToHop = new ArrayList<>(knownSensors);

        boolean foundSender = false;
        for(int i = 0 ; i < sensorsToHop.size() && !foundSender ; i++) {
            if(sensorsToHop.get(i).getID() == message.getID()) {
                foundSender = true;
                sensorsToHop.remove(i);
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
