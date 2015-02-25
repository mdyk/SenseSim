package org.mdyk.sensesim.simulation.engine.dissim;

import com.google.common.eventbus.Subscribe;

import dissim.simspace.core.SimControlException;
import dissim.simspace.core.SimModel;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.network.NetworkManager;
import org.mdyk.netsim.logic.scenario.Scenario;
import org.mdyk.netsim.logic.scenario.ScenarioFactory;
import org.mdyk.netsim.logic.simEngine.SimEngine;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.phenomena.IPhenomenonModel;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimProgrammableNode;
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
public class DisSimEngine implements SimEngine<DisSimProgrammableNode>, Runnable {

    private static final Logger LOG = Logger.getLogger(DisSimEngine.class);

    @Inject
    private NetworkManager networkManager;

    @Inject
    private ScenarioFactory scenarioFactory;

    @Inject
    private Environment environment;

    private File scenarioXML;

    private List<DisSimProgrammableNode> sensorsList = new ArrayList<>();

    private List<PhenomenonSimEntity> phenomenaList = new ArrayList<>();

    public DisSimEngine() {
        EventBusHolder.getEventBus().register(this);
    }

    @Override
    public void loadScenario(Scenario scenario) {
        Collection<List<ISensorModel>> sensorLists = scenario.scenarioSensors().values();

        for (List<ISensorModel> nodeList : sensorLists) {
            addNodes(nodeList);
        }

        List<IPhenomenonModel<GeoPosition>> phenomenaModels = scenario.getPhenomena();
        for (IPhenomenonModel<GeoPosition> model : phenomenaModels) {
            phenomenaList.add(new PhenomenonSimEntity(model));
        }

        environment.loadPhenomena(phenomenaModels);
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
        SimModel.getInstance().stopSimulation();
    }

    @Override
    public void addNode(DisSimProgrammableNode sensorNode) {
        networkManager.addNode(sensorNode);
        sensorsList.add(sensorNode);
    }

    private void addNodes(List<ISensorModel> nodesList) {
        for (ISensorModel sensorModel : nodesList) {
            addNode((DisSimProgrammableNode) sensorModel);
        }
    }

    @Subscribe
    public void processEvent(InternalEvent event) {
        LOG.debug(">> processEvent");
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
            case SCENARIO_LOADED:
                LOG.debug("SCENARIO_LOADED event");
                scenarioXML = (File) event.getPayload();
                Scenario scenario = scenarioFactory.createXMLScenario(scenarioXML);
                loadScenario(scenario);
                break;
        }
        LOG.debug("<< processEvent");
    }

    @Override
    public void run() {
        for(DisSimProgrammableNode wrapper : sensorsList) {
            wrapper.startNode();
        }
        try {
            SimModel.getInstance().startSimulation();
        } catch (SimControlException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
