package org.mdyk.netsim.logic.scenario;

import org.mdyk.netsim.logic.node.Sensor;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.phenomena.IPhenomenonModel;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

import java.util.List;
import java.util.Map;

/**
 * Interface for scenario configuration
 */
// TODO generalizacja pod kątem położenia
public interface Scenario {

    public String scenarioName();

    /**
     * Returns map with sensors.
     * @return
     *      map where keys are class types of nodes and values are
     *      lists of nodes with desired class.
     */
    public Map<Class, List<Sensor>> scenarioSensors();

    public List<IPhenomenonModel<GeoPosition>> getPhenomena();

}
