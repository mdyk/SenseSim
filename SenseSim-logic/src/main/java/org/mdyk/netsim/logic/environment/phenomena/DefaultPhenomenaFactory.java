package org.mdyk.netsim.logic.environment.phenomena;

import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.event.IPhenomenonModel;
import org.mdyk.netsim.mathModel.event.SimplePhenomenon;
import org.mdyk.netsim.mathModel.event.time.IPhenomenonTime;

import java.util.List;
import java.util.Map;


public class DefaultPhenomenaFactory implements PhenomenaFactory {
    @Override
    public IPhenomenonModel<GeoPosition> createPhenomenon(Map<IPhenomenonTime, Object> values, AbilityType abilityType, List<GeoPosition> points) {
        return new SimplePhenomenon(values,abilityType,points);
    }
}
