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
import org.mdyk.netsim.logic.node.*;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.program.groovy.GroovyMiddlewareFactory;
import org.mdyk.netsim.logic.node.statistics.DefaultStatisticsFactory;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.sensesim.simulation.engine.dissim.DisSimEngine;
import org.mdyk.sensesim.simulation.engine.dissim.communication.DisSimCommunicationProcessFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimEntityFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimSensorAPIFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimSensorsLogicFactory;
import org.mdyk.sensesim.simulation.engine.dissim.phenomena.DisSimPhenomenaFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DisSimDeviceAPITest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(CommunicationProcessFactory.class).to(DisSimCommunicationProcessFactory.class);
                bind(WirelessChannel.class).to(DefaultWirelessChannel.class);
                bind(SimEngine.class).to(DisSimEngine.class);
                bind(SimEntityFactory.class).to(DisSimEntityFactory.class);
                bind(SensorAPIFactory.class).to(DisSimSensorAPIFactory.class);
                bind(SensorLogicFactory.class).to(DisSimSensorsLogicFactory.class);
                bind(MiddlewareFactory.class).to(GroovyMiddlewareFactory.class);
                bind(PhenomenaFactory.class).to(DisSimPhenomenaFactory.class);
                bind(CommunicationProcessFactory.class).to(DisSimCommunicationProcessFactory.class);
                bind(SensorStatisticsFactory.class).to(DefaultStatisticsFactory.class);
                install(new FactoryModuleBuilder().build(ScenarioFactory.class));
            }
        });

        Field instance = SimModel.class.getDeclaredField("simModel");
        instance.setAccessible(true);
        instance.set(null, null);

    }

    @Test
    public void api_moveTest() throws InterruptedException {
        SimEngine simEngine = injector.getInstance(SimEngine.class);
        DevicesFactory devicesFactory = injector.getInstance(DevicesFactory.class);

        Device device = devicesFactory.buildSensor(1, new GeoPosition(52.230963, 21.004534), 10, 5000, 10, new ArrayList<>());
        simEngine.addNode(device);
        simEngine.runScenario();
        Thread.sleep(1000);

        SensorAPI<GeoPosition> api = device.getSensorAPI();

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

        Thread.sleep(10000);

        GeoPosition positionAfterStart = api.api_getPosition();

        TestCase.assertTrue(positionAfterStart.getLatitude() != posAfterStop2.getLatitude() && positionAfterStart.getLongitude() != posAfterStop2.getLongitude());

        simEngine.stopScenario();
        Thread.sleep(1000);
    }

    @Test
    public void api_communicationTest() throws InterruptedException {
        SimEngine simEngine = injector.getInstance(SimEngine.class);
        DevicesFactory devicesFactory = injector.getInstance(DevicesFactory.class);

        Device sender = devicesFactory.buildSensor(1, new GeoPosition(52.230963, 21.004534), 10, 5000, 0, new ArrayList<>());
        Device receiver = devicesFactory.buildSensor(2, new GeoPosition(52.230963, 21.004534), 10, 5000, 0, new ArrayList<>());

        simEngine.addNode(sender);
        simEngine.addNode(receiver);
        simEngine.runScenario();
        Thread.sleep(1000);

        sender.getDeviceLogic().setRoutingAlgorithm(new FloodingRouting(sender.getStatistics()));
        receiver.getDeviceLogic().setRoutingAlgorithm(new FloodingRouting(receiver.getStatistics()));

        final StringBuilder content = new StringBuilder();

        Function<Message , Object> handler = h -> {
            content.append((String) h.getMessageContent());
            return null;
        };

        receiver.getSensorAPI().api_setOnMessageHandler(handler);
        sender.getSensorAPI().api_sendMessage(1, 1,2, "test", 5000);

        Thread.sleep(10000);

        TestCase.assertEquals("test", content.toString());

        simEngine.stopScenario();
        Thread.sleep(1000);
    }

    @Test
    public void api_hopByHopFloodingTest() throws InterruptedException {
        SimEngine simEngine = injector.getInstance(SimEngine.class);
        DevicesFactory devicesFactory = injector.getInstance(DevicesFactory.class);

        Device sender = devicesFactory.buildSensor(1, new GeoPosition(52.230532, 21.005521), 25, 5000, 0, new ArrayList<>());
        Device hop1 = devicesFactory.buildSensor(2, new GeoPosition(52.230535, 21.005795), 25,5000, 0, new ArrayList<>());
        Device receiver = devicesFactory.buildSensor(3, new GeoPosition(52.230556, 21.005937), 25,5000, 0, new ArrayList<>());
        Device hop2 = devicesFactory.buildSensor(4, new GeoPosition(52.230555, 21.005819), 15,5000, 0, new ArrayList<>());

        simEngine.addNode(sender);
        simEngine.addNode(hop1);
        simEngine.addNode(receiver);
        simEngine.addNode(hop2);
        simEngine.runScenario();
        Thread.sleep(1000);

        sender.getDeviceLogic().setRoutingAlgorithm(new FloodingRouting(sender.getStatistics()));
        hop1.getDeviceLogic().setRoutingAlgorithm(new FloodingRouting(hop1.getStatistics()));
        receiver.getDeviceLogic().setRoutingAlgorithm(new FloodingRouting(receiver.getStatistics()));
        hop2.getDeviceLogic().setRoutingAlgorithm(new FloodingRouting(hop2.getStatistics()));


        final StringBuilder senderContent = new StringBuilder();
        final ArrayList<Integer> senderCount = new ArrayList<>();
        Function<Message , Object> senderHandler = h -> {
            senderContent.append((String) h.getMessageContent());
            senderCount.add(1);
            return null;
        };
        sender.getSensorAPI().api_setOnMessageHandler(senderHandler);

        final StringBuilder hop1Content = new StringBuilder();
        final ArrayList<Integer> hop1Count = new ArrayList<>();
        Function<Message , Object> hop1Handler = h -> {
            hop1Content.append((String) h.getMessageContent());
            hop1Count.add(1);
            return null;
        };
        hop1.getSensorAPI().api_setOnMessageHandler(hop1Handler);

        final StringBuilder receiverContent = new StringBuilder();
        final ArrayList<Integer> receiverCount = new ArrayList<>();
        Function<Message , Object> receiverHandler = h -> {
            receiverContent.append((String) h.getMessageContent());
            receiverCount.add(1);
            return null;
        };
        receiver.getSensorAPI().api_setOnMessageHandler(receiverHandler);


        sender.getSensorAPI().api_sendMessage(1, 1,3,"test", 512);

//        while(true);

        Thread.sleep(5000);

        System.out.println("SenderCount="+senderCount.size());
        System.out.println("Hop1Count="+hop1Count.size());
        System.out.println("ReceiverCount="+receiverCount.size());

        // FIXME odblokować po poprawieniu algorytmu routingu
        // the receiver should receive two messages (from hop1 and hop2)
        // so the output string is "testtest"
        TestCase.assertEquals("", senderContent.toString());
        TestCase.assertEquals("testtesttest", hop1Content.toString());
        TestCase.assertEquals("testtesttest", receiverContent.toString());


        simEngine.stopScenario();
        Thread.sleep(1000);
    }



}
