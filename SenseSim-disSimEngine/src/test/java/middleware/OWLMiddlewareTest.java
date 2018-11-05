package middleware;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import dissim.simspace.core.SimModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.network.DefaultWirelessChannel;
import org.mdyk.netsim.logic.network.NetworkManager;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.*;
import org.mdyk.netsim.logic.node.program.owl.OWLMiddleware;
import org.mdyk.netsim.logic.node.program.owl.OWLMiddlewareFactory;
import org.mdyk.netsim.logic.node.statistics.DefaultStatisticsFactory;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.logic.sensor.DefaultSensorFactory;
import org.mdyk.netsim.logic.sensor.SensorFactory;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.device.connectivity.CommunicationInterface;
import org.mdyk.sensesim.simulation.engine.dissim.DisSimEngine;
import org.mdyk.sensesim.simulation.engine.dissim.communication.DisSimCommunicationProcessFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimAPIFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimEntityFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimSensorsLogicFactory;
import org.mdyk.sensesim.simulation.engine.dissim.phenomena.DisSimPhenomenaFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import sensesim.integration.mcop.MCopPluginFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
                bind(WirelessChannel.class).to(DefaultWirelessChannel.class);
                bind(NetworkManager.class);
                install(new FactoryModuleBuilder().build(ScenarioFactory.class));

                bind(SimEngine.class).to(DisSimEngine.class);
                bind(DeviceLogicFactory.class).to(DisSimSensorsLogicFactory.class);
                bind(SimEntityFactory.class).to(DisSimEntityFactory.class);
                bind(APIFactory.class).to(DisSimAPIFactory.class);
                bind(SensorFactory.class).to(DefaultSensorFactory.class);
                bind(PhenomenaFactory.class).to(DisSimPhenomenaFactory.class);
                bind(MiddlewareFactory.class).to(OWLMiddlewareFactory.class);
                bind(DeviceStatisticsFactory.class).to(DefaultStatisticsFactory.class);
                bind(CommunicationProcessFactory.class).to(DisSimCommunicationProcessFactory.class);
//                bind(IRealDevicePlugin.class).to(RealDevicePlugin.class);
//                bind(MCopPluginFactory.class).to(org.mdyk.sensesim.integrator.mcop.plugin.MCopPluginFactoryImpl.class);
            }
        });


        Field instance = SimModel.class.getDeclaredField("simModel");
        instance.setAccessible(true);
        instance.set(null, null);

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

    @Test
    public void verifyInformationNeed() throws OWLOntologyCreationException {

        String ontologyPath = "/Users/michal/Documents/Workspace/SenseSim/SenseSim-app/src/main/resources/cognitive-agent-ontology.owl";
        String ontologyIRI = "http://www.semanticweb.org/michal/ontologies/2018/7/cognitive-agent-ontology";

        SimEngine simEngine = injector.getInstance(SimEngine.class);
        DevicesFactory devicesFactory = injector.getInstance(DevicesFactory.class);

        List<CommunicationInterface> communicationInterfaces = new ArrayList<>();
        CommunicationInterface commInt1 = new CommunicationInterface(1, "int1",5000,5000,10, CommunicationInterface.TopologyType.ADHOC);
        communicationInterfaces.add(commInt1);

        Device device = devicesFactory.buildSensor(1, "device-1", new GeoPosition(52.230963, 21.004534), 10, 5000, 10, new ArrayList<>(), new ArrayList<>(), communicationInterfaces);
        simEngine.addNode(device);
        simEngine.runScenario();

        OWLMiddleware middleware = (OWLMiddleware) device.getMiddleware();
        File ontologyFile = new File(ontologyPath);

        middleware.loadOntology(ontologyFile, ontologyIRI);

        middleware.verifyInformationNeed(null);

        //TestCase.assertTrue(middleware.relationExists("Immediate"));



    }

}