package org.mdyk.netsim.logic.environment;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.Functions;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.IPhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;

import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents environment in which sensors works
 */
@Singleton
public class Environment {

    private static final Logger LOG = Logger.getLogger(Environment.class);

    private List<IPhenomenonModel<GeoPosition>> phenomena = new LinkedList<>();

    public Environment() {
        EventBusHolder.getEventBus().register(this);
    }

    /**
     * Returns value of the observed phenomenon
     * @return
     *      value of the phenomenon. Null if no value is present.
     */
    public PhenomenonValue getEventValue(GeoPosition position, double time, AbilityType ability) {
        LOG.trace(">>> getEventValue [position=" + position + ", time=" + time + ", ability=" + ability + "]");
        PhenomenonValue retVal = null;

        for(IPhenomenonModel event : phenomena) {
            if(event.hasAbility(ability)) {
                retVal = event.getPhenomenonValue(ability, time);
            }
        }

        LOG.trace("<<< getEventValue");
        return retVal;
    }

    // TODO pełna obsługa
    public void startEvent(IPhenomenonModel<GeoPosition> phenomenon) {
        EventBusHolder.getEventBus().post(EventFactory.createNewPhenomenonEvent(phenomenon));
    }

    // TODO tymczasowe
    public boolean isNodeInEventRegion(GeoPosition position) {
        for(IPhenomenonModel event : phenomena) {
            if(Functions.isPointInRegion(position,event.getPhenomenonRegionPoints())) {
                return true;
            }
        }
        return false;
    }

    public void loadPhenomena(List<IPhenomenonModel<GeoPosition>> phenomena) {
        LOG.trace(">>> loadEvents");

        LOG.info("Events size: " + phenomena.size());
        this.phenomena = phenomena;

        for(IPhenomenonModel phenomenon : phenomena) {
            startEvent(phenomenon);
        }

        LOG.trace("<<< loadEvents");
    }

}
