package org.mdyk.netsim.logic.util;

/**
 * Reprezentuje pozycję węzła
 */
public class Position {

    protected double positionX;
    protected double positionY;
    protected double positionZ;

    public Position(double positionX, double positionY, double positionZ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public void setPositionZ(double positionZ) {
        this.positionZ = positionZ;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getPositionZ() {
        return positionZ;
    }
}
