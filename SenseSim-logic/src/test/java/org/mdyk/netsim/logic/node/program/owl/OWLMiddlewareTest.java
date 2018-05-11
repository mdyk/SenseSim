package org.mdyk.netsim.logic.node.program.owl;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.util.GeoPosition;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by michal on 17.03.2018.
 */
public class OWLMiddlewareTest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        injector = null;
    }

    @Test
    public void testParseSpatialLocation() throws Exception {
        OWLMiddleware middleware = new OWLMiddleware();

        String spatialLocation = "(52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554#21.007243);(52.23079#21.007115)";

        List<GeoPosition> points = middleware.parseLatLonLocation(spatialLocation);



    }

}