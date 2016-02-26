package programming;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import dissim.simspace.core.SimModel;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.network.DefaultWirelessChannel;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.*;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.program.groovy.GroovyMiddlewareFactory;
import org.mdyk.netsim.logic.node.program.groovy.GroovyProgram;
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

public class GroovyProgrammingTest {

    private Injector injector;

    @Before
    public void init() throws Exception{
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
    @SuppressWarnings("unchecked")
    public void installProgramOnOneNodeTest() throws Exception {
        SimEngine simEngine = injector.getInstance(SimEngine.class);
        SensorsFactory sensorsFactory = injector.getInstance(SensorsFactory.class);

        Device node1 = sensorsFactory.buildSensor(1, new GeoPosition(52.230532, 21.005521), 25, 5000 , 10, new ArrayList<>());

        List<GeoPosition> route = new ArrayList<>();
        route.add(new GeoPosition(52.230532, 21.005521));
        route.add(new GeoPosition(52.230963,21.004534));
        route.add(new GeoPosition(52.231594, 21.003547));
        node1.getDeviceLogic().setRoute(route);

        simEngine.addNode(node1);
        simEngine.runScenario();
        Thread.sleep(1000);

        SensorAPI<GeoPosition> api = node1.getSensorAPI();
        api.api_startMove();

        Thread.sleep(1000);
        TestCase.assertTrue(api.api_getPosition().getLatitude() != 52.231594 || api.api_getPosition().getLongitude() != 21.003547);

        Middleware middleware =node1.getMiddleware();

        // Program should stop node's movement
        String stopMoveScript = "api.api_stopMove();";
        GroovyProgram stopPrgram = new GroovyProgram(stopMoveScript, false);

        middleware.loadProgram(stopPrgram);

        Thread.sleep(1000);
        GeoPosition nodePosition1 = (GeoPosition) node1.getSensorAPI().api_getPosition();
        Thread.sleep(2000);
        GeoPosition nodePosition2 = (GeoPosition) node1.getSensorAPI().api_getPosition();

        TestCase.assertEquals(nodePosition1.getLatitude() , nodePosition2.getLatitude());
        TestCase.assertEquals(nodePosition1.getLongitude() , nodePosition2.getLongitude());

        // Program should start node's movement
        String startMoveScript = "api.api_startMove();";
        GroovyProgram startProgram = new GroovyProgram(startMoveScript, false);

        middleware.loadProgram(startProgram);

        Thread.sleep(1000);
        nodePosition1 = (GeoPosition) node1.getSensorAPI().api_getPosition();
        Thread.sleep(2000);
        nodePosition2 = (GeoPosition) node1.getSensorAPI().api_getPosition();

        TestCase.assertNotSame(nodePosition1.getLatitude() , nodePosition2.getLatitude());
        TestCase.assertNotSame(nodePosition1.getLongitude() , nodePosition2.getLongitude());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void installProgramOnAllNodesTest() throws Exception {
        SimEngine simEngine = injector.getInstance(SimEngine.class);
        SensorsFactory sensorsFactory = injector.getInstance(SensorsFactory.class);

        Device node1 = sensorsFactory.buildSensor(1, new GeoPosition(52.230532, 21.005521), 25, 5000, 10, new ArrayList<>());
        Device node2 = sensorsFactory.buildSensor(2, new GeoPosition(52.230553, 21.005862), 25, 5000, 10, new ArrayList<>());
        Device node3 = sensorsFactory.buildSensor(3, new GeoPosition(52.230562, 21.006125), 25, 5000, 10, new ArrayList<>());
        Device node4 = sensorsFactory.buildSensor(4, new GeoPosition(52.230572, 21.006419), 25, 5000, 10, new ArrayList<>());

        simEngine.addNode(node1);
        simEngine.addNode(node2);
        simEngine.addNode(node3);
        simEngine.addNode(node4);

        simEngine.runScenario();

        Thread.sleep(1000);

        node1.getSensorAPI().api_stopMove();
        node2.getSensorAPI().api_stopMove();
        node3.getSensorAPI().api_stopMove();
        node4.getSensorAPI().api_stopMove();

        GeoPosition node1Pos1 = (GeoPosition) node1.getSensorAPI().api_getPosition();
        GeoPosition node2Pos1 = (GeoPosition) node2.getSensorAPI().api_getPosition();
        GeoPosition node3Pos1 = (GeoPosition) node3.getSensorAPI().api_getPosition();
        GeoPosition node4Pos1 = (GeoPosition) node4.getSensorAPI().api_getPosition();

        Thread.sleep(2000);

        GeoPosition node1Pos2 = (GeoPosition) node1.getSensorAPI().api_getPosition();
        GeoPosition node2Pos2 = (GeoPosition) node2.getSensorAPI().api_getPosition();
        GeoPosition node3Pos2 = (GeoPosition) node3.getSensorAPI().api_getPosition();
        GeoPosition node4Pos2 = (GeoPosition) node4.getSensorAPI().api_getPosition();

        TestCase.assertEquals(node1Pos1,node1Pos2);
        TestCase.assertEquals(node2Pos1,node2Pos2);
        TestCase.assertEquals(node3Pos1,node3Pos2);
        TestCase.assertEquals(node4Pos1,node4Pos2);

        List<GeoPosition> node1Route = new ArrayList<>();
        node1Route.add(new GeoPosition(52.230532, 21.005521));
        node1Route.add(new GeoPosition(52.230553, 21.005862));

        node1.getSensorAPI().api_setRoute(node1Route);

        List<GeoPosition> node2Route = new ArrayList<>();
        node2Route.add(new GeoPosition(52.230553, 21.005862));
        node2Route.add(new GeoPosition(52.230562, 21.006125));

        node2.getSensorAPI().api_setRoute(node2Route);

        List<GeoPosition> node3Route = new ArrayList<>();
        node3Route.add(new GeoPosition(52.230562, 21.006125));
        node3Route.add(new GeoPosition(52.230572, 21.006419));

        node3.getSensorAPI().api_setRoute(node3Route);

        List<GeoPosition> node4Route = new ArrayList<>();
        node4Route.add(new GeoPosition(52.230572, 21.006419));
        node4Route.add(new GeoPosition(52.230588, 21.00665));

        node4.getSensorAPI().api_setRoute(node4Route);

        // Program should start node's movement
        String startMoveScript = "api.api_startMove();";
        GroovyProgram startProgram = new GroovyProgram(startMoveScript, true);

        node1.getMiddleware().loadProgram(startProgram);

        Thread.sleep(5000);

        GeoPosition node1Pos3 = (GeoPosition) node1.getSensorAPI().api_getPosition();
        GeoPosition node2Pos3 = (GeoPosition) node2.getSensorAPI().api_getPosition();
        GeoPosition node3Pos3 = (GeoPosition) node3.getSensorAPI().api_getPosition();
        GeoPosition node4Pos3 = (GeoPosition) node4.getSensorAPI().api_getPosition();

        TestCase.assertTrue(node1Pos1.getLatitude() != node1Pos3.getLatitude());
        TestCase.assertTrue(node1Pos1.getLongitude() != node1Pos3.getLongitude());

        TestCase.assertTrue(node2Pos1.getLatitude() != node2Pos3.getLatitude());
        TestCase.assertTrue(node2Pos1.getLongitude() != node2Pos3.getLongitude());

        TestCase.assertTrue(node3Pos1.getLatitude() != node3Pos3.getLatitude());
        TestCase.assertTrue(node3Pos1.getLongitude() != node3Pos3.getLongitude());

        TestCase.assertTrue(node4Pos1.getLatitude() != node4Pos3.getLatitude());
        TestCase.assertTrue(node4Pos1.getLongitude() != node4Pos3.getLongitude());

    }

}
