package org.mdyk.sensesim.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.mdyk.netsim.logic.scenario.Scenario;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.view.SenseSimView;
import org.mdyk.netsim.logic.LogicModule;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.logic.scenario.xml.XMLScenarioLoadException;
import org.mdyk.netsim.mathModel.MathModule;
import org.mdyk.sensesim.config.SenseSimConfig;
import sensesim.integration.mcop.MCopPluginFactory;
import webservices.Platform;

import java.io.File;

/**
 * Main class which runs application
 */
public class SenseSim {

    public static void main(String[] args) throws XMLScenarioLoadException {

        Injector injector = Guice.createInjector(new SenseSimConfig(), new LogicModule() , new MathModule());
        SimEngine simEngine =  injector.getInstance(SimEngine.class);



        SenseSimView senseSimView = injector.getInstance(SenseSimView.class);
        senseSimView.show();

        // Scenario file
        if(args.length == 1 ) {
            String scenarioFilePath = args[0];
            ScenarioFactory scenarioFactory = injector.getInstance(ScenarioFactory.class);
            Scenario scenario = scenarioFactory.createXMLScenario(new File(scenarioFilePath));
            simEngine.loadScenario(scenario);
        }
        
    }
}
