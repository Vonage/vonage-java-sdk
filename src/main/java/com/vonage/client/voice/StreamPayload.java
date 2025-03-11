/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.util.Objects;

/**
 * Represents the JSON payload that will be sent in {@link VoiceClient#startStream}.
 */
public class StreamPayload extends UuidRequestWrapper {
    private final URI[] streamUrl;
    private final Integer loop;
    private final Double level;

    /**
     * Creates a new StreamPayload.
     *
     * @param builder The builder to construct this object from.
     * @since 8.19.0
     */
    StreamPayload(Builder builder) {
        streamUrl = new URI[]{Objects.requireNonNull(builder.streamUrl, "Stream URL is required.")};
        loop = builder.loop;
        level = builder.level;
    }

    /**
     * An array containing a single URL to an MP3 or wav (16-bit) audio file.
     *
     * @return The stream URL wrapped in an array.
     */
    @JsonProperty("stream_url")
    public URI[] getStreamUrl() {
        return streamUrl;
    }

    /**
     * Number of times the audio file at {@code streamUrl} is repeated before the stream ends.
     *
     * @return The number of times the audio file is repeated, or {@code null} if unspecified.
     */
    @JsonProperty("loop")
    public Integer getLoop() {
        return loop;
    }

    /**
     * Volume which the audio is played at.
     *
     * @return The stream volume between -1.0 and 1.0, or {@code null} if unspecified.
     */
    @JsonProperty("level")
    public Double getLevel() {
        return level;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @return A new Builder.
     * @since 8.19.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for specifying the properties of a StreamPayload. The URL is mandatory.
     *
     * @since 8.19.0
     */
    public static class Builder {
        private URI streamUrl;
        private Integer loop;
        private Double level;

        Builder() {}

        /**
         * Sets the stream URL.
         *
         * @param streamUrl URL to an MP3 or wav (16-bit) audio file.
         * @return This builder.
         */
        public Builder streamUrl(String streamUrl) {
            return streamUrl(URI.create(streamUrl));
        }

        /**
         * Sets the stream URL.
         *
         * @param streamUrl URL to an MP3 or wav (16-bit) audio file.
         * @return This builder.
         */
        public Builder streamUrl(URI streamUrl) {
            this.streamUrl = streamUrl;
            return this;
        }

        /**
         * Sets the number of times the audio is repeated before the stream ends.
         *
         * @param loop Number of times the audio is repeated before the stream ends (0 means infinite).
         * @return This builder.
         */
        public Builder loop(int loop) {
            this.loop = loop;
            return this;
        }

        /**
         * Sets the volume the audio is played at.
         *
         * @param level The volume the audio is played at (-1.0 to 1.0).
         * @return This builder.
         */
        public Builder level(double level) {
            this.level = level;
            return this;
        }

        /**
         * Builds the StreamPayload object.
         *
         * @return A new {@linkplain StreamPayload} instance with this builder's properties.
         */
        public StreamPayload build() {
            return new StreamPayload(this);
        }
    }
}
