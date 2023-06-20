/*
 *   Copyright 2023 Vonage
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

/**
 * The JSON payload that will be sent in a {@link StreamRequestWrapper}.
 * <p>
 * {@code streamUrl}: An array containing a single URL to an mp3 or wav (16-bit) audio file.
 * {@code loop}: The number of times the audio file at {@code streamUrl} is repeated before the stream ends. Set to 0 to loop infinitely
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
class StreamPayload {
    private final String[] streamUrl;
    private final Integer loop;
    private final Double level;

    public StreamPayload(String streamUrl, Integer loop, Double level) {
        this.streamUrl = new String[]{streamUrl};
        this.loop = loop;
        this.level = level;
    }

    @JsonProperty("stream_url")
    public String[] getStreamUrl() {
        return streamUrl;
    }

    @JsonProperty("loop")
    public Integer getLoop() {
        return loop;
    }

    @JsonProperty("level")
    public Double getLevel() {
        return level;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from StreamPayload object.", jpe);
        }
    }
}
