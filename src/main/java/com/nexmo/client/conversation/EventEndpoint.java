package com.nexmo.client.conversation;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;

public class EventEndpoint {
    private final CreateEventMethod createEvent;
    private final GetEventMethod getEvent;
    private final ListEventsMethod listEvents;
    private final DeleteEventMethod deleteEvents;

    EventEndpoint(HttpWrapper wrapper){
        this.createEvent = new CreateEventMethod(wrapper);
        this.getEvent = new GetEventMethod(wrapper);
        this.listEvents = new ListEventsMethod(wrapper);
        this.deleteEvents = new DeleteEventMethod(wrapper);
    }

    public Event post(Event request) throws NexmoClientException {
        return this.createEvent.execute(request);
    }

    public Event get(SpecificEventRequest request) throws NexmoClientException{
        return this.getEvent.execute(request);
    }

    public EventPage get(ListEventRequest request) throws NexmoClientException{
        return this.listEvents.execute(request);
    }

    public Integer delete(SpecificEventRequest request) throws  NexmoClientException{
        return this.deleteEvents.execute(request);
    }
}
