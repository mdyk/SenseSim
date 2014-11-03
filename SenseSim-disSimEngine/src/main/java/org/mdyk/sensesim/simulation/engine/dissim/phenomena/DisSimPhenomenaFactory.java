package org.mdyk.sensesim.simulation.engine.dissim.phenomena;

import com.google.inject.Singleton;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.event.IPhenomenonModel;
import org.mdyk.netsim.mathModel.event.time.IPhenomenonTime;
import org.mdyk.sensesim.simulation.engine.dissim.DisSimEngine;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;


@Singleton
public class DisSimPhenomenaFactory implements PhenomenaFactory {

    @Inject
    private Environment environment;

    @Inject
    private WirelessChannel wirelessChannel;

    @Inject
    private DisSimEngine disSimEngine;

    @Override
    public IPhenomenonModel<GeoPosition> createPhenomenon(Map<IPhenomenonTime, Object> values, AbilityType abilityType, List<GeoPosition> points) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
