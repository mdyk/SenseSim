package org.mdyk.sensesim.simulation.engine.dissim;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import dissim.simspace.core.SimControlException;
import dissim.simspace.core.SimModel;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.network.NetworkManager;
import org.mdyk.netsim.logic.node.Device;
import org.mdyk.netsim.logic.scenario.Scenario;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.sensesim.simulation.engine.dissim.phenomena.events.PhenomenonSimEntity;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Simulation engine based on DisSim framework
 */
@Singleton
public class DisSimEngine implements SimEngine, Runnable {

    private static final Logger LOG = Logger.getLogger(DisSimEngine.class);

    @Inject
    private NetworkManager networkManager;

    @Inject
    private ScenarioFactory scenarioFactory;

    @Inject
    private Environment environment;

    private File scenarioXML;

    private List<Device> deviceList = new ArrayList<>();

    private List<PhenomenonSimEntity> phenomenaList = new ArrayList<>();

    public DisSimEngine() {
        EventBusHolder.getEventBus().register(this);
    }

    @Override
    public void loadScenario(Scenario scenario) {
        Collection<List<Device>> sensorLists = scenario.scenarioSensors().values();

        for (List<Device> nodeList : sensorLists) {
            addNodes(nodeList);
        }

        List<PhenomenonModel<GeoPosition>> phenomenaModels = scenario.getPhenomena();
        for (PhenomenonModel<GeoPosition> model : phenomenaModels) {
            phenomenaList.add(new PhenomenonSimEntity(model));
        }

        environment.loadPhenomena(phenomenaModels);

        EventBusHolder.post(EventFactory.createScenarioLoadedEvent(scenario));

    }

    @Override
    public void runScenario() {
        new Thread(this).start();
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
        try {
            SimModel.getInstance().stopSimulation();
        } catch (SimControlException e) {
            LOG.error(e.getMessage(),e);
            throw new RuntimeException("Unable to stop DisSim", e);
        }
    }

    @Override
    public void addNode(Device deviceNode) {
        networkManager.addNode(deviceNode.getDeviceLogic());
        deviceList.add(deviceNode);
    }

    private void addNodes(List<Device> nodesList) {
        for (Device deviceModel : nodesList) {
            addNode(deviceModel);
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void processEvent(InternalEvent event) {
        LOG.debug(">> processEvent");
        try {
            switch(event.getEventType()){
                case SIM_PAUSE_NODES:
                    LOG.debug("SIM_PAUSE_NODES event");
                    this.pauseScenario();
                    break;
                case SIM_RESUME_NODES:
                    LOG.debug("SIM_RESUME_NODES event");
                    this.resumeScenario();
                    break;
                case SIM_START_NODES:
                    LOG.debug("SIM_START_NODES event");
                    this.runScenario();
                    break;
                case SIM_STOP_NODES:
                    LOG.debug("SIM_STOP_NODES event");
                    this.stopScenario();
                    break;
                case SCENARIO_FILE_LOADED:
                    LOG.debug("SCENARIO_FILE_LOADED event");
                    scenarioXML = (File) event.getPayload();
                    Scenario scenario = scenarioFactory.createXMLScenario(scenarioXML);
                    loadScenario(scenario);
                    break;
                case SCENARIO_FILE_RELOADED:
                    LOG.debug("SCENARIO_FILE_RELOADED event");
                    scenarioXML = (File) event.getPayload();
                    clearScenario();
                    loadScenario(scenarioFactory.createXMLScenario(scenarioXML));
                    break;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
        LOG.debug("<< processEvent");
    }

    private void clearScenario() {
        try {
            SimModel.getInstance().stopSimulation();
            phenomenaList.clear();
            deviceList.clear();
            environment.clearPhenomena();
            networkManager.stopNodes();
            networkManager.clearNodes();

        } catch (SimControlException e) {
            throw new RuntimeException(e.getMessage() , e);
        }
    }

    @Override
    public void run() {
        for(Device wrapper : deviceList) {
            wrapper.getDeviceLogic().startNode();
        }
        try {
            SimModel.getInstance().startSimulation();
        } catch (SimControlException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }
}
