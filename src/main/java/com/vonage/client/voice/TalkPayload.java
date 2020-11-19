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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

/**
 * The JSON payload that will be sent in a {@link TalkRequest}.
 * <p>
 * {@code text}: A string of up to 1500 characters containing the message to be synthesized
 * in the Call or Conversation. Each comma in text adds a short pause to the synthesized speech.
 * {@link VoiceName}: DEPRECATED: The name of the voice used to deliver {@code text}.
 * {@code loop}: The number of times the audio file at stream_url is repeated before the stream ends. Set to 0 to loop infinitely.
 * {@link TextToSpeechLanguage}: The Language that will be used to convert {@code text} into speech
 * {@code style}: The Vocal Style to use (vocal Range, tessitura, timbre to use in the TTS
 */

public class TalkPayload {
    private String text;
    private int loop;
    private TextToSpeechLanguage language;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer style = null;

    @Deprecated
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private VoiceName voiceName;

    @Deprecated
    public TalkPayload(String text, VoiceName voiceName, int loop) {
        this.text = text;
        this.voiceName = voiceName;
        this.loop = loop;
    }

    public TalkPayload(String text, TextToSpeechLanguage language, int style, int loop){
        this.text = text;
        this.language = language;
        this.style = style;
        this.loop = loop;
    }

    public TalkPayload(String text, TextToSpeechLanguage language, int loop){
        this.text = text;
        this.language = language;
        this.loop = loop;
    }

    public int getLoop() {
        return loop;
    }

    public String getText() {
        return text;
    }

    @JsonProperty(value = "voice_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public VoiceName getVoiceName() {
        return voiceName;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public TextToSpeechLanguage getLanguage() { return language; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getStyle() { return style; }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from TalkPayload object.", jpe);
        }
    }
}
