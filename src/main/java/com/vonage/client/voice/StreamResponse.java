/*
 *   Copyright 2020 Vonage
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;

/**
 * Response from successfully streaming an audio file or stopping a stream to an active {@link Call}.
 * <p>
 * This would be returned by {@link VoiceClient#startStream(String, String)} or {@link VoiceClient#stopStream(String)}
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamResponse {
    private String uuid;
    private String message;

    public String getUuid() {
        return uuid;
    }

    public String getMessage() {
        return message;
    }

    public static StreamResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, StreamResponse.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from StreamResponse object.", jpe);
        }
    }

}
