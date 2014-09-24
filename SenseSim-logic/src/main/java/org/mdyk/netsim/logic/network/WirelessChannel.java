package org.mdyk.netsim.logic.network;


import org.mdyk.netsim.logic.node.SensorNode;

import java.util.List;

public interface WirelessChannel {

    /**
     * Metoda która umożliwia węzłom sieci skanowanie w poszukiwaniu sąsiadów.
     * Zwracana jest lista węzłów będących w sąsiedztwie węzła wysyłającego żądanie.
     * @return
     */
    public List<SensorNode> scanForNeighbors(SensorNode requestedSensorNode);

}
