package org.mdyk.netsim.logic.event;


public class InternalEvent {

    private EventType eventType;
    private Object payload;

    public InternalEvent(EventType type, Object payload){
        this.payload = payload;
        this.eventType = type;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Object getPayload() {
        return payload;
    }
}
