package scenario;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.logic.communication.routing.FloodingRouting;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.network.DefaultWirelessChannel;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.*;
import org.mdyk.netsim.logic.node.program.groovy.GroovyMiddlewareFactory;
import org.mdyk.netsim.logic.node.statistics.DefaultStatisticsFactory;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.logic.scenario.xml.XMLScenario;
import org.mdyk.netsim.logic.sensor.DefaultSensorFactory;
import org.mdyk.netsim.logic.sensor.temp.PT100Sensor;
import org.mdyk.netsim.logic.sensor.SensorFactory;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.device.connectivity.CommunicationInterface;
import org.mdyk.netsim.mathModel.observer.ecg.EcgConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.temperature.TemperatureConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.sensesim.simulation.engine.dissim.DisSimEngine;
import org.mdyk.sensesim.simulation.engine.dissim.communication.DisSimCommunicationProcessFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimAPIFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimEntityFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimSensorsLogicFactory;
import org.mdyk.sensesim.simulation.engine.dissim.phenomena.DisSimPhenomenaFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;


public class XMLScenarioTest {


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
                bind(APIFactory.class).to(DisSimAPIFactory.class);
                bind(SensorFactory.class).to(DefaultSensorFactory.class);
                bind(DeviceLogicFactory.class).to(DisSimSensorsLogicFactory.class);
                bind(MiddlewareFactory.class).to(GroovyMiddlewareFactory.class);
                bind(PhenomenaFactory.class).to(DisSimPhenomenaFactory.class);
                bind(CommunicationProcessFactory.class).to(DisSimCommunicationProcessFactory.class);
                bind(DeviceStatisticsFactory.class).to(DefaultStatisticsFactory.class);
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
        File scenarioXML = FileUtils.toFile(getClass().getResource("/scenario-1.xml"));
        ScenarioFactory scenarioFactory = injector.getInstance(ScenarioFactory.class);
        XMLScenario xmlScenario = scenarioFactory.createXMLScenario(scenarioXML);
        xmlScenario.initialize();
        List<Device> nodes = xmlScenario.scenarioDevices();

        TestCase.assertTrue(nodes.size() == 4);

        for (Device node : nodes) {
            TestCase.assertTrue(node.getDeviceLogic().getRoutingAlgorithm() instanceof FloodingRouting);
            TestCase.assertTrue(node.getDeviceLogic().getAbilities().get(0).equals(AbilityType.TEMPERATURE));
            TestCase.assertTrue(node.getDeviceLogic().getSensors().get(0) instanceof PT100Sensor);
        }

    }

    @Test
    public void testCommunicationInterfaces() throws Exception {
        File scenarioXML = FileUtils.toFile(getClass().getResource("/scenario-1.xml"));
        ScenarioFactory scenarioFactory = injector.getInstance(ScenarioFactory.class);
        XMLScenario xmlScenario = scenarioFactory.createXMLScenario(scenarioXML);
        xmlScenario.initialize();
        List<Device> nodes = xmlScenario.scenarioDevices();

        TestCase.assertTrue(nodes.size() == 4);

        for (Device node : nodes) {
            if(node.getDeviceLogic().getID() == 1){
                TestCase.assertEquals(2, node.getDeviceLogic().getCommunicationInterfaces().size());
                TestCase.assertEquals(1, node.getDeviceLogic().getCommunicationInterfaces().get(0).getId());
                TestCase.assertEquals("WiFi-Direct", node.getDeviceLogic().getCommunicationInterfaces().get(0).getName());
                TestCase.assertEquals(5000d, node.getDeviceLogic().getCommunicationInterfaces().get(0).getInputBandwidth());
                TestCase.assertEquals(5000d, node.getDeviceLogic().getCommunicationInterfaces().get(0).getOutputBandwidth());
                TestCase.assertEquals(CommunicationInterface.TopologyType.ADHOC, node.getDeviceLogic().getCommunicationInterfaces().get(0).getTopologyType());

                TestCase.assertEquals(2, node.getDeviceLogic().getCommunicationInterfaces().get(1).getId());
                TestCase.assertEquals("LTE", node.getDeviceLogic().getCommunicationInterfaces().get(1).getName());
                TestCase.assertEquals(5000d, node.getDeviceLogic().getCommunicationInterfaces().get(1).getInputBandwidth());
                TestCase.assertEquals(5000d, node.getDeviceLogic().getCommunicationInterfaces().get(1).getOutputBandwidth());
                TestCase.assertEquals(CommunicationInterface.TopologyType.FIXED, node.getDeviceLogic().getCommunicationInterfaces().get(1).getTopologyType());
                TestCase.assertEquals(2, node.getDeviceLogic().getCommunicationInterfaces().get(1).getConnectedDevices().size());
                TestCase.assertTrue(node.getDeviceLogic().getCommunicationInterfaces().get(1).getConnectedDevices().contains(3));
                TestCase.assertTrue(node.getDeviceLogic().getCommunicationInterfaces().get(1).getConnectedDevices().contains(4));
                TestCase.assertTrue(!node.getDeviceLogic().getCommunicationInterfaces().get(1).getConnectedDevices().contains(2));
            }
        }

    }


    @Test
    public void testScenarioPhenomena() throws Exception {
        File scenarioXML = FileUtils.toFile(getClass().getResource("/scenario-1.xml"));
        ScenarioFactory scenarioFactory = injector.getInstance(ScenarioFactory.class);
        XMLScenario xmlScenario = scenarioFactory.createXMLScenario(scenarioXML);
        xmlScenario.initialize();
        List<PhenomenonModel<GeoPosition>> phenomenonModelList = xmlScenario.getPhenomena();

        TestCase.assertEquals(2 , phenomenonModelList.size());

//        PhenomenonValue val1 = phenomenonModelList.get(1).getPhenomenonValue(AbilityType.TEMPERATURE,0.075d);
//        TestCase.assertEquals(36.1 , val1.getValue());

//        PhenomenonValue val2 = phenomenonModelList.get(1).getPhenomenonValue(AbilityType.TEMPERATURE,500);
//        TestCase.assertEquals(110 , val2.getMilivolts());
//
//        PhenomenonValue val3 = phenomenonModelList.get(1).getPhenomenonValue(AbilityType.TEMPERATURE,1000);
//        TestCase.assertEquals(110 , val3.getMilivolts());
//
//        PhenomenonValue valPhoto1 = phenomenonModelList.get(1).getPhenomenonValue(AbilityType.PHOTO, 500);
//        TestCase.assertTrue(valPhoto1.getValue() != null);
//
//        byte[] imageArray = FileUtils.readFileToByteArray(FileUtils.toFile(getClass().getResource("/exampleImage.jpg")));
//        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageArray));
//
//        TestCase.assertTrue(bufferedImagesEqual((BufferedImage) valPhoto1.getValue(), image));
//
//
//        PhenomenonValue valPhoto2 = phenomenonModelList.get(1).getPhenomenonValue(AbilityType.PHOTO, 1500);
//        TestCase.assertTrue(valPhoto1.getValue() != null);
//
//        TestCase.assertTrue(bufferedImagesEqual((BufferedImage) valPhoto2.getValue(), image));

        // Test zdarzenia opartego o model percepcji
        PhenomenonModel observerPhenomenon = phenomenonModelList.get(0);
        TemperatureConfigurationSpace configurationSpace = (TemperatureConfigurationSpace) observerPhenomenon.getEventValue(TemperatureConfigurationSpace.class , 5);
        TestCase.assertEquals(10d , configurationSpace.getTemperature());

        PhenomenonModel ecgPhenomenon = phenomenonModelList.get(1);
        EcgConfigurationSpace ecgConfigurationSpace = (EcgConfigurationSpace) ecgPhenomenon.getEventValue(EcgConfigurationSpace.class , 0.75d);
        TestCase.assertEquals(7.8d , ecgConfigurationSpace.getMilivolts());

    }

    boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
            for (int x = 0; x < img1.getWidth(); x++) {
                for (int y = 0; y < img1.getHeight(); y++) {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y))
                        return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

}
