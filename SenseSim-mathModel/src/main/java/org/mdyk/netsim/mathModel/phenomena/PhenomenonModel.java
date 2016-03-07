package org.mdyk.netsim.mathModel.phenomena;

import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;

import java.util.List;

/**
 * Description of event based on mathematical mathModel
 */
public interface PhenomenonModel<P extends Position> {

    /**
     * Returns name of the phenomenon
     * @return
     *      name of the phenomenon.
     */
    String getName();

    /**
     * Returns list of points (described by position) which
     * are vertex of event region.
     * @return
     *      list of vertex
     */
    List<P> getPhenomenonRegionPoints();

    /**
     * Returns value of the event with given ability and time.
     * @return
     *      array which represents phenomenon value for given time
     */
    @Deprecated
    PhenomenonValue getPhenomenonValue(AbilityType ability, double time);

    @Deprecated
    boolean hasAbility(AbilityType ability);

    /**
     * Returns value of the event.
     * @param configurationSpaceClass
     *      class of the configuration space
     * @return
     *      value of the phenomenon for given configuration class
     */
    ConfigurationSpace getEventValue(Class configurationSpaceClass , double time);

}
