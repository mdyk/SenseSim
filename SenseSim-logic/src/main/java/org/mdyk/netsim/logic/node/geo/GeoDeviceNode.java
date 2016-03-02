package org.mdyk.netsim.logic.node.geo;

import org.mdyk.netsim.mathModel.device.DeviceNode;
import org.mdyk.netsim.logic.util.GeoPosition;

/**
 * Interface for nodes with geo positioning
 */
public interface GeoDeviceNode extends DeviceNode<GeoPosition> {

    public void setLatitude(double latitude);

    public double getLatitude();

    public void setLongitude(double longitude);

    public double getLongitude();

}
