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
 * An NCCO input action which allows for the collection of digits from a person.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputAction implements Action {
    private static final String ACTION = "input";

    @JsonProperty(required = true)
    private Collection<String> type;
    private Integer timeOut;
    private Integer maxDigits;
    private Boolean submitOnHash;
    private Collection<String> eventUrl;
    private SpeechSettings speech;
    private EventMethod eventMethod;

    /**
     * Do not use the 'new' operator to create a new InputAction. Instead use {@link InputAction.Builder} to build
     * the InputAction object.
     *
     * @param builder  builder to create InputAction object
     *
     * @deprecated Use {@link Builder}
     */
    @Deprecated
    public InputAction(Builder builder) {
        this.type = builder.type;
        this.timeOut = builder.timeOut;
        this.maxDigits = builder.maxDigits;
        this.submitOnHash = builder.submitOnHash;
        this.eventUrl = builder.eventUrl;
        this.eventMethod = builder.eventMethod;
        this.speech = builder.speech;
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    public Collection<String> getType(){
        return type;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public Integer getMaxDigits() {
        return maxDigits;
    }

    public Boolean getSubmitOnHash() {
        return submitOnHash;
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

    public void setSpeech(SpeechSettings speech) {
        this.speech = speech;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private Integer timeOut;
        private Integer maxDigits;
        private Boolean submitOnHash;
        private Collection<String> eventUrl;
        private EventMethod eventMethod;
        private SpeechSettings speech;
        private Collection<String> type;


        /**
         * @param timeOut The result of the callee's activity is sent to the eventUrl webhook endpoint timeOut seconds
         *                after the last action. The default value is 3. Max is 10.
         *
         * @return The {@link Builder} to keep building the input action.
         */
        public Builder timeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * @param maxDigits The number of digits the user can press. The maximum value is 20, the default is 4 digits.
         *
         * @return The {@link Builder} to keep building the input action.
         */
        public Builder maxDigits(Integer maxDigits) {
            this.maxDigits = maxDigits;
            return this;
        }

        /**
         * @param submitOnHash Set to true so the callee's activity is sent to your webhook endpoint at eventUrl after
         *                     he or she presses #. If # is not pressed the result is submitted after timeOut seconds.
         *                     The default value is false. That is, the result is sent to your webhook endpoint after
         *                     timeOut seconds.
         *
         * @return The {@link Builder} to keep building the input action.
         */
        public Builder submitOnHash(Boolean submitOnHash) {
            this.submitOnHash = submitOnHash;
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
         * @param speech Speech recognition settings object to enable speech input. Required if dtmf is not provided.
         * @return The {@link Builder} to keep building the input action.
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
