package org.mdyk.netsim.mathModel.event;

import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;

import java.util.List;

/**
 * Description of event based on mathematical mathModel
 */
public interface IPhenomenonModel<P extends Position> {

    /**
     * Returns list of points (described by position) which
     * are vertex of event region.
     * @return
     *      list of vertex
     */
    public List<P> getPhenomenonRegionPoints();

    /**
     * Returns value of the event with given ability and time.
     * @return
     *      array which represents phenomenon value for given time
     */
    public Object getPhenomenonValue(AbilityType ability, double time);

    public boolean hasAbility(AbilityType ability);

}
