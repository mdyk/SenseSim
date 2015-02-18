package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
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

import java.util.ArrayList;
import java.util.List;

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
        simEngine.stopScenario();
        TestCase.assertTrue(api.api_getPosition().getLatitude() != 52.231594 || api.api_getPosition().getLongitude() != 21.003547);

    }

}
