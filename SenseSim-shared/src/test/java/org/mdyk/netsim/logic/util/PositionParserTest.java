package org.mdyk.netsim.logic.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class PositionParserTest {
    @Test
    public void parsePosition() throws Exception {
        String positionString = "52.231073#21.007506";
        GeoPosition gp = PositionParser.parsePosition(positionString);

        Assert.assertEquals(gp.getLatitude(), 52.231073, 0.0);
        Assert.assertEquals(gp.getLongitude(), 21.007506, 0.0);
    }

    @Test
    public void encodePosition() throws Exception {
        GeoPosition gp = new GeoPosition(52.231073 , 21.007506);
        String positionString = PositionParser.encodePosition(gp);

        Assert.assertEquals("52.231073#21.007506",positionString);
    }

    @Test
    public void parsePositionsList() throws Exception {
        List<GeoPosition> gpList = PositionParser.parsePositionsList("(52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554#21.007243);(52.23079#21.007115)");

        GeoPosition gp1 = new GeoPosition(52.231073 , 21.007506);
        GeoPosition gp2 = new GeoPosition(52.230797 , 21.008139);
        GeoPosition gp3 = new GeoPosition(52.230488 , 21.007726);
        GeoPosition gp4 = new GeoPosition(52.230554 , 21.007243);
        GeoPosition gp5 = new GeoPosition(52.23079 , 21.007115);

        List<GeoPosition> gpListExpected = new ArrayList<>();
        gpListExpected.add(gp1);
        gpListExpected.add(gp2);
        gpListExpected.add(gp3);
        gpListExpected.add(gp4);
        gpListExpected.add(gp5);

        Assert.assertArrayEquals(gpListExpected.toArray(), gpList.toArray());
    }

    @Test
    public void encodePositionsList() throws Exception {
        // (52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554#21.007243);(52.23079#21.007115)
        GeoPosition gp1 = new GeoPosition(52.231073 , 21.007506);
        GeoPosition gp2 = new GeoPosition(52.230797 , 21.008139);
        GeoPosition gp3 = new GeoPosition(52.230488 , 21.007726);
        GeoPosition gp4 = new GeoPosition(52.230554 , 21.007243);
        GeoPosition gp5 = new GeoPosition(52.23079 , 21.007115);

        List<GeoPosition> gpList = new ArrayList<>();
        gpList.add(gp1);
        gpList.add(gp2);
        gpList.add(gp3);
        gpList.add(gp4);
        gpList.add(gp5);

        String encodedPositions = PositionParser.encodePositionsList(gpList);

        Assert.assertEquals("(52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554#21.007243);(52.23079#21.007115)",encodedPositions);

    }

}