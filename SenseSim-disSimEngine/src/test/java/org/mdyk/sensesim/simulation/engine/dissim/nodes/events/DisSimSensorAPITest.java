package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import dissim.simspace.core.SimModel;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.routing.FloodingRouting;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.network.DefaultWirelessChannel;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.SensorNodeFactory;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.geo.ProgrammableNode;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.sensesim.simulation.engine.dissim.DisSimEngine;
import org.mdyk.sensesim.simulation.engine.dissim.communication.DisSimCommunicationProcessFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimSensorNodeFactory;
import org.mdyk.sensesim.simulation.engine.dissim.phenomena.DisSimPhenomenaFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DisSimSensorAPITest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(CommunicationProcessFactory.class).to(DisSimCommunicationProcessFactory.class);
                bind(WirelessChannel.class).to(DefaultWirelessChannel.class);
                bind(SimEngine.class).to(DisSimEngine.class);
                bind(SensorNodeFactory.class).to(DisSimSensorNodeFactory.class);
                bind(PhenomenaFactory.class).to(DisSimPhenomenaFactory.class);
                bind(CommunicationProcessFactory.class).to(DisSimCommunicationProcessFactory.class);
                install(new FactoryModuleBuilder().build(ScenarioFactory.class));
            }
        });

        Field instance = SimModel.class.getDeclaredField("simModel");
        instance.setAccessible(true);
        instance.set(null, null);

    }

    @Test
    public void api_moveTest() throws InterruptedException {
        SimEngine<DisSimProgrammableNode> simEngine = injector.getInstance(SimEngine.class);
        SensorNodeFactory sensorNodeFactory = injector.getInstance(SensorNodeFactory.class);

        ProgrammableNode sensor = sensorNodeFactory.createGeoSensorNode(1, new GeoPosition(52.230963,21.004534), 10, 10, new ArrayList<>());
        DisSimProgrammableNode disSimNode = (DisSimProgrammableNode) sensor;
        simEngine.addNode(disSimNode);
        simEngine.runScenario();
        Thread.sleep(1000);

        SensorAPI<GeoPosition> api = disSimNode.getAPI();

        List<GeoPosition> route = new ArrayList<>();
        route.add(new GeoPosition(52.230963,21.004534));
        route.add(new GeoPosition(52.231594, 21.003547));
        api.api_setRoute(route);
        api.api_startMove();

        Thread.sleep(1000);
        TestCase.assertTrue(api.api_getPosition().getLatitude() != 52.231594 || api.api_getPosition().getLongitude() != 21.003547);
        api.api_stopMove();
        GeoPosition posAfterStop = api.api_getPosition();
        Thread.sleep(5000);

        GeoPosition posAfterStop2 = api.api_getPosition();
        TestCase.assertEquals(posAfterStop , posAfterStop2);

        api.api_startMove();

        Thread.sleep(5000);

        GeoPosition positionAfterStart = api.api_getPosition();

        TestCase.assertTrue(positionAfterStart.getLatitude() != posAfterStop2.getLatitude() && positionAfterStart.getLongitude() != posAfterStop2.getLongitude());

        simEngine.stopScenario();
        Thread.sleep(1000);
    }

    @Test
    public void api_communicationTest() throws InterruptedException {
        SimEngine<DisSimProgrammableNode> simEngine = injector.getInstance(SimEngine.class);
        SensorNodeFactory sensorNodeFactory = injector.getInstance(SensorNodeFactory.class);

        ProgrammableNode sender = sensorNodeFactory.createGeoSensorNode(1, new GeoPosition(52.230963,21.004534), 10, 0, new ArrayList<>());
        ProgrammableNode receiver = sensorNodeFactory.createGeoSensorNode(2, new GeoPosition(52.230963,21.004534), 10, 0, new ArrayList<>());

        simEngine.addNode((DisSimProgrammableNode) sender);
        simEngine.addNode((DisSimProgrammableNode) receiver);
        simEngine.runScenario();
        Thread.sleep(1000);

        sender.setRoutingAlgorithm(new FloodingRouting());
        receiver.setRoutingAlgorithm(new FloodingRouting());

        final StringBuilder content = new StringBuilder();

        Function<Message<?> , Object> handler = h -> {
            System.out.println();
            content.append((String) h.getMessageContent());
            return null;
        };

        receiver.getAPI().api_setOnMessageHandler(handler);
        sender.getAPI().api_sendMessage(2, new TestMessage(1,2, 5000));

        Thread.sleep(5000);

        TestCase.assertEquals("test", content.toString());

        simEngine.stopScenario();
        Thread.sleep(1000);
    }

    @Test
    public void api_hopByHopFloodingTest() throws InterruptedException {
        SimEngine<DisSimProgrammableNode> simEngine = injector.getInstance(SimEngine.class);
        SensorNodeFactory sensorNodeFactory = injector.getInstance(SensorNodeFactory.class);

        ProgrammableNode sender = sensorNodeFactory.createGeoSensorNode(1, new GeoPosition(52.230532,21.005521), 25, 0, new ArrayList<>());
        ProgrammableNode hop1 = sensorNodeFactory.createGeoSensorNode(2, new GeoPosition(52.230535,21.005795), 25, 0, new ArrayList<>());
        ProgrammableNode receiver = sensorNodeFactory.createGeoSensorNode(3, new GeoPosition(52.230556,21.005937), 25, 0, new ArrayList<>());
        ProgrammableNode hop2 = sensorNodeFactory.createGeoSensorNode(4, new GeoPosition(52.230555,21.005819), 15, 0, new ArrayList<>());

        simEngine.addNode((DisSimProgrammableNode) sender);
        simEngine.addNode((DisSimProgrammableNode) hop1);
        simEngine.addNode((DisSimProgrammableNode) receiver);
        simEngine.addNode((DisSimProgrammableNode) hop2);
        simEngine.runScenario();
        Thread.sleep(1000);

        sender.setRoutingAlgorithm(new FloodingRouting());
        hop1.setRoutingAlgorithm(new FloodingRouting());
        receiver.setRoutingAlgorithm(new FloodingRouting());
        hop2.setRoutingAlgorithm(new FloodingRouting());


        final StringBuilder senderContent = new StringBuilder();
        Function<Message<?> , Object> senderHandler = h -> {
            senderContent.append((String) h.getMessageContent());
            return null;
        };
        sender.getAPI().api_setOnMessageHandler(senderHandler);

        final StringBuilder hop1Content = new StringBuilder();
        Function<Message<?> , Object> hop1Handler = h -> {
            hop1Content.append((String) h.getMessageContent());
            return null;
        };
        hop1.getAPI().api_setOnMessageHandler(hop1Handler);

        final StringBuilder receiverContent = new StringBuilder();
        Function<Message<?> , Object> receiverHandler = h -> {
            receiverContent.append((String) h.getMessageContent());
            return null;
        };
        receiver.getAPI().api_setOnMessageHandler(receiverHandler);


        sender.getAPI().api_sendMessage(3, new TestMessage(1,3, 512));

//        while(true);

        Thread.sleep(5000);

        // the receiver should receive two messages (from hop1 and hop2)
        // so the output string is "testtest"
        TestCase.assertEquals("testtest", receiverContent.toString());
        TestCase.assertEquals("testtest", hop1Content.toString());
        TestCase.assertEquals("", senderContent.toString());

        simEngine.stopScenario();
        Thread.sleep(1000);
    }

    private static class TestMessage implements Message<Object> {

        private int source;
        private int dest;
        private int size;

        protected TestMessage(int source, int dest, int size) {
            this.source = source;
            this.dest = dest;
            this.size = size;
        }

        @Override
        public int getID() {
            return 1;
        }

        @Override
        public Object getMessageContent() {
            return "test";
        }

        public int getMessageSource(){
            return this.source;
        }

        /**
         * Returns destination sensor of the message. It is the origin sensor, which should not change during communication
         * process.
         * @return
         *      destination (sink) sensor
         */
        public int getMessageDest(){
            return this.dest;
        }

        @Override
        public int getSize() {
            return this.size;
        }
    }

}
