package org.mdyk.netsim.logic.network;


import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.device.SensorNode;

import java.util.List;

public interface WirelessChannel<P extends Position> {

    /**
     * Metoda która umożliwia węzłom sieci skanowanie w poszukiwaniu sąsiadów.
     * Zwracana jest lista węzłów będących w sąsiedztwie węzła wysyłającego żądanie.
     * @return
     *      list of neighbors.
     */
    public List<SensorNode<P>> scanForNeighbors(SensorNode<P> requestedSensorNode);

}
