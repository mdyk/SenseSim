package org.mdyk.sensesim.simulation.engine.dissim.communication.events;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import dissim.simspace.core.SimModel;
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
import sensesim.integration.mcop.MCopPluginFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class StayInIdleTest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(CommunicationProcessFactory.class).to(DisSimCommunicationProcessFactory.class);
                bind(WirelessChannel.class).to(DefaultWirelessChannel.class);
                bind(SimEngine.class).to(DisSimEngine.class);
                bind(DeviceLogicFactory.class).to(DisSimSensorsLogicFactory.class);
                bind(SimEntityFactory.class).to(DisSimEntityFactory.class);
                bind(APIFactory.class).to(DisSimAPIFactory.class);
                bind(SensorFactory.class).to(DefaultSensorFactory.class);
                bind(MiddlewareFactory.class).to(GroovyMiddlewareFactory.class);
                bind(PhenomenaFactory.class).to(DisSimPhenomenaFactory.class);
                bind(DeviceStatisticsFactory.class).to(DefaultStatisticsFactory.class);
                install(new FactoryModuleBuilder().build(ScenarioFactory.class));
                bind(MCopPluginFactory.class).to(org.mdyk.sensesim.integrator.mcop.plugin.MCopPluginFactoryImpl.class);
            }
        });

        Field instance = SimModel.class.getDeclaredField("simModel");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void stayInIdle() throws InterruptedException {

        SimEngine simEngine = injector.getInstance(SimEngine.class);
        DevicesFactory devicesFactory = injector.getInstance(DevicesFactory.class);

        List<CommunicationInterface> communicationInterfaces = new ArrayList<>();
        CommunicationInterface commInt1 = new CommunicationInterface(1, "int1",5000,5000,90, CommunicationInterface.TopologyType.ADHOC);
        communicationInterfaces.add(commInt1);

        Device sender = devicesFactory.buildSensor(1, "device-1", new GeoPosition(52.230963, 21.004534), 10, 5000, 0, new ArrayList<>(), new ArrayList<>(), communicationInterfaces);
        
        simEngine.addNode(sender);
        simEngine.runScenario();


        sender.getDeviceAPI().api_stayIdleFor(10);

        Thread.sleep(10000);

        simEngine.stopScenario();        

    }

}
