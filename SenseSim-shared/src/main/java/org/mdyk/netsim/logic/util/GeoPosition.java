package org.mdyk.netsim.logic.util;

/**
 * Represents node's geo position
 */
public class GeoPosition extends Position {

    public GeoPosition(GeoPosition geoPosition) {
        super(geoPosition.getLatitude(), geoPosition.getLongitude(), 0);
    }

    /**
     * Constructor for node geo position.
     * @param latitude
     *      node's latitude. Should be passed in degrees.
     * @param longitude
     *      node's longitude. Should be passed in degrees.
     */
    public GeoPosition(double latitude, double longitude) {
        super(latitude, longitude, 0);
    }

    /**
     * Return node's latitude
     * @param latitude
     */
    public void setLatitude(double latitude){
        this.positionX = latitude;
    }

    /**
     * Returns node's longitude
     * @param longitude
     */
    public void setLongitude(double longitude){
        this.positionY = longitude;
    }

    public double getLatitude() {
        return this.positionX;
    }

    public double getLongitude() {
        return this.positionY;
    }

    public void setPositionX(double positionX) {
        this.setLatitude(positionX);
    }

    public void setPositionY(double positionY) {
        this.setLongitude(positionY);
    }

    public void setPositionZ(double positionZ) {
        this.positionZ = 0.0;
    }

    public double getPositionX() {
        return getLatitude();
    }

    public double getPositionY() {
        return getLongitude();
    }

    public double getPositionZ() {
        return 0.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append("Latitude: ").append(getLatitude());
        sb.append(" ; ");
        sb.append("Longitude: ").append(getLongitude());
        sb.append("]");
        return sb.toString();
    }


}
