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

/**
 * Represents an {@link EventType#AUDIO_SAY_STOP} event.
 */
public final class AudioSayStopEvent extends EventWithBody<AudioSayStopEvent.Body> {

    AudioSayStopEvent() {}

    private AudioSayStopEvent(Builder builder) {
        super(builder);
        (body = new Body()).sayId = builder.sayId;
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Body extends JsonableBaseObject {
        @JsonProperty("say_id") private UUID sayId;
    }

    /**
     *
     * @return
     */
    @JsonIgnore
    public UUID getSayId() {
        return body != null ? body.sayId : null;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends EventWithBody.Builder<AudioSayStopEvent, Builder> {
        private UUID sayId;

        Builder() {
            super(EventType.AUDIO_RECORD_STOP);
        }

        /**
         *
         * @param sayId
         *
         * @return This builder.
         */
        public Builder sayId(String sayId) {
            this.sayId = UUID.fromString(sayId);
            return this;
        }

        @Override
        public AudioSayStopEvent build() {
            return new AudioSayStopEvent(this);
        }
    }
}
