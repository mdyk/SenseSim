package org.mdyk.netsim.logic.communication;


import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.sensor.SensorNode;

import java.util.List;


/**
 * Interface for sensors' routing algorithms
 */
// TODO kryteria dla algorytmu trasowania
public interface RoutingAlgorithm<P extends Position> {

    /**
     * Returns sensors which should receive message in the next hop
     * @param sender
     *      reference to a sending sensor
     * @param destination
     *      destination sensor
     * @param message
     *      message to be sent
     * @param knownSensors
     *      sensors which are known by sender
     * @return
     *      list of sensors which are destination for the hop.
     */
    List<SensorNode<P>> getNodesToHop(int sender, int destination, Message message, List<SensorNode<P>> knownSensors);


}
