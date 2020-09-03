/*
 * Copyright (c) 2020 Vonage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.voice.ncco;

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
        this.streamUrl = builder.streamUrl;
        this.level = builder.level;
        this.bargeIn = builder.bargeIn;
        this.loop = builder.loop;
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
        private Float level = null;
        private Boolean bargeIn = null;
        private Integer loop = null;

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
