package org.mdyk.netsim.logic.event;

import com.google.common.eventbus.EventBus;


public class EventBusHolder {

    private static EventBus bus;

    static {
        bus = new EventBus();
    }

    public static EventBus getEventBus() {
        return bus;
    }

    public static void post(InternalEvent internalEvent) {
        bus.post(internalEvent);
    }

    public static void post(EventType eventType, Object payload) {
        InternalEvent internalEvent = new InternalEvent(eventType, payload);
        bus.post(internalEvent);
    }

}
