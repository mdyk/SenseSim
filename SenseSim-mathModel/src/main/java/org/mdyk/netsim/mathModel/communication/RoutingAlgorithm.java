package org.mdyk.netsim.mathModel.communication;


import org.mdyk.netsim.mathModel.sensor.ISensorModel;

import java.util.List;


/**
 * Interface for sensors' routing algorithms
 */
// TODO kryteria dla algorytmu trasowania
public interface RoutingAlgorithm {

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
    public List<ISensorModel<?>> getNodesToHop(ISensorModel<?> sender, ISensorModel<?> destination, Message<?> message, List<ISensorModel<?>> knownSensors);

}
