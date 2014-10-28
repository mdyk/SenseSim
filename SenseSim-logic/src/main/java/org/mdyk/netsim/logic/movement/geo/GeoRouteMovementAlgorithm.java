package org.mdyk.netsim.logic.movement.geo;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.Functions;

import java.util.List;

/**
 * Algorytm ruchu po lini z uwzględnieniem współrzędnych geograficznych.
 */
public class GeoRouteMovementAlgorithm implements GeoMovementAlgorithm {

    private static final Logger LOG = Logger.getLogger(GeoRouteMovementAlgorithm.class);

    private List<GeoPosition> route;
    private int nearestCheckpointIdx = 0;
    private double bearing = Double.NaN;

    public GeoPosition nextPositionToCheckpoint(GeoPosition currentPosition, double speed) {
        LOG.trace(String.format(">> nextPositionToCheckpoint(currentPosition: %s, speed %s)", currentPosition, speed));

        if(checkpointAchieved(currentPosition, speed)){
            LOG.debug("checkpointAchieved: " + nearestCheckpointIdx);
            switchToNextCheckpoint();
            calculateBearing(currentPosition, getNextCheckpoint());
        }

        GeoPosition newPosition = nextPositionToCheckpoint(currentPosition, getNextCheckpoint(), speed);
        LOG.trace("<< nextPositionToCheckpoint");
        return newPosition;
    }

    private boolean checkpointAchieved(GeoPosition currentPosition, double speed) {
        double distance = Functions.calculateGeoDistance(currentPosition, getNextCheckpoint());
        LOG.debug("distance to checkpoint: " + distance);
        return distance <= speed;
    }

    private void switchToNextCheckpoint() {
        if(nearestCheckpointIdx < (route.size() - 1)){
            nearestCheckpointIdx ++ ;
        }
        else{
            nearestCheckpointIdx = 0;
        }
    }

    private void calculateBearing(GeoPosition currentPosition, GeoPosition nextCheckpoint) {
        LOG.debug(String.format(">> calculateBearing(currentPosition: %s, nextCheckpoint: %s)", currentPosition, nextCheckpoint));
        this.bearing = Functions.calculateBearingWithTwoPoints(currentPosition,nextCheckpoint);
        LOG.debug("new bearing: " + bearing);
        LOG.debug("<< calculateBearing");
    }

    @Override
    public GeoPosition nextPositionToCheckpoint(GeoPosition currentPosition, GeoPosition nextCheckpoint, double speed) {

        LOG.trace(String.format(">> nextPositionToCheckpoint(currentPosition: %s, nextCheckpoint %s, speed %s)",
                currentPosition, nextCheckpoint, speed));
        GeoPosition newPosition = Functions.calculateGeoPosition(currentPosition,bearing,speed);
        LOG.debug("newPosition: " + newPosition);
        LOG.trace("<< nextPositionToCheckpoint");
        return newPosition;
    }


    @Override
    public void setRoute(List<GeoPosition> route) {
        this.route = route;
    }

    public GeoPosition getNextCheckpoint() {
        return route.get(nearestCheckpointIdx);
    }
}
