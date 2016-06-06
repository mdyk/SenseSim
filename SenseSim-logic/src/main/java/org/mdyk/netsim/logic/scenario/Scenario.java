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

    String scenarioName();

    /**
     * Returns map with sensors.
     * @return
     *      map where keys are class types of nodes and values are
     *      lists of nodes with desired class.
     */
    Map<Class, List<Device>> scenarioSensors();

    List<PhenomenonModel<GeoPosition>> getPhenomena();

    List<GeoPosition> getScenarioRegionPoints();

}
