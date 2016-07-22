package org.mdyk.sensesim.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.network.DefaultWirelessChannel;
import org.mdyk.netsim.logic.network.NetworkManager;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.*;
import org.mdyk.netsim.logic.node.program.owl.OWLMiddlewareFactory;
import org.mdyk.netsim.logic.node.statistics.DefaultStatisticsFactory;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.logic.sensor.DefaultSensorFactory;
import org.mdyk.netsim.logic.sensor.SensorFactory;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.view.SenseSimView;
import org.mdyk.netsim.view.jfx.SenseSimJFXApp;
import org.mdyk.sensesim.simulation.engine.dissim.DisSimEngine;
import org.mdyk.sensesim.simulation.engine.dissim.communication.DisSimCommunicationProcessFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimAPIFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimEntityFactory;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.DisSimSensorsLogicFactory;
import org.mdyk.sensesim.simulation.engine.dissim.phenomena.DisSimPhenomenaFactory;


public class SenseSimConfig extends AbstractModule {

    @Override
    protected void configure() {
        bind(SenseSimView.class).to(SenseSimJFXApp.class);
        bind(WirelessChannel.class).to(DefaultWirelessChannel.class);
        bind(NetworkManager.class);
        install(new FactoryModuleBuilder().build(ScenarioFactory.class));

        //TODO zebranie w jedną konfigurację dla silnika symulacyjnego
        bind(SimEngine.class).to(DisSimEngine.class);
        bind(DeviceLogicFactory.class).to(DisSimSensorsLogicFactory.class);
        bind(SimEntityFactory.class).to(DisSimEntityFactory.class);
        bind(APIFactory.class).to(DisSimAPIFactory.class);
        bind(SensorFactory.class).to(DefaultSensorFactory.class);
        bind(PhenomenaFactory.class).to(DisSimPhenomenaFactory.class);
        bind(MiddlewareFactory.class).to(OWLMiddlewareFactory.class);
        bind(DeviceStatisticsFactory.class).to(DefaultStatisticsFactory.class);
        bind(CommunicationProcessFactory.class).to(DisSimCommunicationProcessFactory.class);
    }

}
