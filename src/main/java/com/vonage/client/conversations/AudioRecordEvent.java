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
     * Text-to-speech transcription language.
     *
     * @return The transcription language as an enum, or {@code null} if unspecified.
     */
    @JsonIgnore
    public TextToSpeechLanguage getLanguage() {
        return body.transcription != null ? body.transcription.language : null;
    }

    /**
     * Whether sentiment analysis is enabled.
     *
     * @return {@code true} if sentiment analysis is enabled,
     * or {@code null} if transcription is not enabled.
     */
    @JsonIgnore
    public Boolean getSentimentAnalysis() {
        return body.transcription != null ? body.transcription.sentimentAnalysis : null;
    }

    /**
     * File format for the recording.
     *
     * @return The recording format as a string, or {@code null} if unspecified.
     */
    public String getFormat() {
        return body.format;
    }

    /**
     * The validity parameter.
     *
     * @return The validity as an integer, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Integer getValidity() {
        return body.validity;
    }

    /**
     * Number of channels for the recording.
     *
     * @return The number of channels as an Integer, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Integer getChannels() {
        return body.channels;
    }

    /**
     * Whether the audio recording is streamed.
     *
     * @return {@code true} if the recording is streamed, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Boolean getStreamed() {
        return body.streamed;
    }

    /**
     * Whether the audio is split.
     *
     * @return {@code true} if the recording is split, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Boolean getSplit() {
        return body.split;
    }

    /**
     * Whether the audio has multiple tracks.
     *
     * @return {@code true} if the recording is multi-track, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Boolean getMultitrack() {
        return body.multitrack;
    }

    /**
     * Whether to detect speech in the recording.
     *
     * @return {@code true} if speech detection is enabled, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Boolean getDetectSpeech() {
        return body.detectSpeech;
    }

    /**
     * Whether to enable beep start.
     *
     * @return {@code true} if beep start, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Boolean getBeepStart() {
        return body.beepStart;
    }

    /**
     * Whether to enable beep stop.
     *
     * @return {@code true} if beep stop, or {@code null} if unspecified.
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

    /**
     * Builder for setting the Audio Record event parameters.
     */
    public static final class Builder extends EventWithBody.Builder<AudioRecordEvent, Builder> {
        String format;
        Integer validity, channels;
        Boolean streamed, split, multitrack, detectSpeech, beepStart, beepStop, sentimentAnalysis;
        TextToSpeechLanguage language;

        Builder() {
            super(EventType.AUDIO_RECORD);
        }

        /**
         * File format for the recording.
         *
         * @param format The format as a string.
         *
         * @return This builder.
         */
        public Builder format(String format) {
            this.format = format;
            return this;
        }

        /**
         * Audio recording validity parameter.
         *
         * @param validity The validity as an int.
         *
         * @return This builder.
         */
        public Builder validity(int validity) {
            this.validity = validity;
            return this;
        }

        /**
         * Number of channels for the recording.
         *
         * @param channels The number of channels as an int.
         *
         * @return This builder.
         */
        public Builder channels(int channels) {
            this.channels = channels;
            return this;
        }

        /**
         * Whether the audio recording is streamed.
         *
         * @param streamed {@code true} if the recording should be streamed.
         *
         * @return This builder.
         */
        public Builder streamed(boolean streamed) {
            this.streamed = streamed;
            return this;
        }

        /**
         * Whether the recording should be split.
         *
         * @param split {@code true} if the audio should be split.
         *
         * @return This builder.
         */
        public Builder split(boolean split) {
            this.split = split;
            return this;
        }

        /**
         * Whether the audio should have multiple tracks.
         *
         * @param multitrack {@code true} if the recording should be multi-track.
         *
         * @return This builder.
         */
        public Builder multitrack(boolean multitrack) {
            this.multitrack = multitrack;
            return this;
        }

        /**
         * Whether speech detection is enabled.
         *
         * @param detectSpeech {@code true} to enable speech detection.
         *
         * @return This builder.
         */
        public Builder detectSpeech(boolean detectSpeech) {
            this.detectSpeech = detectSpeech;
            return this;
        }

        /**
         * Whether to set the {@code beep_start} flag.
         *
         * @param beepStart {@code true} to enable beep start.
         *
         * @return This builder.
         */
        public Builder beepStart(boolean beepStart) {
            this.beepStart = beepStart;
            return this;
        }

        /**
         * Whether to set the {@code beep_stop} flag.
         *
         * @param beepStop {@code true} to enable beep stop.
         *
         * @return This builder.
         */
        public Builder beepStop(boolean beepStop) {
            this.beepStop = beepStop;
            return this;
        }

        /**
         * Whether to enable sentiment analysis in the recording transcription.
         *
         * @param sentimentAnalysis {@code true} to enable transcription sentiment analysis.
         *
         * @return This builder.
         */
        public Builder sentimentAnalysis(boolean sentimentAnalysis) {
            this.sentimentAnalysis = sentimentAnalysis;
            return this;
        }

        /**
         * Text-to-speech transcription language. Setting this will enable recording transcription.
         *
         * @param language The transcription language as an enum.
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
