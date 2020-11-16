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
 * An NCCO stream action which allows for media to be streamed to a call.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamAction implements Action {
    private static final String ACTION = "stream";

    private Collection<String> streamUrl;
    private Float level;
    private Boolean bargeIn;
    private Integer loop;

    private StreamAction(Builder builder) {
        streamUrl = builder.streamUrl;
        level = builder.level;
        bargeIn = builder.bargeIn;
        loop = builder.loop;
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    public Collection<String> getStreamUrl() {
        return streamUrl;
    }

    public Float getLevel() {
        return level;
    }

    public Boolean getBargeIn() {
        return bargeIn;
    }

    public Integer getLoop() {
        return loop;
    }

    public static Builder builder(Collection<String> streamUrl) {
        return new Builder(streamUrl);
    }

    public static Builder builder(String... streamUrl) {
        return new Builder(streamUrl);
    }

    public static class Builder {
        private Collection<String> streamUrl;
        private Float level;
        private Boolean bargeIn;
        private Integer loop;

        /**
         * @param streamUrl An array containing a single URL to an mp3 or wav (16-bit) audio file to stream to the
         *                  Call or Conversation.
         */
        public Builder(Collection<String> streamUrl) {
            this.streamUrl = streamUrl;
        }

        /**
         * @param streamUrl An array containing a single URL to an mp3 or wav (16-bit) audio file to stream to the
         *                  Call or Conversation.
         */
        public Builder(String... streamUrl) {
            this(Arrays.asList(streamUrl));
        }

        /**
         * @param streamUrl An array containing a single URL to an mp3 or wav (16-bit) audio file to stream to the
         *                  Call or Conversation.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder streamUrl(Collection<String> streamUrl) {
            this.streamUrl = streamUrl;
            return this;
        }

        /**
         * @param streamUrl An array containing a single URL to an mp3 or wav (16-bit) audio file to stream to the
         *                  Call or Conversation.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder streamUrl(String... streamUrl) {
            return streamUrl(Arrays.asList(streamUrl));
        }

        /**
         * @param level Set the audio level of the stream in the range between -1 and 1 inclusively with a precision
         *              of 0.1. The default value is 0.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder level(Float level) {
            this.level = level;
            return this;
        }

        /**
         * @param bargeIn Set to true so this action is terminated when the user presses a button on the keypad.
         *                Use this feature to enable users to choose an option without having to listen to the whole
         *                message in your Interactive Voice Response (IVR ). If you set bargeIn to true on one more
         *                Stream actions then the next action in the NCCO stack must be an input action.
         *                <p>
         *                The default value is false.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder bargeIn(Boolean bargeIn) {
            this.bargeIn = bargeIn;
            return this;
        }

        /**
         * @param loop The number of times audio is repeated before the Call is closed.
         *             The default value is 1. Set to 0 to loop infinitely.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder loop(Integer loop) {
            this.loop = loop;
            return this;
        }

        /**
         * @return A new {@link StreamAction} object from the stored builder options.
         */
        public StreamAction build() {
            return new StreamAction(this);
        }
    }
}
