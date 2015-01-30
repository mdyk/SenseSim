package org.mdyk.netsim.logic.network;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.message.Message;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.node.SensorNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.sensor.DefaultSensorModel;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

import java.util.LinkedList;
import java.util.List;


public class NetworkManagerTest {

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
    public void testGetNeighborhood() throws Exception {
        NetworkManager networkManager = injector.getInstance(NetworkManager.class);

        TestSensorNode node1 = new TestSensorNode(1, new GeoPosition(52.231594, 21.003547));
        networkManager.addNode(node1);

        TestSensorNode node2 = new TestSensorNode(2, new GeoPosition(52.231594, 21.003547));
        networkManager.addNode(node2);

        TestSensorNode node3 = new TestSensorNode(3, new GeoPosition(30.230786, 21.005350));
        networkManager.addNode(node3);

        networkManager.actualizeNaighbours(node1);
        networkManager.actualizeNaighbours(node2);
        networkManager.actualizeNaighbours(node3);

        List<SensorNode> sensorNodes =  networkManager.getNeighborhood(node2);
        TestCase.assertEquals(1 , sensorNodes.size());
        TestCase.assertEquals(1 , sensorNodes.get(0).getID());

        List<SensorNode> sensorNodes2 =  networkManager.getNeighborhood(node1);
        TestCase.assertEquals(1 , sensorNodes2.size());
        TestCase.assertEquals(2 , sensorNodes2.get(0).getID());

        List<SensorNode> sensorNodes3 =  networkManager.getNeighborhood(node3);
        TestCase.assertEquals(0 , sensorNodes3.size());

    }

    public class TestSensorNode extends DefaultSensorModel implements SensorNode {


        protected TestSensorNode(int id, Position position) {
            super(id, position, 90, 1, new LinkedList<AbilityType>());
        }

        @Override
        public void sense() {}

        @Override
        public void startNode() {}

        @Override
        public void stopNode() {}

        @Override
        public void pauseNode() {}

        @Override
        public void resumeNode() {}

        @Override
        public void work() {}

        @Override
        public void move() {}

        @Override
        public void startCommunication(Message message, ISensorModel... receivers) {
        }

        @Override
        public CommunicationStatus getCommunicationStatus() {return null;}
    }
}
