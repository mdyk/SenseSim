package org.mdyk.netsim.logic.environment.phenomena;

import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.IPhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;

import java.util.List;
import java.util.Map;

/**
 * Interface for creating new phenomena. Implementation
 * should be provided by simulation engine
 */
public interface PhenomenaFactory {

    /**
     * Creates simple phenomenon with given observation capability, region and values.
     * @param values
     *      map which holds phenomenon observations (values) indexed by time (keys).
     * @param abilityType
     *      type of observation capability {@link AbilityType}.
     * @param points
     *      list of points which defines spatial area of the phenomenon.
     * @return
     */
    public IPhenomenonModel<GeoPosition> createPhenomenon(Map<IPhenomenonTimeRange, Object> values, AbilityType abilityType, List<GeoPosition> points);

}
