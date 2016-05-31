package org.mdyk.netsim.logic.simEngine;

import org.mdyk.netsim.logic.node.Device;
import org.mdyk.netsim.logic.scenario.Scenario;

import java.util.List;

/**
 * Interface to simulation engine
 */
public interface SimEngine {

    void loadScenario(Scenario scenario);

    void runScenario();

    void pauseScenario();

    void resumeScenario();

    void stopScenario();

    void addNode(Device deviceNode);

    List<Device> getDeviceList();

}
