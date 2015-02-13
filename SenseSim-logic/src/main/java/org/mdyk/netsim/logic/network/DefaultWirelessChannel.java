package org.mdyk.netsim.logic.network;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.sensor.SensorNode;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Default implementation of wireless channel
 */
@Singleton
public class DefaultWirelessChannel implements WirelessChannel<GeoPosition> {

    private static final Logger LOG = Logger.getLogger(DefaultWirelessChannel.class);

    @Inject
    private NetworkManager networkManager;

    @Override
    public List<SensorNode<GeoPosition>> scanForNeighbors(SensorNode<GeoPosition> requestedSensorNode) {
        LOG.trace(">>> scanForNeighbors [request sensor: " + requestedSensorNode.getID() + "]");
        List<SensorNode<GeoPosition>> nodesList = networkManager.getNeighborhood(requestedSensorNode);
        LOG.trace("<<< scanForNeighbors");
        return nodesList;
    }

}
