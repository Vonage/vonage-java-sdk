/*
 * Copyright (c) 2011-2019 Nexmo Inc
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
import java.util.Map;

/**
 * An NCCO notify action which allows for custom events to be sent to a configured webhook.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotifyAction implements Action {
    private static final String ACTION = "notify";

    private Map<String, ?> payload;
    private Collection<String> eventUrl;
    private EventMethod eventMethod;

    private NotifyAction(Builder builder) {
        this.payload = builder.payload;
        this.eventUrl = builder.eventUrl;
        this.eventMethod = builder.eventMethod;
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    public Map getPayload() {
        return payload;
    }

    public Collection<String> getEventUrl() {
        return eventUrl;
    }

    public EventMethod getEventMethod() {
        return eventMethod;
    }

    public static Builder builder(Map<String, ?> payload, Collection<String> eventUrl) {
        return new Builder(payload, eventUrl);
    }

    public static Builder builder(Map<String, ?> payload, String... eventUrl) {
        return new Builder(payload, eventUrl);
    }

    public static class Builder {
        private Map<String, ?> payload;
        private Collection<String> eventUrl;
        private EventMethod eventMethod = null;

        /**
         * @param payload  A Map of String keys and ? values that will be converted to JSON and sent to your event URL.
         * @param eventUrl The URL to send events to.
         */
        public Builder(Map<String, ?> payload, Collection<String> eventUrl) {
            this.payload = payload;
            this.eventUrl = eventUrl;
        }

        /**
         * @param payload  A Map of String keys and ? values that will be converted to JSON and sent to your event URL.
         * @param eventUrl The URL to send events to.
         */
        public Builder(Map<String, ?> payload, String... eventUrl) {
            this(payload, Arrays.asList(eventUrl));
        }

        /**
         * @param payload A Map of String keys and ? values that will be converted to JSON and sent to your event URL.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder payload(Map<String, ?> payload) {
            this.payload = payload;
            return this;
        }

        /**
         * @param eventUrl The URL to send events to.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * @param eventUrl The URL to send events to.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventUrl(String... eventUrl) {
            return eventUrl(Arrays.asList(eventUrl));
        }

        /**
         * @param eventMethod The HTTP method to use when sending the payload to your event url.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        /**
         * @return A new {@link NotifyAction} object from the stored builder options.
         */
        public NotifyAction build() {
            return new NotifyAction(this);
        }
    }
}
