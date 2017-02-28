package org.mdyk.netsim.logic.network;


import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.device.DeviceNode;

import java.util.List;

public interface WirelessChannel<P extends Position> {

    /**
     * Metoda która umożliwia węzłom sieci skanowanie w poszukiwaniu sąsiadów.
     * Zwracana jest lista węzłów będących w sąsiedztwie węzła wysyłającego żądanie.
     * @return
     *      list of neighbors.
     */
    @Deprecated
    List<DeviceNode<P>> scanForNeighbors(DeviceNode<P> requestedSensorNode);

    List<DeviceNode<P>> scanForNeighbors(int commIntId, DeviceNode<P> requestedSensorNode);

}
