package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmbeddedEvents {
    private Event[] events;

    @JsonProperty("events")
    public Event[] getEvents() {
        return events;
    }

    @JsonProperty("events")
    public void setEvents(Event[] events) {
        this.events = events;
    }
}
