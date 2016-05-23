package org.mdyk.netsim.mathModel.phenomena;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.temperature.TemperatureConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimplePhenomenon implements PhenomenonModel<GeoPosition> {

    private static final Logger LOG = Logger.getLogger(SimplePhenomenon.class);

    private String phenomenonName;
    private List<GeoPosition> region;
    private Map<Class , Map<IPhenomenonTimeRange, ConfigurationSpace>> phenomenonValues;

    @Deprecated
    private Map<AbilityType , Map<IPhenomenonTimeRange, Object>> values;


    public SimplePhenomenon(String phenomenonName , Map<Class , Map<IPhenomenonTimeRange, ConfigurationSpace>> phenomenonValues, List<GeoPosition> points) {
        region = new LinkedList<>();
        for(GeoPosition position : points) {
            region.add(position);
        }
        this.phenomenonName = phenomenonName;
        this.phenomenonValues = phenomenonValues;
    }

    @Deprecated
    public SimplePhenomenon(Map<AbilityType , Map<IPhenomenonTimeRange, Object>> values, List<GeoPosition> points) {
        region = new LinkedList<>();
        for(GeoPosition position : points) {
            region.add(position);
        }
        this.values = values;
    }

    @Override
    public String getName() {
        return phenomenonName;
    }

    @Override
    public List<GeoPosition> getPhenomenonRegionPoints() {
        return region;
    }

    @Override
    @Deprecated
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
    @Deprecated
    public boolean hasAbility(AbilityType ability) {
        return values.containsKey(ability);
    }

    @Override
    public ConfigurationSpace getEventValue(Class configurationSpaceClass , double time) {
        LOG.trace(">> getEventValue[class=" + configurationSpaceClass +"]");
        if(!this.phenomenonValues.containsKey(configurationSpaceClass)) {
            return null;
        }

        ConfigurationSpace phenomenonValue = null;
        Map<IPhenomenonTimeRange, ConfigurationSpace> values = phenomenonValues.get(configurationSpaceClass);

        for(IPhenomenonTimeRange timeRange : values.keySet()) {
            if(timeRange.isInTime(time)) {
                phenomenonValue = values.get(timeRange);
            }
        }

        LOG.trace("<< getEventValue");
        return phenomenonValue;
    }

    @Override
    public boolean hasConfigurationSpace(Class configurationSpaceClass) {
        return phenomenonValues.containsKey(configurationSpaceClass);
    }
}
