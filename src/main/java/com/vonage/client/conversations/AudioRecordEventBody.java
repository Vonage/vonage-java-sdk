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
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.voice.TextToSpeechLanguage;

class AudioRecordEventBody extends JsonableBaseObject {
    @JsonProperty("transcription") Transcription transcription;
    @JsonProperty("format") String format;
    @JsonProperty("validity") Integer validity;
    @JsonProperty("channels") Integer channels;
    @JsonProperty("streamed") Boolean streamed;
    @JsonProperty("split") Boolean split;
    @JsonProperty("multitrack") Boolean multitrack;
    @JsonProperty("detect_speech") Boolean detectSpeech;
    @JsonProperty("beep_start") Boolean beepStart;
    @JsonProperty("beep_stop") Boolean beepStop;

    static class Transcription extends JsonableBaseObject {
       @JsonProperty("language") TextToSpeechLanguage language;
       @JsonProperty("sentiment_analysis") Boolean sentimentAnalysis;
    }

    AudioRecordEventBody() {}

    AudioRecordEventBody(AudioRecordEvent.Builder builder) {
        if (builder.language != null || builder.sentimentAnalysis != null) {
            transcription = new Transcription();
            transcription.language = builder.language;
            transcription.sentimentAnalysis = builder.sentimentAnalysis;
        }
        format = builder.format;
        validity = builder.validity;
        channels = builder.channels;
        streamed = builder.streamed;
        split = builder.split;
        multitrack = builder.multitrack;
        detectSpeech = builder.detectSpeech;
        beepStart = builder.beepStart;
        beepStop = builder.beepStop;
    }
}
