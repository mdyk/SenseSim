package org.mdyk.netsim.logic.communication.routing;

import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.communication.Message;
import org.mdyk.netsim.mathModel.communication.RoutingAlgorithm;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;
import org.mdyk.netsim.mathModel.sensor.SensorNode;

import java.util.List;


/**
 * Implementation of flooding routing algorithm.
 */
public class FloodingRouting implements RoutingAlgorithm<GeoPosition> {

    @Override
    public List<SensorNode<GeoPosition>> getNodesToHop(SensorNode<GeoPosition> sender, SensorNode<GeoPosition> destination, Message<?> message, List<SensorNode<GeoPosition>> knownSensors) {
        return knownSensors;
    }

}
