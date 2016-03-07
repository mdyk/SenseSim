package org.mdyk.netsim.logic.event;

import javafx.util.Pair;
import org.mdyk.netsim.mathModel.device.DeviceNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;


public class EventFactory {

    private EventFactory() {}

    public static InternalEvent createNewNodeEvent(DeviceNode node)  {
        return new InternalEvent(EventType.NEW_NODE, node);
    }

    public static InternalEvent createNodePositionChangedEvent(DeviceNode node)  {
        return new InternalEvent(EventType.NODE_POSITION_CHANGED, node);
    }

    // TODO: niekoniecznie musi to być PhenomenonModel<GeoPosition> (trzeba wprowadzić mapowanie typów przez Guice)
    public static InternalEvent createNewPhenomenonEvent(PhenomenonModel<GeoPosition> phenomenon)  {
        return new InternalEvent(EventType.PHENOMENON_CREATED, phenomenon);
    }

    public static InternalEvent startSenseEvent(DeviceNode node) {
        return new InternalEvent(EventType.NODE_START_SENSE, node);
    }


    public static InternalEvent endSenseEvent(DeviceNode node) {
        return new InternalEvent(EventType.NODE_END_SENSE, node);
    }

    public static InternalEvent startProgramExecutionEvent(int PID) {
        return new InternalEvent(EventType.START_PROGRAM_EXECUTION, PID);
    }

    public static InternalEvent endProgramExecutionEvent(int PID) {
        return new InternalEvent(EventType.END_PROGRAM_EXECUTION, PID);
    }

    public static InternalEvent loadProgram(Integer nodeId , String Code) {
        Pair<Integer, String> programCode = new Pair<>(nodeId,Code);
        return new InternalEvent(EventType.LOAD_PROGRAM, programCode);
    }
}
