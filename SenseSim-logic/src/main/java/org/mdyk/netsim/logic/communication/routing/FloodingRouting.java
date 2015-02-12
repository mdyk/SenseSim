package org.mdyk.netsim.logic.communication.routing;

import com.google.inject.Singleton;
import org.mdyk.netsim.mathModel.communication.Message;
import org.mdyk.netsim.mathModel.communication.RoutingAlgorithm;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

import java.util.List;


/**
 * Implementation of flooding routing algorithm.
 */
public class FloodingRouting implements RoutingAlgorithm {

    @Override
    public List<ISensorModel<?>> getNodesToHop(ISensorModel<?> sender, ISensorModel<?> destination, Message<?> message, List<ISensorModel<?>> knownSensors) {
        return knownSensors;
    }

}
