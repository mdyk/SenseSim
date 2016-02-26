package org.mdyk.netsim.logic.simEngine;

import org.mdyk.netsim.logic.node.Device;
import org.mdyk.netsim.logic.scenario.Scenario;

/**
 * Interface to simulation engine
 */
public interface SimEngine {

    public void loadScenario(Scenario scenario);

    public void runScenario();

    public void pauseScenario();

    public void resumeScenario();

    public void stopScenario();

    public void addNode(Device deviceNode);

}
