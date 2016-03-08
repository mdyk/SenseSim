package org.mdyk.sensesim.simulation.engine.dissim.phenomena;

import com.google.inject.Singleton;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.SimplePhenomenon;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;

import java.util.List;
import java.util.Map;


@Singleton
public class DisSimPhenomenaFactory implements PhenomenaFactory {

    @Override
    public PhenomenonModel<GeoPosition> createPhenomenon(Map<AbilityType , Map<IPhenomenonTimeRange, Object>> values, List<GeoPosition> points) {
        return new SimplePhenomenon(values, points);
    }

    @Override
    public PhenomenonModel<GeoPosition> createPhenomenon(String phenomenonName, Map<Class, Map<IPhenomenonTimeRange, ConfigurationSpace>> phenomenonValues, List<GeoPosition> points) {
        return new SimplePhenomenon(phenomenonName,phenomenonValues,points);
    }
}