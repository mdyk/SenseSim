package org.mdyk.netsim.logic.node.statistics.event;


public class StatisticsEvent {

    public enum EventType {
        COMM_PROC_UPDATE,
        PROGRAM_LOADED,
        PROGRAM_UPDATED,
        GUI_UPDATE_STATISTICS;
    }

    private Object payload;
    private EventType eventType;

    public StatisticsEvent(EventType eventType, Object payload) {
        this.payload = payload;
        this.eventType = eventType;
    }

    public Object getPayload() {
        return payload;
    }

    public EventType getEventType() {
        return eventType;
    }
}
