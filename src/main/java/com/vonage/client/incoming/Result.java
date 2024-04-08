/*
 * Copyright 2024 Vonage
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
package com.vonage.client.incoming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @deprecated Use {@link com.vonage.client.voice.SpeechTranscript}.
 */
@Deprecated
public class Result {
    private String text, confidence;

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
     * @param text transcript text representing the words the user spoke.
     *
     * @deprecated This setter will be removed in a future release.
     */
    @Deprecated
    public void setText(String text) {
        this.text = text;
    }

    /**
     * The confidence estimate between 0.0 and 1.0. A higher number indicates an estimated greater
     * likelihood that the recognized words are correct.
     *
     * @return The confidence estimate between 0.0 and 1.0 as a String.
     *
     * @deprecated This will be converted to a Double in a future release.
     */
    @Deprecated
    public String getConfidence() {
        return confidence;
    }

    /**
     * @param confidence The confidence estimate between 0.0 and 1.0. A higher number indicates an estimated greater
     *                   likelihood that the recognized words are correct.
     *
     * @deprecated This setter will be removed in a future release.
     */
    @Deprecated
    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }
}
