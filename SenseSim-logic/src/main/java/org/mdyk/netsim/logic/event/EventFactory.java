package org.mdyk.netsim.logic.event;

import javafx.util.Pair;
import org.mdyk.netsim.mathModel.sensor.SensorNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.phenomena.IPhenomenonModel;


public class EventFactory {

    private EventFactory() {}

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

    public static InternalEvent startSenseEvent(SensorNode node) {
        return new InternalEvent(EventType.NODE_START_SENSE, node);
    }


    public static InternalEvent endSenseEvent(SensorNode node) {
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
