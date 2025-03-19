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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.JsonableBaseObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * ASR (Automatic Speech Recognition) settings for Input Actions that will be added to a NCCO object.
 */
public class SpeechSettings extends JsonableBaseObject {
    private Collection<String> uuid, context;
    private Double endOnSilence;
    private Integer startTimeout, maxDuration, sensitivity;
    private Language language;
    private Boolean saveAudio;

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    SpeechSettings() {}

    private SpeechSettings(Builder builder) {
        if (builder.uuid != null) {
            uuid = Collections.singletonList(builder.uuid);
        }
        if ((endOnSilence = builder.endOnSilence) != null && (endOnSilence < 0.4 || endOnSilence > 10)) {
            throw new IllegalArgumentException("endOnSilence must be between 0.4 and 10.");
        }
        if ((startTimeout = builder.startTimeout) != null && (startTimeout < 1 || startTimeout > 60)) {
            throw new IllegalArgumentException("startTimeout must be between 1 and 60.");
        }
        if ((maxDuration = builder.maxDuration) != null && (maxDuration < 1 || maxDuration > 60)) {
            throw new IllegalArgumentException("maxDuration must be between 1 and 60.");
        }
        if ((sensitivity = builder.sensitivity) != null && (sensitivity < 10 || sensitivity > 100)) {
            throw new IllegalArgumentException("sensitivity must be between 10 and 100.");
        }
        context = builder.context;
        language = builder.language;
        saveAudio = builder.saveAudio;
    }

    /**
     * Unique ID of the Call leg for the user to capture the speech of. The first joined leg of the call by default.
     *
     * @return The call ID wrapped in a string collection, or {@code null} if unspecified.
     */
    @JsonProperty("uuid")
    public Collection<String> getUuid() {
        return uuid;
    }

    /**
     * Expected language of the user's speech.
     *
     * @return The language as an enum, or {@code null} if unspecified.
     */
    @JsonProperty("language")
    public Language getLanguage() {
        return language;
    }

    /**
     * List of hints to improve recognition quality of certain words are expected from the user.
     *
     * @return The collection of hint strings, or {@code null} if unspecified.
     */
    @JsonProperty("context")
    public Collection<String> getContext() {
        return context;
    }

    /**
     * Controls how long the system will wait after user stops speaking to decide the input is completed.
     *
     * @return The input completion wait time in seconds as a double, or {@code null} if unspecified.
     */
    @JsonProperty("endOnSilence")
    public Double getEndOnSilence() {
        return endOnSilence;
    }

    /**
     * Controls how long the system will wait for the user to start speaking.
     *
     * @return The initial speech timeout in seconds as an integer, or {@code null} if unspecified.
     */
    @JsonProperty("startTimeout")
    public Integer getStartTimeout() {
        return startTimeout;
    }

    /**
     * Controls maximum speech duration from the moment user starts speaking.
     *
     * @return The maximum speech duration in seconds as an integer, or {@code null} if unspecified.
     */
    @JsonProperty("maxDuration")
    public Integer getMaxDuration() {
        return maxDuration;
    }

    /**
     * Audio sensitivity used to differentiate noise from speech.
     *
     * @return The audio sensitivity as an integer, or {@code null} if unspecified.
     */
    @JsonProperty("sensitivity")
    public Integer getSensitivity() {
        return sensitivity;
    }

    /**
     * Controls whether the speech input recording is sent to your webhook endpoint at eventUrl.
     *
     * @return Whether to send the speech input to the event webhook, or {@code null} if unspecified.
     */
    @JsonProperty("saveAudio")
    public Boolean getSaveAudio() {
        return saveAudio;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     * @since 8.2.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for customising SpeechSettings parameters. All fields are optional.
     *
     * @since 8.2.0
     */
    public static final class Builder {
        private String uuid;
        private Collection<String> context;
        private Integer startTimeout, maxDuration, sensitivity;
        private Double endOnSilence;
        private Language language;
        private Boolean saveAudio;

        private Builder() {}

        /**
         * The unique ID of the Call leg for the user to capture the speech of.
         * The first joined leg of the call by default.
         *
         * @param uuid The call leg ID to capture as a string.
         * @return This builder.
         */
        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        /**
         * Hints to improve recognition quality of certain words are expected from the user.
         *
         * @param context The collection of hint strings.
         * @return This builder.
         *
         * @see #context(String...)
         */
        public Builder context(Collection<String> context) {
            this.context = context;
            return this;
        }

        /**
         * Hints to improve recognition quality of certain words are expected from the user.
         *
         * @param context The hint strings.
         * @return This builder.
         * @see #context(Collection)
         */
        public Builder context(String... context) {
            return context(Arrays.asList(context));
        }

        /**
         * Controls how long the system will wait after user stops speaking to decide the input is completed.
         * The default value is 2.0 (seconds). The range of possible values is between 0.4 and 10.0 seconds.
         *
         * @param endOnSilence The input completion wait time in seconds as a double.
         * @return This builder.
         */
        public Builder endOnSilence(double endOnSilence) {
            this.endOnSilence = endOnSilence;
            return this;
        }

        /**
         * Controls how long the system will wait for the user to start speaking. The range of possible values
         * is between 1 second and 60 seconds. The default value is 10.
         *
         * @param startTimeout The initial speech timeout in seconds as an integer.
         * @return This builder.
         */
        public Builder startTimeout(int startTimeout) {
            this.startTimeout = startTimeout;
            return this;
        }

        /**
         * Controls maximum speech duration (from the moment user starts speaking). The default value is
         * 60 (seconds). The range of possible values is between 1 and 60 seconds.
         * 
         * @param maxDuration The maximum speech duration in seconds as an integer.
         * @return This builder.
         */
        public Builder maxDuration(int maxDuration) {
            this.maxDuration = maxDuration;
            return this;
        }

        /**
         * Audio sensitivity used to differentiate noise from speech. An integer value where 10 represents
         * low sensitivity and 100 maximum sensitivity. Default is 90.
         *
         * @param sensitivity The audio sensitivity as an integer.
         * @return This builder.
         */
        public Builder sensitivity(int sensitivity) {
            this.sensitivity = sensitivity;
            return this;
        }

        /**
         * Expected language of the user's speech. Default is {@link Language#ENGLISH_UNITED_STATES}.
         *
         * @param language The expected speech language as an enum.
         * @return This builder.
         */
        public Builder language(Language language) {
            this.language = language;
            return this;
        }

        /**
         * Controls whether the speech input recording ({@code recording_url}) is sent to your webhook
         * endpoint at {@code eventUrl}. The default value is {@code false}.
         *
         * @param saveAudio {@code true} to send the speech input to the event webhook.
         * @return This builder.
         */
        public Builder saveAudio(boolean saveAudio) {
            this.saveAudio = saveAudio;
            return this;
        }

        /**
         * Builds the SpeechSettings object with this builder's properties.
         *
         * @return A new SpeechSettings instance.
         */
        public SpeechSettings build() {
            return new SpeechSettings(this);
        }
    }
}
