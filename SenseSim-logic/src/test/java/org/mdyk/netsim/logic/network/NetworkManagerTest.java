package org.mdyk.netsim.logic.network;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.device.DeviceNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.device.DefaultDeviceModel;
import org.mdyk.netsim.mathModel.device.connectivity.CommunicationInterface;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

import java.util.ArrayList;
import java.util.HashMap;
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

        CommunicationInterface communicationInterface11 = new CommunicationInterface(1, "Int1", 5000, 5000, 90, CommunicationInterface.TopologyType.ADHOC);
        List<CommunicationInterface> communicationInterfaces1 = new ArrayList<>();
        communicationInterfaces1.add(communicationInterface11);
        TestDeviceNode node1 = new TestDeviceNode(1, new GeoPosition(52.231594, 21.003547) , communicationInterfaces1);
        networkManager.addNode(node1);

        CommunicationInterface communicationInterface21 = new CommunicationInterface(1, "Int1", 5000, 5000, 90, CommunicationInterface.TopologyType.ADHOC);
        List<Integer> fixedNodes2 = new ArrayList<>();
        fixedNodes2.add(3);
        CommunicationInterface communicationInterface22 = new CommunicationInterface(2, "Int2", 5000, 5000, 90, CommunicationInterface.TopologyType.FIXED,fixedNodes2);
        List<CommunicationInterface> communicationInterfaces2 = new ArrayList<>();
        communicationInterfaces2.add(communicationInterface21);
        communicationInterfaces2.add(communicationInterface22);
        TestDeviceNode node2 = new TestDeviceNode(2, new GeoPosition(52.231594, 21.003547), communicationInterfaces2);
        networkManager.addNode(node2);

        CommunicationInterface communicationInterface31 = new CommunicationInterface(1, "Int1", 5000, 5000, 90, CommunicationInterface.TopologyType.ADHOC);
        List<Integer> fixedNodes3 = new ArrayList<>();
        fixedNodes3.add(2);
        CommunicationInterface communicationInterface32 = new CommunicationInterface(2, "Int2", 5000, 5000, 90, CommunicationInterface.TopologyType.FIXED,fixedNodes3);
        List<CommunicationInterface> communicationInterfaces3 = new ArrayList<>();
        communicationInterfaces3.add(communicationInterface31);
        communicationInterfaces3.add(communicationInterface32);
        TestDeviceNode node3 = new TestDeviceNode(3, new GeoPosition(30.230786, 21.005350), communicationInterfaces3);
        networkManager.addNode(node3);

        networkManager.actualizeNaighbours(node1);
        networkManager.actualizeNaighbours(node2);
        networkManager.actualizeNaighbours(node3);

        List<DeviceNode<?>> sensorNodes =  networkManager.getNeighborhood(node2,1);
        TestCase.assertEquals(1 , sensorNodes.size());
        TestCase.assertEquals(1 , sensorNodes.get(0).getID());

        List<DeviceNode<?>> sensorNodes2 =  networkManager.getNeighborhood(node1,1);
        TestCase.assertEquals(1 , sensorNodes2.size());
        TestCase.assertEquals(2 , sensorNodes2.get(0).getID());

        List<DeviceNode<?>> sensorNodes3 =  networkManager.getNeighborhood(node3,1);
        TestCase.assertEquals(0 , sensorNodes3.size());
        sensorNodes3 =  networkManager.getNeighborhood(node3,2);
        TestCase.assertEquals(1 , sensorNodes3.size());
    }

    public class TestDeviceNode extends DefaultDeviceModel implements DeviceNode {


        protected TestDeviceNode(int id, Position position , List<CommunicationInterface> communicationInterfaces) {
            super(id, "test", position, 90, 5000,  1, new LinkedList<>(), new LinkedList<>(), communicationInterfaces);
        }

        @Override
        protected void onMessage(double time, Message message) {
            // unused
        }

        @Override
        protected void onMessage(double time, int communicationInterfaceId, Message message) {
            
        }

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
        public void startCommunication(Message message, DeviceNode... receivers) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void startCommunication(Message message, HashMap receivers) {
            
        }

    }
}
