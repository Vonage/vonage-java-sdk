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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.*;

/**
 * An NCCO input action which allows for the collection of digits and automatic speech recognition from a person.
 */
public class InputAction extends JsonableBaseObject implements Action {
    private static final String ACTION = "input";

    @JsonProperty(required = true)
    private Collection<String> type;
    private DtmfSettings dtmf;
    private Collection<String> eventUrl;
    private SpeechSettings speech;
    private EventMethod eventMethod;
    private InputMode mode;

    InputAction() {}

    private InputAction(Builder builder) {
        eventUrl = builder.eventUrl;
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
        return ACTION;
    }

    @JsonProperty("type")
    public Collection<String> getType() {
        return type;
    }

    @JsonProperty("dtmf")
    public DtmfSettings getDtmf() {
        return dtmf;
    }

    @JsonProperty("eventUrl")
    public Collection<String> getEventUrl() {
        return eventUrl;
    }

    @JsonProperty("eventMethod")
    public EventMethod getEventMethod() {
        return eventMethod;
    }

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
        private Collection<String> eventUrl;
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
         * @param eventUrl The URL wrapped in a singleton collection to send the event metadata to.
         *
         * @return This builder to keep building the input action.
         * @deprecated This will be removed in a future release. Use {@link #eventUrl(String)} instead.
         */
        @Deprecated
        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * Vonage sends the digits pressed by the callee to this URL after timeOut pause inactivity or when
         * {@code #} is pressed.
         *
         * @param eventUrl The URL to send the event metadata to.
         *
         * @return This builder to keep building the input action.
         * @deprecated This will be removed in a future release. Use {@link #eventUrl(String)} instead.
         */
        @Deprecated
        public Builder eventUrl(String... eventUrl) {
            return eventUrl(Arrays.asList(eventUrl));
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
            return eventUrl(Collections.singletonList(eventUrl));
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
         * Sets the acceptable input types. From v8.12.0 onwards, you should not call this method manually;
         * instead, use {@link #dtmf()} and / or {@link #speech(SpeechSettings)}. This method will be removed
         * in a future release.
         *
         * @param type Acceptable input types as a collection. Valid values are ["dtmf"] for DTMF input only,
         *             ["speech"] for ASR only, or ["dtmf", "speech"] for both.
         *
         * @return This builder to keep building the input action.
         *
         * @deprecated Use {@link #dtmf(DtmfSettings)} and {@link #speech(SpeechSettings)} instead.
         * The type will be set automatically based on the provided settings.
         */
        @Deprecated
        public Builder type(Collection<String> type) {
            this.type.clear();
            if (type != null) {
                this.type.addAll(type);
            }
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
