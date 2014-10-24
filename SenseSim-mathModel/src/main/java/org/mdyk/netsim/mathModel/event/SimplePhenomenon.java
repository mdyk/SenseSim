package org.mdyk.netsim.mathModel.event;

import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.event.time.IPhenomenonTime;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimplePhenomenon implements IPhenomenonModel<GeoPosition> {

    private List<GeoPosition> region;
    // TODO: uwzględnienie wielu zdolności
    private Map<IPhenomenonTime , Object> values;
    private AbilityType abilityType;

    public SimplePhenomenon(Map<IPhenomenonTime, Object> values, AbilityType abilityType, List<GeoPosition> points) {
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
    public Object getPhenomenonValue(AbilityType ability, int time) {
        Set<IPhenomenonTime> timeSet = values.keySet();

        Object value = null;

        for(IPhenomenonTime timeRange : timeSet) {
            if(time >= timeRange.fromTime() && time <= timeRange.toTime()) {
                value = values.get(timeRange);
            }
        }

        return value;
    }

    @Override
    public boolean hasAbility(AbilityType ability) {
        if(ability.name().equals(abilityType.name())){
            return true;
        }
        return false;
    }
}
