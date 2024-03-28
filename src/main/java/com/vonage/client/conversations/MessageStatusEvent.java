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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

abstract class MessageStatusEvent extends EventWithBody<MessageStatusEvent.Body> {

    MessageStatusEvent() {}

    MessageStatusEvent(Builder<?, ?> builder) {
        super(builder);
        (body = new Body()).eventId = builder.eventId;
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Body extends JsonableBaseObject {
        @JsonProperty("event_id") private Integer eventId;
    }

    /**
     * ID of the event.
     *
     * @return The event ID as an Integer.
     */
    @JsonIgnore
    public Integer getEventId() {
        return body != null ? body.eventId : null;
    }

    @SuppressWarnings("unchecked")
    static abstract class Builder<E extends MessageStatusEvent,
            B extends Builder<? extends E, ? extends  B>>
            extends EventWithBody.Builder<E, Builder<E, B>> {

        private Integer eventId;

        Builder(EventType type) {
            super(type);
        }

        /**
         * ID of the event.
         *
         * @param eventId The event ID as an Integer.
         * @return This builder.
         */
        public B eventId(int eventId) {
            this.eventId = eventId;
            return (B) this;
        }
    }
}
