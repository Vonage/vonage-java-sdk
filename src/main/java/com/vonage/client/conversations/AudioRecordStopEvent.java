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
 * Represents an {@link EventType#AUDIO_RECORD_STOP} event.
 */
public final class AudioRecordStopEvent extends EventWithBody<AudioRecordStopEvent.Body> {

    private AudioRecordStopEvent() {}

    private AudioRecordStopEvent(Builder builder) {
        super(builder);
        (body = new Body()).recordId = builder.recordId;
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Body extends JsonableBaseObject {
        @JsonProperty("record_id") private UUID recordId;
    }

    /**
     * Unique recording identifier.
     *
     * @return The recording ID, or {@code null} if unknown.
     */
    @JsonIgnore
    public UUID getRecordId() {
        return body != null ? body.recordId : null;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends EventWithBody.Builder<AudioRecordStopEvent, Builder> {
        private UUID recordId;

        Builder() {
            super(EventType.AUDIO_RECORD_STOP);
        }

        /**
         * Unique recording identifier.
         *
         * @param recordId The recording ID as a string.
         *
         * @return This builder.
         */
        public Builder recordId(String recordId) {
            return recordId(UUID.fromString(recordId));
        }

        /**
         * Unique recording identifier.
         *
         * @param recordId The recording ID.
         *
         * @return This builder.
         */
        public Builder recordId(UUID recordId) {
            this.recordId = recordId;
            return this;
        }

        @Override
        public AudioRecordStopEvent build() {
            return new AudioRecordStopEvent(this);
        }
    }
}
