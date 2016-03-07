package org.mdyk.sensesim.simulation.engine.dissim.phenomena;

import com.google.inject.Singleton;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.SimplePhenomenon;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;
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
    public PhenomenonModel<GeoPosition> createPhenomenon(Map<AbilityType , Map<IPhenomenonTimeRange, Object>> values, List<GeoPosition> points) {
        return new SimplePhenomenon(values, points);
    }
}
