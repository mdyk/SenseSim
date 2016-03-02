package org.mdyk.netsim.logic.communication;


import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.device.DeviceNode;

import java.util.List;


/**
 * Interface for sensors' routing algorithms
 */
// TODO kryteria dla algorytmu trasowania
public interface RoutingAlgorithm<P extends Position> {

    /**
     * Returns sensors which should receive message in the next hop
     * @param sender
     *      reference to a sending device
     * @param destination
     *      destination device
     * @param message
     *      message to be sent
     * @param knownSensors
     *      sensors which are known by sender
     * @return
     *      list of sensors which are destination for the hop.
     */
    List<DeviceNode<P>> getNodesToHop(int sender, int destination, Message message, List<DeviceNode<P>> knownSensors);


}
