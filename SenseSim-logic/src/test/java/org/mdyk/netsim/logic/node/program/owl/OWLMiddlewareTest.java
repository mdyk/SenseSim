package org.mdyk.netsim.logic.node.program.owl;

import org.junit.Test;
import org.mdyk.netsim.logic.util.GeoPosition;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by michal on 17.03.2018.
 */
public class OWLMiddlewareTest {

    @Test
    public void testParseSpatialLocation() throws Exception {
        OWLMiddleware middleware = new OWLMiddleware();

        String spatialLocation = "(52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554#21.007243);(52.23079#21.007115)";

        List<GeoPosition> points = middleware.parseLatLonLocation(spatialLocation);



    }

}