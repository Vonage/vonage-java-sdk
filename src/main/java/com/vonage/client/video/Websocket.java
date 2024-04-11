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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Defines the properties in {@link ConnectRequest#getWebsocket()}.
 *
 * @since 8.5.0
 */
public class Websocket extends JsonableBaseObject {
    private URI uri;
    private List<String> streams;
    private Map<String, String> headers;
    private AudioRate audioRate;

    Websocket() {}

    Websocket(ConnectRequest.Builder builder) {
        uri = builder.uri;
        streams = builder.streams;
        headers = builder.headers;
        audioRate = builder.audioRate;
    }

    /**
     * @return The uri, or {@code null} if absent / unknown / not applicable.
     */
    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    /**
     * @return The streams, or {@code null} if absent / unknown / not applicable.
     */
    @JsonProperty("streams")
    public List<String> getStreams() {
        return streams;
    }

    /**
     * @return The headers, or {@code null} if absent / unknown / not applicable.
     */
    @JsonProperty("headers")
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @return The audioRate, or {@code null} if absent / unknown / not applicable.
     */
    @JsonProperty("audioRate")
    public AudioRate getAudioRate() {
        return audioRate;
    }

    /**
     * Represents the possible audio bitrate values for a WebSocket connection.
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
    }
}
