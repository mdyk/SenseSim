package org.mdyk.netsim.mathModel;

import junit.framework.TestCase;
import org.junit.Test;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.logic.util.Position;

import java.util.LinkedList;
import java.util.List;

public class FunctionsTest {

    @Test
    public void testCalculateDistance() throws Exception {

        Position pos1 = new Position(1,1,0);
        Position pos2 = new Position(2,1,0);

        TestCase.assertEquals(1.0, Functions.calculateDistance(pos1, pos2));

        pos1 = new Position(1,1,0);
        pos2 = new Position(1,1,0);

        TestCase.assertEquals(0.0, Functions.calculateDistance(pos1, pos2));

        pos1 = new Position(2,1,0);
        pos2 = new Position(1,1,0);

        TestCase.assertEquals(1.0, Functions.calculateDistance(pos1, pos2));

        pos1 = new Position(1,1,0);
        pos2 = new Position(2,2,0);

        TestCase.assertEquals(1.0*Math.sqrt(2), Functions.calculateDistance(pos1, pos2));

    }

    @Test
    public void calculateGeoPositionTest(){
        GeoPosition currentPosition = new GeoPosition(43.60411, 1.44411);
        int distanceToMove = 1; //meter
        double bearing = 43.604;

        GeoPosition newPosition = Functions.calculateGeoPosition(currentPosition, bearing, distanceToMove);

        TestCase.assertTrue(Functions.calculateGeoDistance(currentPosition,newPosition) > 0.9d
                            && Functions.calculateGeoDistance(currentPosition,newPosition)< 1.1);
    }

    @Test
    public void calculateGeoDistanceTest() {

        GeoPosition position1 = new GeoPosition(43.60411, 1.44411);
        GeoPosition position2 = new GeoPosition(43.604117, 1.444119);

        double distance = Functions.calculateGeoDistance(position1, position2);

        TestCase.assertEquals(1.062597923115387d,distance);
    }

    @Test
    public void calculateBearingWithTwoPointsTest() {

        GeoPosition position1 = new GeoPosition(43.60411,1.44411);
        GeoPosition position2 = new GeoPosition(43.60458583589277d , 1.4438488331430568);

        double bearing = Functions.calculateBearingWithTwoPoints(position1,position2);

        TestCase.assertEquals(338.3252583519347, bearing);

    }

    @Test
    public void isPointInRegion() {

        List<GeoPosition> region = new LinkedList<>();
        GeoPosition position1 = new GeoPosition(52.231893, 21.003531);
        GeoPosition position2 = new GeoPosition(52.233378, 21.009518);
        GeoPosition position3 = new GeoPosition(52.231157, 21.010913);
        GeoPosition position4 = new GeoPosition(52.229966, 21.004722);

        region.add(position1);
        region.add(position2);
        region.add(position3);
        region.add(position4);

        GeoPosition pointInRegion = new GeoPosition(52.231833, 21.006653);
        TestCase.assertTrue(Functions.isPointInRegion(pointInRegion, region));

        GeoPosition pointOutsideRegion = new GeoPosition(52.233173, 21.013820);
        TestCase.assertTrue(!Functions.isPointInRegion(pointOutsideRegion, region));

    }


    @Test
    public void isPointInRegionWholePoland() {

        List<GeoPosition> region = new LinkedList<>();



        GeoPosition position1 = new GeoPosition(53.721823, 14.323730);
        GeoPosition position2 = new GeoPosition(54.177929, 23.380290);
        GeoPosition position3 = new GeoPosition(49.638199, 23.728027);
        GeoPosition position4 = new GeoPosition(49.295487, 15.378418);
//        GeoPosition position4 = new GeoPosition(52.230554, 21.007243);

        region.add(position1);
        region.add(position2);
        region.add(position3);
        region.add(position4);


        GeoPosition pointInRegion = new GeoPosition(52.230856, 21.006546);
        TestCase.assertTrue(Functions.isPointInRegion(pointInRegion, region));

//        GeoPosition pointOutsideRegion = new GeoPosition(52.233173, 21.013820);
//        TestCase.assertTrue(!Functions.isPointInRegion(pointOutsideRegion, region));

    }


}
