package org.mdyk.sensesim.simulation.engine.dissim;

import com.google.common.eventbus.Subscribe;

import dissim.simspace.SimControlException;
import dissim.simspace.SimModel;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.network.NetworkManager;
import org.mdyk.netsim.logic.scenario.Scenario;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.EventsRoutedSensorNodeWrapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * Simulation engine based on DisSim framework
 */
//@PluginImplementation
@Singleton
public class DisSimEngine /*extends BasicSimContext*/ implements /*SimContextInterface,*/ SimEngine<EventsRoutedSensorNodeWrapper>, Runnable {

    private Logger logger = Logger.getLogger(DisSimEngine.class);

    @Inject
    private NetworkManager networkManager;

    @Inject
    private ScenarioFactory scenarioFactory;

    @Inject
    private Environment environment;

    private File scenarioXML;

    private List<EventsRoutedSensorNodeWrapper> sensorsList = new LinkedList<>();

    public DisSimEngine() {
//        super();
//        super("SenseSimContext", SimModel.getInstance());
        EventBusHolder.getEventBus().register(this);
    }

    @Override
    public void loadScenario(Scenario scenario) {
        Collection<List<ISensorModel>> sensorLists = scenario.scenarioSensors().values();

        for (List<ISensorModel> nodeList : sensorLists) {
            addNodes(nodeList);
        }
    }

    @Override
    public void runScenario() {
//        networkManager.runNodes();

        new Thread(this).start();

//        for(EventsRoutedSensorNodeWrapper wrapper : sensorsList) {
//            wrapper.startNode();
//        }
//        try {
//            SimModel.getInstance().startSimulation();
//        } catch (SimControlException e) {
//            logger.error(e.getMessage() ,e);
//        }
    }

    @Override
    public void pauseScenario() {
        networkManager.pauseNodes();
    }

    @Override
    public void resumeScenario() {
        networkManager.resumeNodes();
    }

    @Override
    public void stopScenario() {
        networkManager.stopNodes();
    }

    @Override
    public void addNode(EventsRoutedSensorNodeWrapper sensorNode) {
        networkManager.addNode(sensorNode);
    }

    private void addNodes(List<ISensorModel> nodesList) {
        for (ISensorModel sensorModel : nodesList) {
            addNode((EventsRoutedSensorNodeWrapper) sensorModel);
            sensorsList.add((EventsRoutedSensorNodeWrapper) sensorModel);
        }
    }

//    @Override
//    public void initContext() {
////        Scenario scenario = scenarioFactory.createXMLScenario(scenarioXML);
////        loadScenario(scenario);
//
//
//
//    }
//
//    @Override
//    public void stopContext() {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public boolean isReadyToASAPtimeFlow() {
//        return true;
//    }

//    @Override
//    public SimCalendarInterface getSimCalendar() {
//        return super.getSimCalendar();
//    }

//    @Override
//    public void clearContext() {
//        super.clearContext();
//    }

    @Subscribe
    public void processEvent(InternalEvent event) {
        logger.debug(">> processEvent");
        switch(event.getEventType()){
            case SIM_PAUSE_NODES:
                logger.debug("SIM_PAUSE_NODES event");
                this.pauseScenario();
                break;
            case SIM_RESUME_NODES:
                logger.debug("SIM_RESUME_NODES event");
                this.resumeScenario();
                break;
            case SIM_START_NODES:
                logger.debug("SIM_START_NODES event");
                this.runScenario();
                break;
            case SIM_STOP_NODES:
                logger.debug("SIM_STOP_NODES event");
                this.stopScenario();
                break;
            case SCENARIO_LOADED:
                logger.debug("SCENARIO_LOADED event");
                scenarioXML = (File) event.getPayload();
                Scenario scenario = scenarioFactory.createXMLScenario(scenarioXML);
                loadScenario(scenario);
                break;
        }
        logger.debug("<< processEvent");
    }

    @Override
    public void run() {
        for(EventsRoutedSensorNodeWrapper wrapper : sensorsList) {
            wrapper.startNode();
        }
        try {
            SimModel.getInstance().startSimulation();
        } catch (SimControlException e) {
            logger.error(e.getMessage() ,e);
        }
    }
}
