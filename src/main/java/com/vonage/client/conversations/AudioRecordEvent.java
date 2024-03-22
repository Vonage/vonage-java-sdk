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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vonage.client.voice.TextToSpeechLanguage;

/**
 * Represents an {@link EventType#AUDIO_RECORD} event.
 */
public final class AudioRecordEvent extends EventWithBody<AudioRecordEventBody> {

    AudioRecordEvent() {}

    private AudioRecordEvent(Builder builder) {
        super(builder);
        body = new AudioRecordEventBody();
    }

    /**
     * @return The language, or {@code null} if absent / unknown / not applicable.
     */
    @JsonIgnore
    public TextToSpeechLanguage getLanguage() {
        return body.transcription != null ? body.transcription.language : null;
    }

    /**
     * @return The sentimentAnalysis, or {@code null} if absent / unknown / not applicable.
     */
    @JsonIgnore
    public Boolean getSentimentAnalysis() {
        return body.transcription != null ? body.transcription.sentimentAnalysis : null;
    }

    /**
     * @return The validity, or {@code null} if absent / unknown / not applicable.
     */
    @JsonIgnore
    public Integer getValidity() {
        return body.validity;
    }

    /**
     * @return The channels, or {@code null} if absent / unknown / not applicable.
     */
    @JsonIgnore
    public Integer getChannels() {
        return body.channels;
    }

    /**
     * @return The streamed, or {@code null} if absent / unknown / not applicable.
     */
    @JsonIgnore
    public Boolean getStreamed() {
        return body.streamed;
    }

    /**
     * @return The split, or {@code null} if absent / unknown / not applicable.
     */
    @JsonIgnore
    public Boolean getSplit() {
        return body.split;
    }

    /**
     * @return The multitrack, or {@code null} if absent / unknown / not applicable.
     */
    @JsonIgnore
    public Boolean getMultitrack() {
        return body.multitrack;
    }

    /**
     * @return The detectSpeech, or {@code null} if absent / unknown / not applicable.
     */
    @JsonIgnore
    public Boolean getDetectSpeech() {
        return body.detectSpeech;
    }

    /**
     * @return The beepStart, or {@code null} if absent / unknown / not applicable.
     */
    @JsonIgnore
    public Boolean getBeepStart() {
        return body.beepStart;
    }

    /**
     * @return The beepStop, or {@code null} if absent / unknown / not applicable.
     */
    @JsonIgnore
    public Boolean getBeepStop() {
        return body.beepStop;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends EventWithBody.Builder<AudioRecordEvent, Builder> {
        String format;
        Integer validity, channels;
        Boolean streamed, split, multitrack, detectSpeech, beepStart, beepStop, sentimentAnalysis;
        TextToSpeechLanguage language;

        Builder() {
            super(EventType.AUDIO_RECORD);
        }

        /**
         *
         * @param format
         *
         * @return This builder.
         */
        public Builder format(String format) {
            this.format = format;
            return this;
        }

        /**
         *
         * @param validity
         *
         * @return This builder.
         */
        public Builder validity(int validity) {
            this.validity = validity;
            return this;
        }

        /**
         *
         * @param channels
         *
         * @return This builder.
         */
        public Builder channels(int channels) {
            this.channels = channels;
            return this;
        }

        /**
         *
         * @param streamed
         *
         * @return This builder.
         */
        public Builder streamed(boolean streamed) {
            this.streamed = streamed;
            return this;
        }

        /**
         *
         * @param split
         *
         * @return This builder.
         */
        public Builder split(boolean split) {
            this.split = split;
            return this;
        }

        /**
         *
         * @param multitrack
         *
         * @return This builder.
         */
        public Builder multitrack(boolean multitrack) {
            this.multitrack = multitrack;
            return this;
        }

        /**
         *
         * @param detectSpeech
         *
         * @return This builder.
         */
        public Builder detectSpeech(boolean detectSpeech) {
            this.detectSpeech = detectSpeech;
            return this;
        }

        /**
         *
         * @param beepStart
         *
         * @return This builder.
         */
        public Builder beepStart(boolean beepStart) {
            this.beepStart = beepStart;
            return this;
        }

        /**
         *
         * @param beepStop
         *
         * @return This builder.
         */
        public Builder beepStop(boolean beepStop) {
            this.beepStop = beepStop;
            return this;
        }

        /**
         *
         * @param sentimentAnalysis
         *
         * @return This builder.
         */
        public Builder sentimentAnalysis(boolean sentimentAnalysis) {
            this.sentimentAnalysis = sentimentAnalysis;
            return this;
        }

        /**
         *
         * @param language
         *
         * @return This builder.
         */
        public Builder language(TextToSpeechLanguage language) {
            this.language = language;
            return this;
        }

        @Override
        public AudioRecordEvent build() {
            return new AudioRecordEvent(this);
        }
    }
}
