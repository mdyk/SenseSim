package org.mdyk.netsim.logic.simEngine.thread;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.network.NetworkManager;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.scenario.Scenario;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of sim engine based on threads
 */
@Singleton
public class ThreadSimEngine /*implements SimEngine<DeviceNodeThread<?,?>>*/ {

    private static final Logger LOG = Logger.getLogger(ThreadSimEngine.class);

    @Inject
    private NetworkManager networkManager;

    @Inject
    private ScenarioFactory scenarioFactory;

    @Inject
    private Environment environment;

    public ThreadSimEngine() {
        EventBusHolder.getEventBus().register(this);
    }


//    @Override
    public void loadScenario(Scenario scenario) {

     /*       Collection<List<IDeviceModel>> sensorLists = scenario.scenarioSensors().values();

            for (List<IDeviceModel> nodeList : sensorLists) {
                addNodes(nodeList);
            }

        environment.loadPhenomena(scenario.getPhenomena());       */

    }

//    @Override
//    public void runScenario() {
//        networkManager.runNodes();
//    }
//
//    @Override
//    public void pauseScenario() {
//        networkManager.pauseNodes();
//    }
//
//    @Override
//    public void resumeScenario() {
//        networkManager.resumeNodes();
//    }
//
//    @Override
//    public void stopScenario() {
//        networkManager.stopNodes();
//    }

//    @Override
//    public void addNode(DeviceNodeThread sensorNode) {
//        networkManager.addNode(sensorNode);
//    }

//    private void addNodes(List<IDeviceModel> nodesList) {
//        for (IDeviceModel sensorModel : nodesList) {
//            addNode((DeviceNodeThread) sensorModel);
//        }
//    }
//
//    @Subscribe
//    public void processEvent(InternalEvent event) {
//        LOG.debug(">> processEvent");
//        switch(event.getEventType()){
//            case SIM_PAUSE_NODES:
//                LOG.debug("SIM_PAUSE_NODES event");
//                this.pauseScenario();
//                break;
//            case SIM_RESUME_NODES:
//                LOG.debug("SIM_RESUME_NODES event");
//                this.resumeScenario();
//                break;
//            case SIM_START_NODES:
//                LOG.debug("SIM_START_NODES event");
//                this.runScenario();
//                break;
//            case SIM_STOP_NODES:
//                LOG.debug("SIM_STOP_NODES event");
//                this.stopScenario();
//                break;
//            case SCENARIO_LOADED:
//                LOG.debug("SCENARIO_LOADED event");
//                File scenarioXML = (File) event.getPayload();
//                Scenario scenario = scenarioFactory.createXMLScenario(scenarioXML);
//                loadScenario(scenario);
//                break;
//        }
//        LOG.debug("<< processEvent");
//    }

}
