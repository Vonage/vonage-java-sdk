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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

/**
 * The JSON payload that will be sent in a {@link TalkRequest}.
 * <p>
 * {@code text}: A string of up to 1500 characters containing the message to be synthesized
 * in the Call or Conversation. Each comma in text adds a short pause to the synthesized speech.
 * {@link VoiceName}: The name of the voice used to deliver {@code text}.
 * {@code loop}: The number of times the audio file at stream_url is repeated before the stream ends. Set to 0 to loop infinitely.
 */

public class TalkPayload {
    private String text;
    private VoiceName voiceName;
    private int loop;

    public TalkPayload(String text, VoiceName voiceName, int loop) {
        this.text = text;
        this.voiceName = voiceName;
        this.loop = loop;
    }

    public int getLoop() {
        return loop;
    }

    public String getText() {
        return text;
    }

    @JsonProperty("voice_name")
    public VoiceName getVoiceName() {
        return voiceName;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from TalkPayload object.", jpe);
        }
    }
}
