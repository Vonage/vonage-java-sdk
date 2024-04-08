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
import java.util.UUID;

abstract class AudioRtcEvent extends EventWithBody<AudioRtcEvent.Body> {

    AudioRtcEvent() {}

    AudioRtcEvent(Builder<?, ?> builder) {
        super(builder);
        (body = new Body()).rtcId = builder.rtcId;
    }

    /**
     * Main body object for Audio events with {@code rtc_id}.
     */
    static class Body extends JsonableBaseObject {
        @JsonProperty("rtc_id") private UUID rtcId;
    }

    /**
     * Unique audio RTC identifier.
     *
     * @return The RTC ID, or {@code null} if unknown.
     */
    @JsonIgnore
    public UUID getRtcId() {
        return body != null ? body.rtcId : null;
    }

    @SuppressWarnings("unchecked")
    static abstract class Builder<E extends AudioRtcEvent,
            B extends Builder<? extends E, ? extends  B>>
            extends EventWithBody.Builder<E, Builder<E, B>> {

        private UUID rtcId;

        Builder(EventType type) {
            super(type);
        }

        /**
         * Unique audio RTC identifier.
         *
         * @param rtcId The RTC ID as a string.
         *
         * @return This builder.
         */
        public B rtcId(String rtcId) {
            return rtcId(UUID.fromString(rtcId));
        }

        /**
         * Unique audio RTC identifier.
         *
         * @param rtcId The RTC ID.
         *
         * @return This builder.
         */
        public B rtcId(UUID rtcId) {
            this.rtcId = rtcId;
            return (B) this;
        }
    }
}
