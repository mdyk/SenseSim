package org.mdyk.sensesim.simulation.engine.dissim.communication.events;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import dissim.simspace.SimControlException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.mathModel.communication.Message;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.network.DefaultWirelessChannel;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.SensorNodeFactory;
import org.mdyk.netsim.logic.node.geo.RoutedGeoSensorNode;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;
import org.mdyk.sensesim.simulation.engine.dissim.DisSimEngine;
import org.mdyk.sensesim.simulation.engine.dissim.communication.DisSimCommunicationProcessFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimSensorNodeFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimRoutedNode;
import org.mdyk.sensesim.simulation.engine.dissim.phenomena.DisSimPhenomenaFactory;

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
                bind(SensorNodeFactory.class).to(DisSimSensorNodeFactory.class);
                bind(PhenomenaFactory.class).to(DisSimPhenomenaFactory.class);
                bind(CommunicationProcessFactory.class).to(DisSimCommunicationProcessFactory.class);
                install(new FactoryModuleBuilder().build(ScenarioFactory.class));
            }
        });
    }


    @Test
    @SuppressWarnings("unchecked")
    public void testCommunication() throws SimControlException, InterruptedException {

        SimEngine<DisSimRoutedNode> simEngine = injector.getInstance(SimEngine.class);
        SensorNodeFactory sensorNodeFactory = injector.getInstance(SensorNodeFactory.class);
        CommunicationProcessFactory processFactory = injector.getInstance(CommunicationProcessFactory.class);

        RoutedGeoSensorNode sender = sensorNodeFactory.createGeoSensorNode(1, new GeoPosition(52.230963,21.004534), 10, 0, new ArrayList<>());
        RoutedGeoSensorNode receiver = sensorNodeFactory.createGeoSensorNode(2, new GeoPosition(52.230963,21.004534), 10, 0, new ArrayList<>());

        Message message = new TestMessage() {
            @Override
            public int getSize() {
                return 5000;
            }
        };

        simEngine.addNode((DisSimRoutedNode) sender);
        simEngine.addNode((DisSimRoutedNode) receiver);
        simEngine.runScenario();

        CommunicationProcessSimEntity communicationSimEntity = (CommunicationProcessSimEntity) processFactory.createCommunicationProcess(0, sender, receiver, 2, message);

        Thread.sleep(5000);

        TestCase.assertEquals(CommunicationStatus.SUCCESS , communicationSimEntity.getCommunicationStatus(28.0));

        simEngine.stopScenario();

    }

    private static abstract class TestMessage implements Message<Object> {

        @Override
        public Object getMessageContent() {
            return null;
        }

        @Override
        public ISensorModel<?> getMessageSource() {
            return null;
        }

        @Override
        public ISensorModel<?> getMessageDest() {
            return null;
        }
    }


}
