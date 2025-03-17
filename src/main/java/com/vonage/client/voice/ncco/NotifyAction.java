/*
 *   Copyright 2025 Vonage
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
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * An NCCO notify action which allows for custom events to be sent to a configured webhook.
 */
public class NotifyAction extends JsonableBaseObject implements Action {
    private Map<String, ?> payload;
    private Collection<URI> eventUrl;
    private EventMethod eventMethod;

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    NotifyAction() {}

    private NotifyAction(Builder builder) {
        payload = builder.payload;
        eventUrl = builder.eventUrl != null ? Collections.singleton(URI.create(builder.eventUrl)) : null;
        eventMethod = builder.eventMethod;
    }

    @Override
    public String getAction() {
        return "notify";
    }

    /**
     * Map of custom keys and values that will be converted to JSON and sent to your event URL.
     *
     * @return The payload as a Map.
     */
    @JsonProperty("payload")
    public Map<String, ?> getPayload() {
        return payload;
    }

    /**
     * Webhook URL to send events to.
     *
     * @return The event URL wrapped as a singleton collection.
     */
    @JsonProperty("eventUrl")
    public Collection<URI> getEventUrl() {
        return eventUrl;
    }

    /**
     * HTTP method to use when sending the payload to your event URL.
     *
     * @return The event HTTP method as an enum, or {@code null} if unspecified.
     */
    @JsonProperty("eventMethod")
    public EventMethod getEventMethod() {
        return eventMethod;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @param payload The payload to send to the event URL as a Map.
     * @param eventUrl The event URL to send the payload to.
     *
     * @return A new Builder.
     */
    public static Builder builder(Map<String, ?> payload, String eventUrl) {
        return builder().payload(payload).eventUrl(eventUrl);
    }

    /**
     * Entrypoint for constructing an instance of this class.
     * You must specify the payload and eventUrl fields using the builder's methods.
     *
     * @return A new Builder.
     * @since 8.17.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to create a NotifyAction. The payload and eventUrl fields are mandatory.
     */
    public static class Builder {
        private Map<String, ?> payload;
        private String eventUrl;
        private EventMethod eventMethod;

        private Builder() {
        }

        /**
         * Map of custom keys and values that will be converted to JSON and sent to your event URL.
         *
         * @param payload The action payload as a Map.
         *
         * @return This builder.
         */
        public Builder payload(Map<String, ?> payload) {
            this.payload = payload;
            return this;
        }

        /**
         * Webhook URL to send events to.
         *
         * @param eventUrl The event webhook URL as a string.
         *
         * @return This builder.
         */
        public Builder eventUrl(String eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * HTTP method to use when sending the payload to your event URL; either {@code GET} or {@code POST}.
         *
         * @param eventMethod The event HTTP method as an enum.
         *
         * @return This builder.
         */
        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        /**
         * Builds the NotifyAction.
         *
         * @return A new NotifyAction object from the stored builder options.
         */
        public NotifyAction build() {
            return new NotifyAction(this);
        }
    }
}
