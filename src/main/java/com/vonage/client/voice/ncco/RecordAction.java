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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.Collection;

/**
 * An NCCO record action which allows for the call to be recorded.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordAction implements Action {
    private static final String ACTION = "record";

    private RecordingFormat format;
    private Integer endOnSilence;
    private Character endOnKey;
    private Integer timeOut;
    private Boolean beepStart;
    private Collection<String> eventUrl;
    private EventMethod eventMethod;
    private SplitRecording split;
    private Integer channels;

    /**
     * @param builder Builder for building the Record Action
     */
    private RecordAction(Builder builder) {
        this.format = builder.format;
        this.endOnSilence = builder.endOnSilence;
        this.endOnKey = builder.endOnKey;
        this.timeOut = builder.timeOut;
        this.beepStart = builder.beepStart;
        this.eventUrl = builder.eventUrl;
        this.eventMethod = builder.eventMethod;

        // Split conversation must be enabled for multiple channels. Checked during construction to avoid
        // method-chaining state confusion.
        this.split = (builder.channels != null && builder.channels > 1) ? SplitRecording.CONVERSATION : builder.split;

        this.channels = builder.channels;
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    public RecordingFormat getFormat() {
        return format;
    }

    public Integer getEndOnSilence() {
        return endOnSilence;
    }

    public Character getEndOnKey() {
        return endOnKey;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public Boolean getBeepStart() {
        return beepStart;
    }

    public Collection<String> getEventUrl() {
        return eventUrl;
    }

    public EventMethod getEventMethod() {
        return eventMethod;
    }

    public SplitRecording getSplit() {
        return split;
    }

    public Integer getChannels() {
        return channels;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RecordingFormat format = null;
        private Integer endOnSilence = null;
        private Character endOnKey = null;
        private Integer timeOut = null;
        private Boolean beepStart = null;
        private Collection<String> eventUrl = null;
        private EventMethod eventMethod = null;
        private SplitRecording split = null;
        private Integer channels = null;

        /**
         * @param format Record the Call in a specific {@link RecordingFormat}.
         *               <p>
         *               The default value is {@link RecordingFormat#MP3}, or {@link RecordingFormat#WAV} when recording
         *               more than 2 channels.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder format(RecordingFormat format) {
            this.format = format;
            return this;
        }

        /**
         * @param endOnSilence Stop recording after n seconds of silence. Once the recording is stopped the recording
         *                     data is sent to event_url. The range of possible values is between 3 and 10 inclusively.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder endOnSilence(Integer endOnSilence) {
            this.endOnSilence = endOnSilence;
            return this;
        }

        /**
         * @param endOnKey Stop recording when a digit is pressed on the handset. Possible values are: *, # or any
         *                 single digit e.g. 9
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder endOnKey(Character endOnKey) {
            this.endOnKey = endOnKey;
            return this;
        }

        /**
         * @param timeOut The maximum length of a recording in seconds. One the recording is stopped the recording data
         *                is sent to event_url. The range of possible values is between 3 seconds and 7200 seconds (2
         *                hours)
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder timeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * @param beepStart Set to true to play a beep when a recording starts
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder beepStart(Boolean beepStart) {
            this.beepStart = beepStart;
            return this;
        }

        /**
         * @param eventUrl The URL to the webhook endpoint that is called asynchronously when a recording is finished.
         *                 If the message recording is hosted by Vonage, this webhook contains the URL you need to
         *                 download the recording and other meta data.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * @param eventUrl The URL to the webhook endpoint that is called asynchronously when a recording is finished.
         *                 If the message recording is hosted by Vonage, this webhook contains the URL you need to
         *                 download the recording and other meta data.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventUrl(String... eventUrl) {
            return eventUrl(Arrays.asList(eventUrl));
        }

        /**
         * @param eventMethod The HTTP method used to make the request to eventUrl. The default value is POST.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        /**
         * @param split Record the sent and received audio in separate channels of a stereo recording—set to {@link
         *              SplitRecording#CONVERSATION} to enable this.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder split(SplitRecording split) {
            this.split = split;
            return this;
        }

        /**
         * @param channels The number of channels to record (maximum 32). If the number of participants exceeds channels
         *                 any additional participants will be added to the last channel in file. {@link #split} will be
         *                 set to {@link SplitRecording#CONVERSATION} during the build process if channels is greater
         *                 than 1.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder channels(Integer channels) {
            this.channels = channels;
            return this;
        }

        /**
         * @return A new {@link RecordAction} object from the stored builder options.
         */
        public RecordAction build() {
            return new RecordAction(this);
        }
    }
}
