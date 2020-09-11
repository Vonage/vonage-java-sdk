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
 * An NCCO input action which allows for the collection of digits from a person.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputAction implements Action {
    private static final String ACTION = "input";

    private Integer timeOut;
    private Integer maxDigits;
    private Boolean submitOnHash;
    private Collection<String> eventUrl;
    private EventMethod eventMethod;

    /**
     * @deprecated Use {@link Builder}
     */
    @Deprecated
    public InputAction(Builder builder) {
        this.timeOut = builder.timeOut;
        this.maxDigits = builder.maxDigits;
        this.submitOnHash = builder.submitOnHash;
        this.eventUrl = builder.eventUrl;
        this.eventMethod = builder.eventMethod;
    }

    @Override
    public String getAction() {
        return ACTION;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer timeOut = null;
        private Integer maxDigits = null;
        private Boolean submitOnHash = null;
        private Collection<String> eventUrl = null;
        private EventMethod eventMethod = null;

        /**
         * @param timeOut The result of the callee's activity is sent to the eventUrl webhook endpoint timeOut seconds
         *                after the last action. The default value is 3. Max is 10.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder timeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * @param maxDigits The number of digits the user can press. The maximum value is 20, the default is 4 digits.
         *
         * @return The {@link Builder} to keep building.
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
         * @return The {@link Builder} to keep building.
         */
        public Builder submitOnHash(Boolean submitOnHash) {
            this.submitOnHash = submitOnHash;
            return this;
        }

        /**
         * @param eventUrl Vonage sends the digits pressed by the callee to this URL after timeOut pause in activity or
         *                 when # is pressed.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * @param eventUrl Vonage sends the digits pressed by the callee to this URL after timeOut pause in activity or
         *                 when # is pressed.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventUrl(String... eventUrl) {
            return eventUrl(Arrays.asList(eventUrl));
        }

        /**
         * @param eventMethod The HTTP method used to send event information to event_url The default value is POST.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
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
