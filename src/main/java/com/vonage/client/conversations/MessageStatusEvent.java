/*
 *   Copyright 2024 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents a Message Status event.
 */
public abstract class MessageStatusEvent extends Event<MessageStatusEvent.Body> {

    MessageStatusEvent() {}

    MessageStatusEvent(Builder<?, ?> builder) {
        super(builder);
    }

    /**
     * Main body object for Message Status events.
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body extends JsonableBaseObject {
        private Integer eventId;

        /**
         * ID of the event.
         *
         * @return The event ID as an Integer.
         */
        @JsonProperty("event_id")
        public Integer getEventId() {
            return eventId;
        }
    }

    static abstract class Builder<E extends MessageStatusEvent,
            B extends Builder<? extends E, ? extends  B>>
            extends Event.Builder<Body, MessageStatusEvent, Builder<E, B>> {

        Builder(EventType type) {
            super(type);
        }
    }
}
