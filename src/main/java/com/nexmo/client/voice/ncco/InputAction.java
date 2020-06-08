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
    private SpeechSettings speech;
    private DTMFSettings dtmf;
    private EventMethod eventMethod;

    /**
     * Do not use the 'new' operator to create a new InputAction. Instead use InputAction.Builder to build the InputAction
     * object.
     *
     * @param builder  builder to create InputAction object
     *
     * @deprecated Use {@link Builder}
     */
    @Deprecated
    public InputAction(Builder builder) {
        this.timeOut = builder.timeOut;
        this.maxDigits = builder.maxDigits;
        this.submitOnHash = builder.submitOnHash;
        this.eventUrl = builder.eventUrl;
        this.eventMethod = builder.eventMethod;
        this.speech = builder.speech;
        this.dtmf = builder.dtmf;
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    @Deprecated
    public Integer getTimeOut() {
        return timeOut;
    }

    @Deprecated
    public Integer getMaxDigits() {
        return maxDigits;
    }

    @Deprecated
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

    public SpeechSettings getSpeech() {
        return speech;
    }

    public void setSpeech(SpeechSettings speech) {
        this.speech = speech;
    }

    public DTMFSettings getDtmf() {
        return dtmf;
    }

    public void setDtmf(DTMFSettings dtmf) {
        this.dtmf = dtmf;
    }

    public static class Builder {
        private Integer timeOut;
        private Integer maxDigits;
        private Boolean submitOnHash;
        private Collection<String> eventUrl;
        private EventMethod eventMethod;
        private SpeechSettings speech;
        private DTMFSettings dtmf;


        /**
         * @param timeOut
         * @deprecated use {@link DTMFSettings#setTimeOut(Integer)}
         * @return {@link Builder}
         */
        @Deprecated
        public Builder timeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * @deprecated use {@link DTMFSettings#setMaxDigits(Integer)}
         * @return {@link Builder}
         */
        @Deprecated
        public Builder maxDigits(Integer maxDigits) {
            this.maxDigits = maxDigits;
            return this;
        }

        /**
         * @deprecated use {@link DTMFSettings#setSubmitOnHash(Boolean)}
         * @return {@link Builder}
         */
        @Deprecated
        public Builder submitOnHash(Boolean submitOnHash) {
            this.submitOnHash = submitOnHash;
            return this;
        }

        /**
         * @param eventUrl Nexmo sends the digits pressed by the callee to this URL after timeOut pause in activity or
         *                 when # is pressed.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * @param eventUrl Nexmo sends the digits pressed by the callee to this URL after timeOut pause in activity or
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
         *
         * @param speech Speech recognition settings object to enable speech input. Required if dtmf is not provided.
         * @return The {@link Builder} to keep building.
         */
        public Builder speech(SpeechSettings speech){
            this.speech = speech;
            return this;
        }

        /**
         *
         * @param dtmf DTMF settings object to enable DTMF input. Required if speech is not provided.
         * @return The {@link Builder} to keep building.
         */
        public Builder dtmf(DTMFSettings dtmf){
            this.dtmf = dtmf;
            return this;
        }

        /**
         * @return A new {@link InputAction} object from the stored builder options.
         */
        public InputAction build() {
            return new InputAction(this);
        }
    }

//    @Override
//    public String toString() {
//        return "InputAction{" +
//                "timeOut=" + timeOut +
//                ", maxDigits=" + maxDigits +
//                ", submitOnHash=" + submitOnHash +
//                ", eventUrl=" + eventUrl +
//                ", speech=" + speech +
//                ", dtmf=" + dtmf +
//                ", eventMethod=" + eventMethod +
//                '}';
//    }
}
