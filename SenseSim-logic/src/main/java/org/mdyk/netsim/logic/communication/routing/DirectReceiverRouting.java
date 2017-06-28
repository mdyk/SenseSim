package org.mdyk.netsim.logic.communication.routing;

import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.logic.node.statistics.DeviceStatistics;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.device.DeviceNode;

import java.util.ArrayList;
import java.util.List;



public class DirectReceiverRouting implements RoutingAlgorithm<GeoPosition> {


//    private DeviceStatistics statistics;
//
//    public DirectReceiverRouting(DeviceStatistics statistics) {
//        this.statistics = statistics;
//    }

    @Override
    public List<DeviceNode<GeoPosition>> getNodesToHop(int sender, int destination, Message message, List<DeviceNode<GeoPosition>> knownSensors) {

        List<DeviceNode<GeoPosition>> sensorsToHop = new ArrayList<>();

        for (DeviceNode deviceNode : knownSensors) {
            if(deviceNode.getID() == destination) {
                sensorsToHop.add(deviceNode);
            }
        }

        return sensorsToHop;
    }


}
