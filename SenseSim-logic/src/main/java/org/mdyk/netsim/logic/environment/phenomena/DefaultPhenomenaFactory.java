package org.mdyk.netsim.logic.environment.phenomena;

import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.IPhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.SimplePhenomenon;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;

import java.util.List;
import java.util.Map;


public class DefaultPhenomenaFactory implements PhenomenaFactory {
    @Override
    public IPhenomenonModel<GeoPosition> createPhenomenon(Map<AbilityType , Map<IPhenomenonTimeRange, Object>> values, List<GeoPosition> points) {
        return new SimplePhenomenon(values, points);
    }
}
