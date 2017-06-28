package org.mdyk.netsim.logic.environment.phenomena;

import org.mdyk.netsim.logic.node.Device;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;

import java.util.List;
import java.util.Map;

/**
 * Interface for creating new phenomena. Implementation
 * should be provided by simulation engine
 */
public interface PhenomenaFactory {

    @Deprecated
    PhenomenonModel<GeoPosition> createPhenomenon(Map<AbilityType , Map<IPhenomenonTimeRange, Object>> values, List<GeoPosition> points);

    PhenomenonModel<GeoPosition> createPhenomenon(String phenomenonName , Map<Class , Map<IPhenomenonTimeRange, ConfigurationSpace>> phenomenonValues, List<GeoPosition> points, boolean infinite);

    /**
     * Creates phenomenon which is attached to a specific device.
     * @param phenomenonName
     *      name of the phenomenon
     * @param phenomenonValues
     *      values of the phenomenon
     * @param attachedTo
     *      reference to a device which is associated with phenomenon
     * @return
     *      phenomenon attached to a device.
     */
    PhenomenonModel<GeoPosition> createPhenomenon(String phenomenonName , Map<Class , Map<IPhenomenonTimeRange, ConfigurationSpace>> phenomenonValues, Device attachedTo, boolean infinite);

}
