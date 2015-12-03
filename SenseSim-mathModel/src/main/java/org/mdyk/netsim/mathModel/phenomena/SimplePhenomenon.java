package org.mdyk.netsim.mathModel.phenomena;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimplePhenomenon implements IPhenomenonModel<GeoPosition> {

    private static final Logger LOG = Logger.getLogger(SimplePhenomenon.class);

    private List<GeoPosition> region;
    private Map<AbilityType , Map<IPhenomenonTimeRange, Object>> values;

    public SimplePhenomenon(Map<AbilityType , Map<IPhenomenonTimeRange, Object>> values, List<GeoPosition> points) {
        region = new LinkedList<>();
        for(GeoPosition position : points) {
            region.add(position);
        }
        this.values = values;
    }

    @Override
    public List<GeoPosition> getPhenomenonRegionPoints() {
        return region;
    }

    @Override
    public PhenomenonValue getPhenomenonValue(AbilityType ability, double time) {

        if(!hasAbility(ability)){
            return null;
        }

        Map<IPhenomenonTimeRange, Object> valueMap = values.get(ability);

        Object value = null;

        for(IPhenomenonTimeRange timeRange : valueMap.keySet()) {

            if(timeRange.isInTime(time)) {
                value = valueMap.get(timeRange);
            }
        }

        return new PhenomenonValue(time, value);
    }

    @Override
    public boolean hasAbility(AbilityType ability) {
        return values.containsKey(ability);
    }
}
