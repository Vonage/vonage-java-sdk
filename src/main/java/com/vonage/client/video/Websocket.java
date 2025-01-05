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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.*;

/**
 * Defines the properties in {@link ConnectRequest#getWebsocket()}.
 *
 * @since 8.5.0
 */
public final class Websocket extends JsonableBaseObject {
    private URI uri;
    private Collection<String> streams;
    private Map<String, String> headers;
    private AudioRate audioRate;

    Websocket() {}

    Websocket(ConnectRequest.Builder builder) {
        String uriStr = Objects.requireNonNull(builder.uri, "WebSocket URI is required.");
        if (uriStr.startsWith("ws://") || uriStr.startsWith("wss://")) {
            uri = URI.create(builder.uri);
        }
        else {
            throw new IllegalArgumentException("Invalid URI protocol: must start with ws:// or wss://");
        }
        if (builder.streams != null) {
            streams = builder.streams instanceof List ? builder.streams : new ArrayList<>(builder.streams);
        }
        headers = builder.headers;
        audioRate = builder.audioRate;
    }

    /**
     * A publicly reachable WebSocket URI to be used for the destination of the audio stream.
     *
     * @return The WebSocket URI.
     */
    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    /**
     * Stream IDs for the Vonage Video streams you want to include in the WebSocket audio.
     * If you omitted, all streams in the session will be included.
     *
     * @return The collection of stream IDs to include, or {@code null} if not specified.
     */
    @JsonProperty("streams")
    public Collection<String> getStreams() {
        return streams;
    }

    /**
     * Headers to be sent to your WebSocket server with each message, with a maximum length of 512 bytes.
     *
     * @return The header key-value pairs as a Map, or {@code null} if not specified.
     */
    @JsonProperty("headers")
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Audio sampling rate in Hz.
     *
     * @return The sample rate as an enum, or {@code null} if not specified.
     */
    @JsonProperty("audioRate")
    public AudioRate getAudioRate() {
        return audioRate;
    }

    /**
     * Represents the possible audio sampling rate values for a WebSocket connection.
     */
    public enum AudioRate {
        /**
         * Linear 16-bit PCM at 8 Khz.
         */
        L16_8K,

        /**
         * Linear 16-bit PCM at 16 Khz.
         */
        L16_16K;

        /**
         * Converts the enum to an integer value representing the bitrate (Hz).
         *
         * @return The audio rate in Hertz as an int.
         */
        @JsonValue
        public int getAudioRate() {
            return this == L16_8K ? 8000 : 16_000;
        }

        /**
         * Converts an integer sampling value into the appropriate enum representation.
         *
         * @param value The sampling rate in Hz or KHz.
         *
         * @return The corresponding enum representation.
         */
        @JsonCreator
        public static AudioRate fromInt(int value) {
            switch (value) {
                case 8: case 8000: return L16_8K;
                case 16: case 16_000: return L16_16K;
                default: throw new IllegalArgumentException("Unsupported sample rate: "+value);
            }
        }
    }
}
