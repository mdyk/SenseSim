package org.mdyk.netsim.logic.network;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.device.DeviceNode;

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
    public List<DeviceNode<GeoPosition>> scanForNeighbors(DeviceNode<GeoPosition> requestedSensorNode) {
        LOG.trace(">>> scanForNeighbors [request device: " + requestedSensorNode.getID() + "]");
        List<DeviceNode<GeoPosition>> nodesList = networkManager.getNeighborhood(requestedSensorNode);
        LOG.trace("<<< scanForNeighbors");
        return nodesList;
    }

}
