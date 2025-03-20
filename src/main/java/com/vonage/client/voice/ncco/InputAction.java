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
import java.util.*;

/**
 * An NCCO input action which allows for the collection of digits and automatic speech recognition from a person.
 */
public class InputAction extends JsonableBaseObject implements Action {
    private Collection<String> type;
    private DtmfSettings dtmf;
    private Collection<URI> eventUrl;
    private SpeechSettings speech;
    private EventMethod eventMethod;
    private InputMode mode;

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    InputAction() {}

    private InputAction(Builder builder) {
        eventUrl = builder.eventUrl != null ? Collections.singletonList(URI.create(builder.eventUrl)) : null;
        eventMethod = builder.eventMethod;
        speech = builder.speech;
        mode = builder.mode;
        if ((type = builder.type).isEmpty()) {
            throw new IllegalStateException("At least one input type must be specified.");
        }
        if ((dtmf = builder.dtmf) != null && mode == InputMode.ASYNCHRONOUS) {
            throw new IllegalStateException("DTMF settings cannot be used with asynchronous mode.");
        }
    }

    @Override
    public String getAction() {
        return "input";
    }

    /**
     * Input types that are acceptable for this action.
     *
     * @return The input types as a collection of strings. Valid values are ["dtmf"] for DTMF input only,
     * ["speech"] for ASR only, or ["dtmf", "speech"] for both.
     */
    @JsonProperty("type")
    public Collection<String> getType() {
        return type;
    }

    /**
     * DTMF settings for this action.
     *
     * @return The DTMF settings object, or {@code null} if unspecified.
     */
    @JsonProperty("dtmf")
    public DtmfSettings getDtmf() {
        return dtmf;
    }

    /**
     * Event URL for this action.
     *
     * @return The event URL wrapped in a singleton collection, or {@code null} if unspecified.
     */
    @JsonProperty("eventUrl")
    public Collection<URI> getEventUrl() {
        return eventUrl;
    }

    /**
     * Event method that will be used with the eventUrl, either {@code GET} or {@code POST}.
     *
     * @return The event HTTP method as an enum, or {@code null} if unspecified.
     */
    @JsonProperty("eventMethod")
    public EventMethod getEventMethod() {
        return eventMethod;
    }

    /**
     * Speech settings for this action.
     *
     * @return The speech settings object, or {@code null} if unspecified.
     */
    @JsonProperty("speech")
    public SpeechSettings getSpeech() {
        return speech;
    }

    /**
     * How the input should be processed. If not set, the default is {@linkplain InputMode#SYNCHRONOUS}.
     *
     * @return The mode as an enum, or {@code null} if unspecified.
     * @since 8.12.0
     */
    @JsonProperty("mode")
    public InputMode getMode() {
        return mode;
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
     * Builder for specifying the properties of an InputAction.
     */
    public static class Builder {
        private final Collection<String> type = new LinkedHashSet<>(2, 1f);
        private DtmfSettings dtmf;
        private String eventUrl;
        private EventMethod eventMethod;
        private SpeechSettings speech;
        private InputMode mode;

        private Builder() {}

        /**
         * Enables DTMF with the default settings.
         *
         * @return This builder to keep building the input action.
         * @since 8.12.0
         */
        public Builder dtmf() {
            dtmf(null);
            type.add("dtmf");
            return this;
        }

        /**
         * Enable DTMF input with the provided settings. Note that if you override any of
         * the defaults, you cannot set {@linkplain #mode(InputMode)} to {@linkplain InputMode#ASYNCHRONOUS}.
         *
         * @param dtmf The DTMF settings object.
         * @return This builder to keep building the input action.
         * @since 6.0.0
         */
        public Builder dtmf(DtmfSettings dtmf) {
            if ((this.dtmf = dtmf) != null) {
                type.add("dtmf");
            }
            else {
                type.remove("dtmf");
            }
            return this;
        }

        /**
         * Automatic Speech Recognition (ASR) settings to enable speech input.
         * Required if {@linkplain #dtmf(DtmfSettings)} is not provided.
         *
         * @param speech The speech settings object.
         * @return This builder to keep building the input action.
         * @since 6.0.0
         */
        public Builder speech(SpeechSettings speech) {
            if ((this.speech = speech) != null) {
                type.add("speech");
            }
            else {
                type.remove("speech");
            }
            return this;
        }

        /**
         * Vonage sends the digits pressed by the callee to this URL after timeOut pause inactivity or when
         * {@code #} is pressed.
         *
         * @param eventUrl The URL to send the event metadata to.
         *
         * @return This builder to keep building the input action.
         *
         * @since 8.12.0
         */
        public Builder eventUrl(String eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * The HTTP method used to send event information to event_url The default value is POST.
         *
         * @param eventMethod The HTTP method to use for the event as an enum.
         *
         * @return This builder to keep building the input action.
         */
        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        /**
         * How the input should be processed. If not set, the default is {@linkplain InputMode#SYNCHRONOUS}.
         * If set to {@linkplain InputMode#ASYNCHRONOUS}, use {@link #dtmf()} instead of {@link #dtmf(DtmfSettings)}.
         *
         * @param mode The DTMF processing mode as an enum.
         * @return This builder to keep building the input action.
         * @since 8.12.0
         */
        public Builder mode(InputMode mode) {
            this.mode = mode;
            return this;
        }

        /**
         * Builds the InputAction.
         *
         * @return A new InputAction object from the stored builder options.
         */
        public InputAction build() {
            return new InputAction(this);
        }
    }
}
