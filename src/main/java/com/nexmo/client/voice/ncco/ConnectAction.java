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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexmo.client.voice.MachineDetection;

import java.util.Arrays;
import java.util.Collection;

/**
 * An NCCO connect action that allows for the establishment of a connection to various {@link Endpoint}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectAction implements Action {
    private static final String ACTION = "connect";

    private Collection<Endpoint> endpoint;
    private String from;
    private EventType eventType;
    private Integer timeOut;
    private Integer limit;
    private MachineDetection machineDetection;
    private Collection<String> eventUrl;
    private EventMethod eventMethod;

    private ConnectAction(Builder builder) {
        this.endpoint = builder.endpoint;
        this.from = builder.from;
        this.eventType = builder.eventType;
        this.timeOut = builder.timeOut;
        this.limit = builder.limit;
        this.machineDetection = builder.machineDetection;
        this.eventUrl = builder.eventUrl;
        this.eventMethod = builder.eventMethod;
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    public Collection<Endpoint> getEndpoint() {
        return endpoint;
    }

    public String getFrom() {
        return from;
    }

    public EventType getEventType() {
        return eventType;
    }

    @JsonProperty("timeout")
    public Integer getTimeOut() {
        return timeOut;
    }

    public Integer getLimit() {
        return limit;
    }

    public MachineDetection getMachineDetection() {
        return machineDetection;
    }

    public Collection<String> getEventUrl() {
        return eventUrl;
    }

    public EventMethod getEventMethod() {
        return eventMethod;
    }

    public static class Builder {
        private Collection<Endpoint> endpoint;
        private String from = null;
        private EventType eventType = null;
        private Integer timeOut = null;
        private Integer limit = null;
        private MachineDetection machineDetection = null;
        private Collection<String> eventUrl = null;
        private EventMethod eventMethod = null;

        /**
         * @param endpoint Connect the call to a specific #{@link Endpoint}.
         */
        public Builder(Collection<Endpoint> endpoint) {
            this.endpoint = endpoint;
        }

        /**
         * @param endpoint Connect the call to a specific #{@link Endpoint}.
         */
        public Builder(Endpoint... endpoint) {
            this(Arrays.asList(endpoint));
        }

        /**
         * @param endpoint Connect the call to a specific #{@link Endpoint}.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder endpoint(Collection<Endpoint> endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        /**
         * @param endpoint Connect the call to a specific #{@link Endpoint}.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder endpoint(Endpoint... endpoint) {
            return endpoint(Arrays.asList(endpoint));
        }

        /**
         * @param from A number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format that identifies the caller.
         *             <p>
         *             This must be one of your Nexmo virtual numbers, another value will result in the caller ID being unknown.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder from(String from) {
            this.from = from;
            return this;
        }

        /**
         * @param eventType Set to {@link EventType#SYNCHRONOUS} to:
         *                  <ul>
         *                  <li>Make the connect action synchronous.
         *                  <li>Enable eventUrl to return an NCCO that overrides the current NCCO when a call moves to
         *                  specific states.
         *                  <ul>
         *                  <p>
         *                  See the <a href="https://developer.nexmo.com/voice/voice-api/ncco-reference#connect-with-fallback-ncco">Connect with fallback NCCO example.</a>
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        /**
         * @param timeOut If the call is unanswered, set the number in seconds before Nexmo stops ringing endpoint.
         *                The default value is 60.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder timeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * @param limit Maximum length of the call in seconds. The default and maximum value is 7200 seconds (2 hours).
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        /**
         * @param machineDetection Configure the behavior when Nexmo detects that a destination is an answerphone.
         *                         <p>
         *                         Set to either:
         *                         <ul>
         *                         <li> {@link MachineDetection#CONTINUE} Nexmo sends an HTTP request to event_url with the Call event machine
         *                         <li> {@link MachineDetection#HANGUP} to end the Call
         *                         </ul>
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder machineDetection(MachineDetection machineDetection) {
            this.machineDetection = machineDetection;
            return this;
        }

        /**
         * @param eventUrl Set the webhook endpoint that Nexmo calls asynchronously on each of the possible <a href="https://developer.nexmo.com/voice/voice-api/guides/call-flow#call-states">Call States</a>.
         *                 If eventType is set to synchronous the eventUrl can return an NCCO that overrides the current
         *                 NCCO when a timeout occurs.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * @param eventUrl Set the webhook endpoint that Nexmo calls asynchronously on each of the possible <a href="https://developer.nexmo.com/voice/voice-api/guides/call-flow#call-states">Call States</a>.
         *                 If eventType is set to synchronous the eventUrl can return an NCCO that overrides the current
         *                 NCCO when a timeout occurs.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventUrl(String... eventUrl) {
            return eventUrl(Arrays.asList(eventUrl));
        }

        /**
         * @param eventMethod The HTTP method Nexmo uses to make the request to eventUrl. The default value is POST.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        /**
         * @return A new {@link ConnectAction} object from the stored builder options.
         */
        public ConnectAction build() {
            return new ConnectAction(this);
        }
    }
}
