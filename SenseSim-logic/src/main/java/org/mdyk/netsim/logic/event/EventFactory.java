package org.mdyk.netsim.logic.event;

import org.mdyk.netsim.logic.node.SensorNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.phenomena.IPhenomenonModel;


public class EventFactory {

    public static InternalEvent createNewNodeEvent(SensorNode node)  {
        return new InternalEvent(EventType.NEW_NODE, node);
    }

    public static InternalEvent createNodePositionChangedEvent(SensorNode node)  {
        return new InternalEvent(EventType.NODE_POSITION_CHANGED, node);
    }

    // TODO: niekoniecznie musi to być IPhenomenonModel<GeoPosition> (trzeba wprowadzić mapowanie typów przez Guice)
    public static InternalEvent createNewPhenomenonEvent(IPhenomenonModel<GeoPosition> phenomenon)  {
        return new InternalEvent(EventType.PHENOMENON_CREATED, phenomenon);
    }

}
