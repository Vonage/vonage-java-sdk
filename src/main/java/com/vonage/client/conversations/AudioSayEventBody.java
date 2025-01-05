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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.voice.TextToSpeechLanguage;
import java.util.UUID;

class AudioSayEventBody extends AudioOutEvent.Body {
    @JsonProperty("say_id") UUID sayId;
    @JsonProperty("text") String text;
    @JsonProperty("style") Integer style;
    @JsonProperty("language") TextToSpeechLanguage language;
    @JsonProperty("premium") Boolean premium;
    @JsonProperty("ssml") Boolean ssml;

    AudioSayEventBody() {
    }

    AudioSayEventBody(UUID sayId) {
        this.sayId = sayId;
    }

    AudioSayEventBody(AudioSayEvent.Builder builder) {
        super(builder);
        if ((text = builder.text) == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Speech text is required and cannot be empty.");
        }
        style = builder.style;
        language = builder.language;
        premium = builder.premium;
        ssml = builder.ssml;
    }
}
