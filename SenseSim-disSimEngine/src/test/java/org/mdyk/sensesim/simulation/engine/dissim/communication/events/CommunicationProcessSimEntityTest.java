package org.mdyk.sensesim.simulation.engine.dissim.communication.events;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import dissim.simspace.core.SimControlException;
import dissim.simspace.core.SimModel;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.network.DefaultWirelessChannel;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.*;
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

public class CommunicationProcessSimEntityTest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(CommunicationProcessFactory.class).to(DisSimCommunicationProcessFactory.class);
                bind(WirelessChannel.class).to(DefaultWirelessChannel.class);
                bind(SimEngine.class).to(DisSimEngine.class);
                bind(SensorLogicFactory.class).to(DisSimSensorsLogicFactory.class);
                bind(SimEntityFactory.class).to(DisSimEntityFactory.class);
                bind(SensorAPIFactory.class).to(DisSimSensorAPIFactory.class);
                bind(MiddlewareFactory.class).to(GroovyMiddlewareFactory.class);
                bind(PhenomenaFactory.class).to(DisSimPhenomenaFactory.class);
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
    public void testCommunication() throws SimControlException, InterruptedException {

        SimEngine simEngine = injector.getInstance(SimEngine.class);
        SensorsFactory sensorsFactory = injector.getInstance(SensorsFactory.class);
        CommunicationProcessFactory processFactory = injector.getInstance(CommunicationProcessFactory.class);

        Device sender = sensorsFactory.buildSensor(1, new GeoPosition(52.230963, 21.004534), 10, 5000, 0, new ArrayList<>());
        Device receiver = sensorsFactory.buildSensor(2, new GeoPosition(52.230963, 21.004534), 10, 5000, 0, new ArrayList<>());

        Message message = new TestMessage() {
            @Override
            public int getSize() {
                return 5000;
            }
        };

        simEngine.addNode(sender);
        simEngine.addNode(receiver);
        simEngine.runScenario();

        CommunicationProcessSimEntity communicationSimEntity = (CommunicationProcessSimEntity) processFactory.createCommunicationProcess(0, sender.getDeviceLogic(), receiver.getDeviceLogic(), 2, message);

        Thread.sleep(10000);

        TestCase.assertEquals(CommunicationStatus.SUCCESS , communicationSimEntity.getCommunicationStatus(28.0));

        simEngine.stopScenario();
        Thread.sleep(1000);

    }

    private static abstract class TestMessage implements Message {

        @Override
        public int getID() {
            return 1;
        }

        @Override
        public Object getMessageContent() {
            return null;
        }

        public int getMessageSource(){
           return -1;
        }

        /**
         * Returns destination device of the message. It is the origin device, which should not change during communication
         * process.
         * @return
         *      destination (sink) device
         */
        public int getMessageDest(){
            return -1;
        }

    }


}
