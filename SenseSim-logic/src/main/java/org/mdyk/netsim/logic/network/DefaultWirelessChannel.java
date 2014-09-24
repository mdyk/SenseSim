package org.mdyk.netsim.logic.network;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.node.SensorNode;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Default implementation of wireless channel
 */
@Singleton
public class DefaultWirelessChannel implements WirelessChannel {

    private static Logger logger = Logger.getLogger(DefaultWirelessChannel.class);

    @Inject
    private NetworkManager networkManager;

    @Override
    public List<SensorNode> scanForNeighbors(SensorNode requestedSensorNode) {
        logger.trace(">>> scanForNeighbors [request sensor: " + requestedSensorNode.getID() + "]");
        List<SensorNode> nodesList = networkManager.getNeighborhood(requestedSensorNode);
        logger.trace("<<< scanForNeighbors");
        return nodesList;
    }

}
