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
                install(new FactoryModuleBuilder().build(ScenarioFactory.class));
            }
        });

        Field instance = SimModel.class.getDeclaredField("simModel");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void installProgramOnOneNodeTest() throws Exception {
        SimEngine simEngine = injector.getInstance(SimEngine.class);
        SensorsFactory sensorsFactory = injector.getInstance(SensorsFactory.class);

        Sensor node1 = sensorsFactory.buildSensor(1, new GeoPosition(52.230532, 21.005521), 25, 0, new ArrayList<>());

        List<GeoPosition> route = new ArrayList<>();
        route.add(new GeoPosition(52.230963,21.004534));
        route.add(new GeoPosition(52.231594, 21.003547));
        node1.getSensorLogic().setRoute(route);

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
        GroovyProgram stopPrgram = new GroovyProgram(stopMoveScript);

        middleware.loadProgram(stopPrgram);

        Thread.sleep(1000);
        GeoPosition nodePosition1 = (GeoPosition) node1.getSensorAPI().api_getPosition();
        Thread.sleep(2000);
        GeoPosition nodePosition2 = (GeoPosition) node1.getSensorAPI().api_getPosition();

        TestCase.assertEquals(nodePosition1.getLatitude() , nodePosition2.getLatitude());
        TestCase.assertEquals(nodePosition1.getLongitude() , nodePosition2.getLongitude());

        // Program should start node's movement
        String startMoveScript = "api.api_startMove();";
        GroovyProgram startProgram = new GroovyProgram(startMoveScript);

        middleware.loadProgram(startProgram);

        Thread.sleep(1000);
        nodePosition1 = (GeoPosition) node1.getSensorAPI().api_getPosition();
        Thread.sleep(2000);
        nodePosition2 = (GeoPosition) node1.getSensorAPI().api_getPosition();

        TestCase.assertNotSame(nodePosition1.getLatitude() , nodePosition2.getLatitude());
        TestCase.assertNotSame(nodePosition1.getLongitude() , nodePosition2.getLongitude());

    }

    @Test
    public void installProgramOnAllNodesTest() throws Exception {
        SimEngine simEngine = injector.getInstance(SimEngine.class);
        SensorsFactory sensorsFactory = injector.getInstance(SensorsFactory.class);

        Sensor sender = sensorsFactory.buildSensor(1, new GeoPosition(52.230532, 21.005521), 25, 0, new ArrayList<>());
        Sensor hop1 = sensorsFactory.buildSensor(2, new GeoPosition(52.230535, 21.005795), 25, 0, new ArrayList<>());
        Sensor receiver = sensorsFactory.buildSensor(3, new GeoPosition(52.230556, 21.005937), 25, 0, new ArrayList<>());
        Sensor hop2 = sensorsFactory.buildSensor(4, new GeoPosition(52.230555, 21.005819), 15, 0, new ArrayList<>());

        simEngine.addNode(sender);
        simEngine.addNode(hop1);
        simEngine.addNode(receiver);
        simEngine.addNode(hop2);
        simEngine.runScenario();
        Thread.sleep(1000);



    }


}
