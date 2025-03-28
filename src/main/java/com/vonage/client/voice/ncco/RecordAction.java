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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;

/**
 * An NCCO record action which allows for the call to be recorded.
 * <p>
 *
 * The record action is asynchronous. Recording starts when the record action is executed in the NCCO and finishes
 * when the synchronous condition in the action is met. That is, endOnSilence, timeOut or endOnKey. If you do not
 * set a synchronous condition, the Voice API immediately executes the next NCCO without recording.
 */
public final class RecordAction extends JsonableBaseObject implements Action {
    private RecordingFormat format;
    private Integer endOnSilence, timeOut, channels;
    private Character endOnKey;
    private Boolean beepStart;
    private Collection<URI> eventUrl;
    private EventMethod eventMethod;
    private SplitRecording split;
    private TranscriptionSettings transcription;

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    RecordAction() {}

    private RecordAction(Builder builder) {
        format = builder.format;
        endOnSilence = builder.endOnSilence;
        endOnKey = builder.endOnKey;
        timeOut = builder.timeOut;
        beepStart = builder.beepStart;
        eventUrl = builder.eventUrl != null ? Collections.singletonList(URI.create(builder.eventUrl)) : null;
        eventMethod = builder.eventMethod;
        // Split conversation must be enabled for multiple channels. Checked during construction to avoid
        // method-chaining state confusion.
        split = (builder.channels != null && builder.channels > 1) ? SplitRecording.CONVERSATION : null;
        channels = builder.channels;
        transcription = builder.transcription;
    }

    @Override
    public String getAction() {
        return "record";
    }

    /**
     * Format of the recording.
     *
     * @return The recording format as an enum, or {@code null} if unspecified.
     */
    @JsonProperty("format")
    public RecordingFormat getFormat() {
        return format;
    }

    /**
     * Stop recording after this many seconds of silence.
     * Once the recording is stopped the recording data is sent to the eventUrl.
     *
     * @return The number of seconds of silence that will end the recording, or {@code null} if unspecified.
     */
    @JsonProperty("endOnSilence")
    public Integer getEndOnSilence() {
        return endOnSilence;
    }

    /**
     * Stop recording when a digit is pressed on the handset.
     *
     * @return The key that will end the recording as a character, or {@code null} if unspecified.
     */
    @JsonProperty("endOnKey")
    public Character getEndOnKey() {
        return endOnKey;
    }

    /**
     * Maximum length of a recording in seconds.
     * Once the recording is stopped the recording data is sent to the eventUrl.
     *
     * @return The maximum recording length in seconds, or {@code null} if unspecified.
     */
    @JsonProperty("timeOut")
    public Integer getTimeOut() {
        return timeOut;
    }

    /**
     * Play a beep when the recording starts.
     *
     * @return Whether a beep will be played on recording start, or {@code null} if unspecified.
     */
    @JsonProperty("beepStart")
    public Boolean getBeepStart() {
        return beepStart;
    }

    /**
     * URL to the webhook endpoint that is called asynchronously when a recording is finished.
     * If the message recording is hosted by Vonage, this webhook contains the URL you need to download the
     * recording and other metadata, which can be parsed using {@linkplain com.vonage.client.voice.EventWebhook}.
     *
     * @return The URL to deliver the webhook to wrapped in a collection, or {@code null} if unspecified.
     */
    @JsonProperty("eventUrl")
    public Collection<URI> getEventUrl() {
        return eventUrl;
    }

    /**
     * HTTP method used to make the request to eventUrl.
     *
     * @return The webhook event method as an enum, or {@code null} if unspecified.
     */
    @JsonProperty("eventMethod")
    public EventMethod getEventMethod() {
        return eventMethod;
    }

    /**
     * Record the sent and received audio in separate channels of a stereo recording.
     *
     * @return The split recording enum, or {@code null} if the recording is single channel audio.
     */
    @JsonProperty("split")
    public SplitRecording getSplit() {
        return split;
    }

    /**
     * Number of channels to record.
     *
     * @return The number of channels to record, or {@code null} if unspecified.
     */
    @JsonProperty("channels")
    public Integer getChannels() {
        return channels;
    }

    /**
     * Transcription settings for the recording.
     *
     * @return The transcription settings object, or {@code null} if transcription is not enabled.
     * @since 8.2.0
     */
    @JsonProperty("transcription")
    public TranscriptionSettings getTranscription() {
        return transcription;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for a RecordAction. All fields are optional.
     */
    public static class Builder {
        private RecordingFormat format;
        private Character endOnKey;
        private Integer timeOut, channels, endOnSilence;
        private Boolean beepStart;
        private String eventUrl;
        private EventMethod eventMethod;
        private TranscriptionSettings transcription;

        Builder() {}

        /**
         * Record the Call in a specific format. The default value is {@link RecordingFormat#MP3}, or
         * {@link RecordingFormat#WAV} when recording more than 2 channels.
         *
         * @param format The recording file format as an enum.
         *
         * @return This builder.
         */
        public Builder format(RecordingFormat format) {
            this.format = format;
            return this;
        }

        /**
         * Stop recording after n seconds of silence. Once the recording is stopped the recording data is sent to
         * eventUrl. The range of possible values is between 3 and 10 inclusively.
         *
         * @param endOnSilence The number of seconds of silence that will end the recording.
         *
         * @return This builder.
         */
        public Builder endOnSilence(int endOnSilence) {
            this.endOnSilence = endOnSilence;
            return this;
        }

        /**
         * Stop recording when a digit is pressed on the handset. Possible values are: *, # and digits 0-9.
         *
         * @param endOnKey The key that will end the recording as a character.
         *
         * @return This builder.
         */
        public Builder endOnKey(char endOnKey) {
            this.endOnKey = endOnKey;
            return this;
        }

        /**
         * Maximum length of a recording in seconds. Once the recording is stopped the recording data is sent to
         * the eventUrl. The range of possible values is between 3 and 7200 seconds (i.e. 2 hours).
         *
         * @param timeOut The maximum recording length in seconds.
         *
         * @return This builder.
         */
        public Builder timeOut(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * Whether to play a beep when the recording starts.
         *
         * @param beepStart Set {@code true} to play a beep when a recording starts, {@code false} otherwise.
         *
         * @return This builder.
         */
        public Builder beepStart(boolean beepStart) {
            this.beepStart = beepStart;
            return this;
        }

        /**
         * URL to the webhook endpoint that is called asynchronously when a recording is finished. If the message
         * recording is hosted by Vonage, this webhook contains the URL you need to download the recording and
         * other metadata, which can be parsed using {@linkplain com.vonage.client.voice.EventWebhook}.
         *
         * @param eventUrl The URL to deliver the webhook to as a string.
         *
         * @return This builder.
         */
        public Builder eventUrl(String eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * HTTP method used to make the request to eventUrl. The default value is {@code POST}.
         *
         * @param eventMethod The webhook event method as an enum.
         *
         * @return This builder.
         */
        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        /**
         * Number of channels to record (maximum 32). If the number of participants exceeds channels any additional
         * participants will be added to the last channel in file. {@link #split} will be set to
         * {@link SplitRecording#CONVERSATION} during the build process if channels is greater than 1.
         *
         * @param channels The number of channels to record.
         *
         * @return This builder.
         */
        public Builder channels(int channels) {
            this.channels = channels;
            return this;
        }

        /**
         * Set this parameter to a non-null value to transcribe the recording.
         *
         * @param transcription The transcription settings.
         *
         * @return This builder.
         * @since 8.2.0
         */
        public Builder transcription(TranscriptionSettings transcription) {
            this.transcription = transcription;
            return this;
        }

        /**
         * Builds the RecordAction.
         *
         * @return A new RecordAction object from the stored builder options.
         */
        public RecordAction build() {
            return new RecordAction(this);
        }
    }
}
