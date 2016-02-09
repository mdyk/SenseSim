package org.mdyk.netsim.mathModel.network;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.sensor.DefaultSensorModel;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

import java.util.ArrayList;
import java.util.List;


public class NetworkGraphTest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {}
        });
    }

    @After
    public void tearDown() throws Exception {
        injector = null;
    }

    @Test
    public void testListNeighbors() throws Exception {
        NetworkGraph networkGraph = injector.getInstance(NetworkGraph.class);

        ISensorModel a = new TestSensor(1);
        ISensorModel b = new TestSensor(2);
        ISensorModel c = new TestSensor(3);
        ISensorModel d = new TestSensor(4);

        networkGraph.addEdge(a,b);
        networkGraph.addEdge(a,c);
        networkGraph.addEdge(b,d);

        List<ISensorModel> neighbors = networkGraph.listNeighbors(a);
        TestCase.assertEquals(2 , neighbors.size());
        TestCase.assertTrue(neighbors.contains(b));
        TestCase.assertTrue(neighbors.contains(c));
        TestCase.assertTrue(!neighbors.contains(a));

        neighbors = networkGraph.listNeighbors(b);
        TestCase.assertEquals(2 , neighbors.size());
        TestCase.assertTrue(neighbors.contains(a));
        TestCase.assertTrue(neighbors.contains(d));
        TestCase.assertTrue(!neighbors.contains(c));
        TestCase.assertTrue(!neighbors.contains(b));


        neighbors = networkGraph.listNeighbors(d);
        TestCase.assertEquals(1 , neighbors.size());
        TestCase.assertTrue(neighbors.contains(b));
    }

    @Test
    public void addEdgeTest() throws Exception {
        NetworkGraph ng = new NetworkGraph();

        ISensorModel s1 = new TestSensorModel(1);
        ISensorModel s2 = new TestSensorModel(2);
        ISensorModel s3 = new TestSensorModel(3);

        ng.addEdge(s1,s2);

        TestCase.assertTrue(ng.hasEdge(s1,s2));
        TestCase.assertTrue(ng.hasEdge(s2,s1));
        TestCase.assertFalse(ng.hasEdge(s1,s3));
        TestCase.assertFalse(ng.hasEdge(s3,s1));

    }

    @Test
    public void removeEdgeTest() throws Exception {
        NetworkGraph ng = new NetworkGraph();

        ISensorModel s1 = new TestSensorModel(1);
        ISensorModel s2 = new TestSensorModel(2);

        ng.addEdge(s1,s2);

        TestCase.assertTrue(ng.hasEdge(s1,s2));
        TestCase.assertTrue(ng.hasEdge(s2,s1));

        ng.removeEdge(s1,s2);
        TestCase.assertFalse(ng.hasEdge(s2,s1));

    }

    public class TestSensor extends DefaultSensorModel {

        protected TestSensor(int id) {
            super(id, null, 0, 0, 0, new ArrayList<AbilityType>());
        }

        @Override
        public void sense() {}

        @Override
        protected void onMessage(double time, Message message) {
            // unused
        }
    }
}
