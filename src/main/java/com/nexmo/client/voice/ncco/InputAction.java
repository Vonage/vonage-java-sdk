/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
         * @param eventUrl Nexmo sends the digits pressed by the callee to this URL after timeOut pause in activity
         *                 or when # is pressed.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * @param eventUrl Nexmo sends the digits pressed by the callee to this URL after timeOut pause in activity
         *                 or when # is pressed.
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
