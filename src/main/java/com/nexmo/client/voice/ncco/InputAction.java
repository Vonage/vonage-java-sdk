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

    public static class Builder {
        private Integer timeOut = null;
        private Integer maxDigits = null;
        private Boolean submitOnHash = null;
        private Collection<String> eventUrl = null;
        private EventMethod eventMethod = null;

        public Builder timeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public Builder maxDigits(Integer maxDigits) {
            this.maxDigits = maxDigits;
            return this;
        }

        public Builder submitOnHash(Boolean submitOnHash) {
            this.submitOnHash = submitOnHash;
            return this;
        }

        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        public Builder eventUrl(String... eventUrl) {
            return eventUrl(Arrays.asList(eventUrl));
        }

        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        public InputAction build() {
            return new InputAction(this);
        }
    }
}
