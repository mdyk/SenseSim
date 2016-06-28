package org.mdyk.netsim.logic.scenario;

import org.mdyk.netsim.logic.node.Device;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;

import java.util.List;
import java.util.Map;

/**
 * Interface for scenario configuration
 */
public interface Scenario {

    void initialize();

    String scenarioName();

    List<Device> scenarioDevices();

    List<PhenomenonModel<GeoPosition>> getPhenomena();

    List<GeoPosition> getScenarioRegionPoints();

}
