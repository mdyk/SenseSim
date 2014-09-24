package org.mdyk.netsim.logic;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.mdyk.netsim.logic.network.DefaultWirelessChannel;
import org.mdyk.netsim.logic.network.NetworkManager;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.SensorNodeFactory;
import org.mdyk.netsim.logic.simEngine.thread.SensorNodeFactoryThread;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.logic.simEngine.thread.ThreadSimEngine;

/**
 *  Configuration for Logic module
 */
public class LogicModule extends AbstractModule {

    @Override
    protected void configure() {
//        bind(WirelessChannel.class).to(DefaultWirelessChannel.class);
//        bind(NetworkManager.class);
//        bind(SensorNodeFactory.class).to(SensorNodeFactoryThread.class);
//        install(new FactoryModuleBuilder().build(ScenarioFactory.class));
    }

}
