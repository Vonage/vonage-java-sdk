/*
 * Copyright 2025 Vonage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents speech to text data contained in {@link SpeechResults#getResults()}.
 *
 * @since 8.2.0
 */
public class SpeechTranscript extends JsonableBaseObject {
    private String text;
    private Double confidence;

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    SpeechTranscript() {}

    /**
     * Transcript text representing the words that the user spoke.
     *
     * @return The transcript text.
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * The confidence estimate between 0.0 and 1.0. A higher number indicates an estimated greater
     * likelihood that the recognized words are correct.
     *
     * @return The confidence estimate between 0.0 and 1.0 as a Double.
     */
    @JsonProperty("confidence")
    public Double getConfidence() {
        return confidence;
    }
}
