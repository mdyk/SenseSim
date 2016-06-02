package sensing;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import dissim.simspace.core.SimModel;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.network.DefaultWirelessChannel;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.*;
import org.mdyk.netsim.logic.node.program.groovy.GroovyMiddlewareFactory;
import org.mdyk.netsim.logic.node.statistics.DefaultStatisticsFactory;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.logic.scenario.xml.XMLScenario;
import org.mdyk.netsim.logic.sensor.DefaultSensorFactory;
import org.mdyk.netsim.logic.sensor.SensorFactory;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.temperature.TemperatureConfigurationSpace;
import org.mdyk.sensesim.simulation.engine.dissim.DisSimEngine;
import org.mdyk.sensesim.simulation.engine.dissim.communication.DisSimCommunicationProcessFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimAPIFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimEntityFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimSensorsLogicFactory;
import org.mdyk.sensesim.simulation.engine.dissim.phenomena.DisSimPhenomenaFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;




public class SenseActivityTest {


    private Injector injector;

    @Before
    public void init() throws Exception {
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

        Field instance = SimModel.class.getDeclaredField("simModel");
        instance.setAccessible(true);
        instance.set(null, null);
    }


    @Test
    public void senseActivityTest() throws Exception{

        SimEngine simEngine = injector.getInstance(SimEngine.class);

        File scenarioXML = FileUtils.toFile(getClass().getResource("/scenario-1.xml"));
        ScenarioFactory scenarioFactory = injector.getInstance(ScenarioFactory.class);
        XMLScenario xmlScenario = scenarioFactory.createXMLScenario(scenarioXML);

        simEngine.loadScenario(xmlScenario);

        simEngine.runScenario();

        Thread.sleep(2000);

        List<Device> devices = simEngine.getDeviceList();

        Device fourthDev = null;
        for(Device device : devices) {
            if(device.getDeviceLogic().getID() == 4) {
                fourthDev = device;
            }
        }

        Map<Class<? extends ConfigurationSpace>, Map<Double, List<ConfigurationSpace>>> observations = fourthDev.getDeviceLogic().getObservations();

        TestCase.assertTrue(observations.containsKey(TemperatureConfigurationSpace.class));
        Map<Double, List<ConfigurationSpace>> tempObservations = observations.get(TemperatureConfigurationSpace.class);

        for(List<ConfigurationSpace> temps : tempObservations.values()) {
            for(ConfigurationSpace configurationSpace : temps) {
                TemperatureConfigurationSpace temperatureConfigurationSpace = (TemperatureConfigurationSpace) configurationSpace;
                TestCase.assertTrue(temperatureConfigurationSpace.getTemperature() >= 9d && temperatureConfigurationSpace.getTemperature() <= 11d);
            }
        }

        simEngine.stopScenario();

    }

}
