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

        public Builder(Collection<Endpoint> endpoint) {
            this.endpoint = endpoint;
        }

        public Builder(Endpoint... endpoint) {
            this(Arrays.asList(endpoint));
        }

        public Builder endpoint(Collection<Endpoint> endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder endpoint(Endpoint... endpoint) {
            return endpoint(Arrays.asList(endpoint));
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder timeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder machineDetection(MachineDetection machineDetection) {
            this.machineDetection = machineDetection;
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

        public ConnectAction build() {
            return new ConnectAction(this);
        }
    }
}
