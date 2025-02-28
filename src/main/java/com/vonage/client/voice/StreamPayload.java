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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.Objects;

/**
 * Represents the JSON payload that will be sent in {@link VoiceClient#startStream}.
 */
class StreamPayload extends JsonableBaseObject {
    @JsonIgnore final String uuid;
    private final String[] streamUrl;
    private final Integer loop;
    private final Double level;

    /**
     * Creates a new StreamPayload.
     *
     * @param streamUrl URL to an MP3 or wav (16-bit) audio file.
     * @param loop Number of times the audio is repeated before the stream ends (0 means infinite).
     * @param level The volume the audio is played at (-1.0 to 1.0).
     * @param uuid UUID of the call to stream audio into.
     */
    public StreamPayload(String streamUrl, Integer loop, Double level, String uuid) {
        this.streamUrl = new String[]{Objects.requireNonNull(streamUrl, "Stream URL is required.")};
        this.loop = loop;
        this.level = level;
        this.uuid = uuid;
    }

    /**
     * An array containing a single URL to an MP3 or wav (16-bit) audio file.
     *
     * @return The stream URL wrapped in an array.
     */
    @JsonProperty("stream_url")
    public String[] getStreamUrl() {
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
}
