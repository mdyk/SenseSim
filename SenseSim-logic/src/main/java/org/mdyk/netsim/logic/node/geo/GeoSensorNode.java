package org.mdyk.netsim.logic.node.geo;

import org.mdyk.netsim.logic.node.SensorNode;
import org.mdyk.netsim.logic.util.GeoPosition;

/**
 * Interface for nodes with geo positioning
 */
public interface GeoSensorNode extends SensorNode<GeoPosition> {

    public void setLatitude(double latitude);

    public double getLatitude();

    public void setLongitude(double longitude);

    public double getLongitude();

}
