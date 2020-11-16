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
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Collection;

/**
 * An NCCO input action which allows for the collection of digits and automatic speech recognition from a person.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputAction implements Action {
    private static final String ACTION = "input";

    @JsonProperty(required = true)
    private Collection<String> type;
    private DtmfSettings dtmf;
    private Collection<String> eventUrl;
    private SpeechSettings speech;
    private EventMethod eventMethod;

    /**
     * @param builder  builder to create InputAction object
     */
    private InputAction(Builder builder) {
        type = builder.type;
        dtmf = builder.dtmf;
        eventUrl = builder.eventUrl;
        eventMethod = builder.eventMethod;
        speech = builder.speech;
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    public Collection<String> getType(){
        return type;
    }

    public DtmfSettings getDtmf() {
        return dtmf;
    }

    public Collection<String> getEventUrl() {
        return eventUrl;
    }

    public EventMethod getEventMethod() {
        return eventMethod;
    }

    public SpeechSettings getSpeech() {
        return speech;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private DtmfSettings dtmf;
        private Collection<String> eventUrl;
        private EventMethod eventMethod;
        private SpeechSettings speech;
        private Collection<String> type;

        /**
         * @param dtmf DTMF settings object to enable DTMF input.
         * @return The {@link Builder} to keep building the input action.
         * @since 6.0.0
         */
        public Builder dtmf(DtmfSettings dtmf) {
            this.dtmf = dtmf;
            return this;
        }

        /**
         * @param eventUrl Vonage sends the digits pressed by the callee to this URL after timeOut pause in activity or
         *                 when # is pressed.
         *
         * @return The {@link Builder} to keep building the input action.
         */
        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * @param eventUrl Vonage sends the digits pressed by the callee to this URL after timeOut pause in activity or
         *                 when # is pressed.
         *
         * @return The {@link Builder} to keep building the input action.
         */
        public Builder eventUrl(String... eventUrl) {
            return eventUrl(Arrays.asList(eventUrl));
        }

        /**
         * @param eventMethod The HTTP method used to send event information to event_url The default value is POST.
         *
         * @return The {@link Builder} to keep building the input action.
         */
        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        /**
         * @param speech Automatic speech recognition settings object to enable speech input. Required if dtmf is not provided.
         * @return The {@link Builder} to keep building the input action.
         * @since 6.0.0
         */
        public Builder speech(SpeechSettings speech){
            this.speech = speech;
            return this;
        }

        /**
         * @param type Acceptable input type, can be set as [ "dtmf" ] for DTMF input only, [ "speech" ] for ASR only,
         *            or [ "dtmf", "speech" ] for both.
         * @return The {@link Builder} to keep building the input action.
         */
        public Builder type(Collection<String> type){
            this.type = type;
            return this;
        }

        /**
         * @return A new {@link InputAction} object from the stored builder options.
         */
        public InputAction build() {
            return new InputAction(this);
        }
    }
}
