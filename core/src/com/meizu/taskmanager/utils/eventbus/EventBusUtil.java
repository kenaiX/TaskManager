package com.meizu.taskmanager.utils.eventbus;

import com.google.common.eventbus.EventBus;

public class EventBusUtil {
    protected static EventBus eventBus;

    public synchronized static EventBus getDefaultEventBus() {
        if (eventBus == null) {
            eventBus = new EventBus("taskmanager");
        }
        return eventBus;
    }
}
