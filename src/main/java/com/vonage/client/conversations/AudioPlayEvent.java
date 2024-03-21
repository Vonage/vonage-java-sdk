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
import java.net.URI;
import java.util.UUID;

/**
 * Represents an {@link EventType#AUDIO_PLAY} event.
 */
public final class AudioPlayEvent extends AudioOutEvent<AudioPlayEvent.Body> {

    AudioPlayEvent() {}

    private AudioPlayEvent(Builder builder) {
        super(builder);
        body = new Body(builder);
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class Body extends AudioOutEvent.Body {
        @JsonProperty("play_id") private UUID playId;
        @JsonProperty("stream_url") private URI[] streamUrl;

        Body() {}

        Body(Builder builder) {
            super(builder);
            streamUrl = builder.streamUrl;
        }
    }

    /**
     * Unique audio play identifier.
     *
     * @return The say ID, or {@code null} if unknown.
     */
    public UUID getPlayId() {
        return body != null ? body.playId : null;
    }

    /**
     * Source URL of the audio to play.
     *
     * @return The stream URL, or {@code null} if unspecified.
     */
    @JsonIgnore
    public URI getStreamUrl() {
        return body != null && body.streamUrl != null && body.streamUrl.length > 0 ? body.streamUrl[0] : null;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends AudioOutEvent.Builder<AudioPlayEvent, Builder> {
        private URI[] streamUrl;

        Builder() {
            super(EventType.AUDIO_PLAY);
        }

        /**
         * Source URL of the audio to play.
         *
         * @param streamUrl The stream URL as a string.
         *
         * @return This builder.
         */
        public Builder streamUrl(String streamUrl) {
            return streamUrl(URI.create(streamUrl));
        }

        /**
         * Source URL of the audio to play.
         *
         * @param streamUrl The stream URL.
         *
         * @return This builder.
         */
        public Builder streamUrl(URI streamUrl) {
            this.streamUrl = new URI[]{streamUrl};
            return this;
        }

        @Override
        public AudioPlayEvent build() {
            return new AudioPlayEvent(this);
        }
    }
}
