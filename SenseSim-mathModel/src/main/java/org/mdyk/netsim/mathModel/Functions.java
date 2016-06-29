package org.mdyk.netsim.mathModel;


import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.logic.util.SenseSimConstans;

import java.util.List;

public class Functions {

    public static double calculateDistance(Position a , Position b) {
        double result;
        // TODO: w miarę możliwości do zmiany
        if (a instanceof GeoPosition && b instanceof GeoPosition){
            result = calculateGeoDistance((GeoPosition)a, (GeoPosition)b);
        }
        else {
            result = Math.sqrt(Math.pow( a.getPositionX() - b.getPositionX() ,2.0 )
                + Math.pow( a.getPositionY() - b.getPositionY() ,2.0 ));
        }
        return result;
    }

    /**
     * Calculates new position of a node based on current position, bearing and distance to travel (in meters)
     * @param currentPosition
     *      current position of a node
     * @param bearing
     *      bearing
     * @param distance
     *      distance to travel in meters
     * @return
     *      new position wrapped in GeoPosition instance.
     */
    public static GeoPosition calculateGeoPosition(GeoPosition currentPosition, double bearing, double distance) {
        double startLat = Math.toRadians(currentPosition.getLatitude());
        double startLong = Math.toRadians(currentPosition.getLongitude());
        bearing = Math.toRadians(bearing);

        double newLat = Math.asin( Math.sin(startLat)*Math.cos(distance/SenseSimConstans.EARTH_RADIUS) +
                Math.cos(startLat)*Math.sin(distance/ SenseSimConstans.EARTH_RADIUS)*Math.cos(bearing) );
        double newLon = startLong + Math.atan2(Math.sin(bearing)*Math.sin(distance/SenseSimConstans.EARTH_RADIUS)*Math.cos(startLat),
                Math.cos(distance/SenseSimConstans.EARTH_RADIUS)-Math.sin(startLat)*Math.sin(newLat));

        return new GeoPosition(Math.toDegrees(newLat), Math.toDegrees(newLon));
    }

    /**
     * Calculates distance in meters between two given geo positions.
     * @param position1
     *      first position
     * @param position2
     *      second position
     * @return
     *      distance in meters between given positions
     */
    public static double calculateGeoDistance(GeoPosition position1, GeoPosition position2) {
        double aStartLat = Math.toRadians(position1.getLatitude());
        double aStartLong = Math.toRadians(position1.getLongitude());
        double aEndLat =Math.toRadians(position2.getLatitude());
        double aEndLong = Math.toRadians(position2.getLongitude());

        double distance = Math.acos(Math.sin(aStartLat) * Math.sin(aEndLat)
                + Math.cos(aStartLat) * Math.cos(aEndLat)
                * Math.cos(aEndLong - aStartLong));

        return SenseSimConstans.EARTH_RADIUS * distance;
    }

    /**
     * Calculates bearing (in degrees) between two points described by GeoPosition
     * @param position1
     *      position of start point
     * @param position2
     *      position of end point
     * @return
     *      bearing, in degrees, between start and end points.
     */
    public static double calculateBearingWithTwoPoints(GeoPosition position1, GeoPosition position2) {
        double dLon = Math.toRadians(position2.getLongitude()) - Math.toRadians(position1.getLongitude());
        double latitude1 = Math.toRadians(position1.getLatitude());
        double latitude2 = Math.toRadians(position2.getLatitude());

        double y= Math.sin(dLon)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2) - Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(dLon);
        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }

    /**
     * Checks if given point is in region described by list of geo positions.
     * @param pointPosition
     *
     * @param regionVertex
     * @return
     *      true if point is in region, false otherwise
     */
    public static boolean isPointInRegion(GeoPosition pointPosition, List<GeoPosition> regionVertex) {
        int i, j;
        boolean isInside = false;

        if(regionVertex.size() == 1) {
            isInside = (pointPosition.getLatitude() == regionVertex.get(0).getLatitude() && pointPosition.getLongitude() == regionVertex.get(0).getLongitude());
        } else {

            //create an array of coordinates from the region boundary list
            GeoPosition[] verts = regionVertex.toArray(new GeoPosition[regionVertex.size()]);
            int sides = verts.length;
            for (i = 0, j = sides - 1; i < sides; j = i++) {
                //verifying if your coordinate is inside your region
                if (
                        (
                                (
                                        (verts[i].getLongitude() <= pointPosition.getLongitude()) && (pointPosition.getLongitude() < verts[j].getLongitude())
                                ) || (
                                        (verts[j].getLongitude() <= pointPosition.getLongitude()) && (pointPosition.getLongitude() < verts[i].getLongitude())
                                )
                        ) &&
                                (pointPosition.getLatitude() < (verts[j].getLatitude() - verts[i].getLatitude()) * (pointPosition.getLongitude() - verts[i].getLongitude()) / (verts[j].getLongitude() - verts[i].getLongitude()) + verts[i].getLatitude())
                        ) {
                    isInside = !isInside;
                }
            }
        }
        return isInside;
    }

}
