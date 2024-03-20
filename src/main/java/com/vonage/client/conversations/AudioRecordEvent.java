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
import com.vonage.client.JsonableBaseObject;

/**
 * Represents an {@link EventType#AUDIO_RECORD} event.
 */
public final class AudioRecordEvent extends EventWithBody<AudioRecordEvent.Body> {

    AudioRecordEvent() {}

    private AudioRecordEvent(Builder builder) {
        super(builder);
        body = new Body();
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Body extends JsonableBaseObject {

    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends EventWithBody.Builder<AudioRecordEvent, Builder> {

        Builder() {
            super(EventType.AUDIO_RECORD);
        }


        @Override
        public AudioRecordEvent build() {
            return new AudioRecordEvent(this);
        }
    }
}
