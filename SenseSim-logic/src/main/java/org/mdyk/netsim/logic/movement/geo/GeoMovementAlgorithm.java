package org.mdyk.netsim.logic.movement.geo;

import org.mdyk.netsim.logic.movement.MovementAlgorithm;
import org.mdyk.netsim.logic.util.GeoPosition;

import java.util.List;

/**
 * Interface for algorithms describing movement of
 * nodes with geolocalization
 */
public interface GeoMovementAlgorithm extends MovementAlgorithm {

    /**
     * Calculates new position of a node concerning current position,
     * next checkpoint and speed.
     * @param currentPosition
     *      current position of a node.
     * @param nextCheckpoint
     *      next checkpoint on nodes route.
     * @param speed
     *      speed of a node.
     * @return
     */
    GeoPosition nextPositionToCheckpoint(GeoPosition currentPosition, GeoPosition nextCheckpoint, double speed);

    GeoPosition nextPositionToCheckpoint(GeoPosition currentPosition, double speed);

    void setRoute(List<GeoPosition> route);

}