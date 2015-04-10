package org.mdyk.netsim.logic.scenario;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.routing.FloodingRouting;
import org.mdyk.netsim.logic.environment.phenomena.DefaultPhenomenaFactory;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.network.DefaultWirelessChannel;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.SensorsFactory;
import org.mdyk.netsim.logic.simEngine.thread.GeoSensorNodeThread;
import org.mdyk.netsim.logic.scenario.xml.XMLScenario;
import org.mdyk.netsim.logic.simEngine.thread.SensorsFactoryThread;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.IPhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

import java.io.File;
import java.util.List;
import java.util.Map;


public class XMLScenarioTest {


    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(WirelessChannel.class).to(DefaultWirelessChannel.class);
//                bind(SensorsFactory.class).to(SensorsFactoryThread.class);
                bind(PhenomenaFactory.class).to(DefaultPhenomenaFactory.class);
                install(new FactoryModuleBuilder().build(ScenarioFactory.class));
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        injector = null;
    }

    @Test
    public void testScenarioName() throws Exception {
        File scenarioXML = FileUtils.toFile(getClass().getResource("/scenario-1.xml"));
        ScenarioFactory scenarioFactory = injector.getInstance(ScenarioFactory.class);
        XMLScenario xmlScenario = scenarioFactory.createXMLScenario(scenarioXML);

        TestCase.assertEquals("1", xmlScenario.scenarioName());
    }

    @Test
    public void testScenarioSensors() throws Exception {
//        File scenarioXML = FileUtils.toFile(getClass().getResource("/scenario-1.xml"));
//        ScenarioFactory scenarioFactory = injector.getInstance(ScenarioFactory.class);
//        XMLScenario xmlScenario = scenarioFactory.createXMLScenario(scenarioXML);
//
//        Map<Class, List<ISensorModel>> nodesMap = xmlScenario.scenarioSensors();
//
//        TestCase.assertTrue(nodesMap.size() == 1);
//        TestCase.assertTrue(nodesMap.containsKey(GeoSensorNodeThread.class));
//
//        List<ISensorModel> nodes = nodesMap.get(GeoSensorNodeThread.class);
//        TestCase.assertTrue(nodes.size() == 4);
//
//        for (ISensorModel node : nodes) {
//            TestCase.assertTrue(node instanceof GeoSensorNodeThread);
//            TestCase.assertTrue(node.getRoutingAlgorithm() instanceof FloodingRouting);
//            TestCase.assertTrue(node.getAbilities().get(0).equals(AbilityType.TEMPERATURE));
//        }

    }

    @Test
    public void testScenarioPhenomena() throws Exception {
        File scenarioXML = FileUtils.toFile(getClass().getResource("/scenario-1.xml"));
        ScenarioFactory scenarioFactory = injector.getInstance(ScenarioFactory.class);
        XMLScenario xmlScenario = scenarioFactory.createXMLScenario(scenarioXML);

        List<IPhenomenonModel<GeoPosition>> phenomenonModelList = xmlScenario.getPhenomena();

        TestCase.assertEquals(1 , phenomenonModelList.size());

        PhenomenonValue val1 = phenomenonModelList.get(0).getPhenomenonValue(AbilityType.TEMPERATURE,1);
        TestCase.assertEquals(110 , val1.getValue());

        PhenomenonValue val2 = phenomenonModelList.get(0).getPhenomenonValue(AbilityType.TEMPERATURE,500);
        TestCase.assertEquals(110 , val2.getValue());

        PhenomenonValue val3 = phenomenonModelList.get(0).getPhenomenonValue(AbilityType.TEMPERATURE,1000);
        TestCase.assertEquals(110 , val3.getValue());
    }

}
