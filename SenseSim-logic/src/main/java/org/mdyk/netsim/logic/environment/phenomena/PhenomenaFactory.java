package org.mdyk.netsim.logic.environment.phenomena;

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

    /**
     * Creates simple phenomenon with given observation capability, region and values.
     * @param values
     *      map which holds phenomenon observations. Key are types of abilities and value are maps which hold timerange
     *      and values of the phenomenon for each ability
     * @param points
     *      list of points which defines spatial area of the phenomenon.
     * @return
     */
    @Deprecated
    PhenomenonModel<GeoPosition> createPhenomenon(Map<AbilityType , Map<IPhenomenonTimeRange, Object>> values, List<GeoPosition> points);

    PhenomenonModel<GeoPosition> createPhenomenon(String phenomenonName , Map<Class , Map<IPhenomenonTimeRange, ConfigurationSpace>> phenomenonValues, List<GeoPosition> points);

}
