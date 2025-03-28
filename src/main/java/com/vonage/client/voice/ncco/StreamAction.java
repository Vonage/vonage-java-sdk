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
import java.util.Objects;

/**
 * An NCCO stream action which allows for media to be streamed to a call.
 */
public class StreamAction extends JsonableBaseObject implements Action {
    private Collection<URI> streamUrl;
    private Double level;
    private Boolean bargeIn;
    private Integer loop;

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    StreamAction() {}

    private StreamAction(Builder builder) {
        streamUrl = Collections.singleton(URI.create(
                Objects.requireNonNull(builder.streamUrl, "Stream URL is required")
        ));
        level = builder.level;
        bargeIn = builder.bargeIn;
        loop = builder.loop;
    }

    @Override
    public String getAction() {
        return "stream";
    }

    /**
     * Publicly accessible URL to an MP3 or 16-bit WAV audio file to stream into the Call or Conversation.
     *
     * @return The stream URL wrapped in a collection.
     */
    @JsonProperty("streamUrl")
    public Collection<URI> getStreamUrl() {
        return streamUrl;
    }

    /**
     * Set the audio level of the stream in the range between -1 and 1 inclusively with a precision of 0.1.
     * The default value is 0.
     *
     * @return The volume level that the audio is played at, or {@code null} if unspecified.
     */
    @JsonProperty("level")
    public Double getLevel() {
        return level;
    }

    /**
     * Determines whether this action is terminated when the user presses a button on the keypad. Use this
     * feature to enable users to choose an option without having to listen to the whole message in your
     * Interactive Voice Response (IVR). If you set bargeIn to true on one more Stream actions then the next
     * action in the NCCO stack must be an input action. The default value is {@code false}.
     *
     * @return Whether user input can terminate this action, or {@code null} if unspecified.
     */
    @JsonProperty("bargeIn")
    public Boolean getBargeIn() {
        return bargeIn;
    }

    /**
     * Number of times audio is repeated before the Call is closed. The default value is 1.
     * Set to 0 to loop infinitely.
     *
     * @return The number of times the audio stream is repeated, or {@code null} if unspecified.
     */
    @JsonProperty("loop")
    public Integer getLoop() {
        return loop;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @param streamUrl URL to an MP3 or 16-bit WAV audio file to stream into the Call or Conversation.
     *
     * @return A new Builder.
     */
    public static Builder builder(String streamUrl) {
        return builder().streamUrl(streamUrl);
    }

    /**
     * Entrypoint for constructing an instance of this class.
     * You must provide the stream URL by calling the {@linkplain Builder#streamUrl(String)} method.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to create a StreamAction. The stream URL is mandatory.
     */
    public static class Builder {
        private String streamUrl;
        private Double level;
        private Boolean bargeIn;
        private Integer loop;

        Builder() {}

        /**
         * URL to an MP3 or 16-bit WAV audio file to stream into the Call or Conversation.
         *
         * @param streamUrl The publicly accessible URL to the audio file to stream.
         *
         * @return This builder.
         */
        public Builder streamUrl(String streamUrl) {
            this.streamUrl = streamUrl;
            return this;
        }

        /**
         * Set the audio level of the stream in the range between -1 and 1 inclusively with a precision of 0.1.
         * The default value is 0.
         *
         * @param level The volume level that the audio is played at, between -1.0 and 1.0 (inclusive).
         *
         * @return This builder.
         */
        public Builder level(double level) {
            this.level = level;
            return this;
        }

        /**
         * Set to {@code true} so this action is terminated when the user presses a button on the keypad. Use this
         * feature to enable users to choose an option without having to listen to the whole message in your
         * Interactive Voice Response (IVR). If you set bargeIn to true on one more Stream actions then the next
         * action in the NCCO stack must be an input action. The default value is {@code false}.
         *
         * @param bargeIn Whether to allow user input to terminate this action.
         *
         * @return This builder.
         */
        public Builder bargeIn(boolean bargeIn) {
            this.bargeIn = bargeIn;
            return this;
        }

        /**
         * Number of times audio is repeated before the Call is closed. The default value is 1.
         * Set to 0 to loop infinitely.
         *
         * @param loop The number of times the audio stream is repeated (0 meaning infinite).
         *
         * @return This builder.
         */
        public Builder loop(int loop) {
            this.loop = loop;
            return this;
        }

        /**
         * Builds the StreamAction.
         *
         * @return A new SteamAction object from the stored builder options.
         */
        public StreamAction build() {
            return new StreamAction(this);
        }
    }
}
