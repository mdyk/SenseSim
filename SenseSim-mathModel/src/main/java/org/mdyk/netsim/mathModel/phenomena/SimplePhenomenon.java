package org.mdyk.netsim.mathModel.phenomena;

import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;
import org.mdyk.netsim.mathModel.phenomena.time.SimplePhenomenonTimeRange;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimplePhenomenon implements IPhenomenonModel<GeoPosition> {

    private List<GeoPosition> region;
    // TODO: uwzględnienie wielu zdolności
    private Map<IPhenomenonTimeRange, Object> values;
    private AbilityType abilityType;

    public SimplePhenomenon(Map<IPhenomenonTimeRange, Object> values, AbilityType abilityType, List<GeoPosition> points) {
        region = new LinkedList<>();

        for(GeoPosition position : points) {
            region.add(position);
        }

        this.values = values;
        this.abilityType = abilityType;

    }

    @Override
    public List<GeoPosition> getPhenomenonRegionPoints() {
        return region;
    }

    @Override
    // TODO: uwzględnienie wielu zdolności
    public PhenomenonValue getPhenomenonValue(AbilityType ability, double time) {
        Set<IPhenomenonTimeRange> timeSet = values.keySet();

        Object value = null;

        for(IPhenomenonTimeRange timeRange : timeSet) {

            if(timeRange.isInTime(time)) {
                value = values.get(timeRange);
            }
        }

        return new PhenomenonValue(time, value);
    }

    @Override
    public boolean hasAbility(AbilityType ability) {
        if(ability.name().equals(abilityType.name())){
            return true;
        }
        return false;
    }
}
