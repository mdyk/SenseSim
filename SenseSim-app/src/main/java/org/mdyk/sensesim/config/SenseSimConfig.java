package org.mdyk.sensesim.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.mdyk.netsim.logic.network.DefaultWirelessChannel;
import org.mdyk.netsim.logic.network.NetworkManager;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.SensorNodeFactory;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.view.SenseSimView;
import org.mdyk.netsim.view.jfx.SenseSimJFXApp;
import org.mdyk.sensesim.simulation.engine.dissim.DisSimEngine;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.SensorNodeFactoryDisSim;


public class SenseSimConfig extends AbstractModule {

    @Override
    protected void configure() {
        bind(SenseSimView.class).to(SenseSimJFXApp.class);
        bind(WirelessChannel.class).to(DefaultWirelessChannel.class);
        bind(NetworkManager.class);
        install(new FactoryModuleBuilder().build(ScenarioFactory.class));

        bind(SimEngine.class).to(DisSimEngine.class);
        bind(SensorNodeFactory.class).to(SensorNodeFactoryDisSim.class);

    }

}
