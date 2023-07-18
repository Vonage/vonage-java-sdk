/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.common;

import com.fasterxml.jackson.annotation.*;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents the "webhooks" field used in Application capabilities.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Webhook {
    private String address;
    private HttpMethod method;
    private Integer connectionTimeout, socketTimeout;

    private Webhook(Builder builder) {
        if ((address = builder.address) == null || address.trim().isEmpty()) {
            throw new IllegalStateException("Address is required.");
        }
        if ((method = builder.method) == null) {
            throw new IllegalStateException("HTTP method is required.");
        }
        if ((connectionTimeout = builder.connectionTimeout) != null) {
            int min = 300, max = 1000;
            if (connectionTimeout < min || connectionTimeout > max) {
                throw new IllegalArgumentException(
                        "Connection timeout must be between "+min+" and "+max+" milliseconds."
                );
            }
        }
        if ((socketTimeout = builder.socketTimeout) != null) {
            int min = 1000, max = 10000;
            if (socketTimeout < min || socketTimeout > max) {
                throw new IllegalArgumentException(
                        "Socket timeout must be between "+min+" and "+max+" milliseconds."
                );
            }
        }
    }

    protected Webhook() {
    }

    public Webhook(String address, HttpMethod method) {
        this.address = address;
        this.method = method;
    }

    /**
     * The webhook's URL.
     *
     * @return The URL as a string.
     */
    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    /**
     * The HTTP request method for this webhook.
     *
     * @return The HTTP method as an enum.
     */
    @JsonProperty("http_method")
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * If Vonage can't connect to the webhook URL for this specified amount of time, then Vonage makes one
     * additional attempt to connect to the webhook endpoint. This is an integer value specified in milliseconds.
     * The minimum is 300, maximum 1000 and default is 1000.
     *
     * @return The connection timeout in milliseconds as an integer, or {@code null}
     * if unspecified (the default) / not applicable.
     */
    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * If a response from the webhook URL can't be read for this specified amount of time, then Vonage makes one
     * additional attempt to read the webhook endpoint. This is an integer value specified in milliseconds.
     * The minimum is 1000, maximum 5000 and default is 5000.
     *
     * @return The socket timeout in milliseconds as an integer, or {@code null}
     * if unspecified (the default) / not applicable.
     */
    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @return A new Builder.
     * @since 7.7.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for configuring Webhook object.
     *
     * @since 7.7.0
     */
    public static class Builder {
        private String address;
        private HttpMethod method;
        private Integer connectionTimeout, socketTimeout;

        Builder() {}

        /**
         * (REQUIRED) The webhook's URL.
         *
         * @param address The address as a string.
         *
         * @return This builder.
         */
        public Builder address(String address) {
            this.address = address;
            return this;
        }

        /**
         * (REQUIRED) The HTTP request method for this webhook.
         *
         * @param method The HTTP method as an enum.
         *
         * @return This builder.
         */
        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        /**
         * (OPTIONAL) If Vonage can't connect to the webhook URL for this specified amount of time, then Vonage
         * makes one additional attempt to connect to the webhook endpoint. This is an integer value specified
         * in milliseconds. The minimum is 300, maximum 1000 and default is 1000.
         *
         * @param connectionTimeout The connection timeout in milliseconds.
         *
         * @return This builder.
         */
        public Builder connectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        /**
         * (OPTIONAL)  If a response from the webhook URL can't be read for this specified amount of time, then
         * Vonage makes one additional attempt to read the webhook endpoint. This is an integer value specified
         * in milliseconds. The minimum is 1000, maximum 5000 and default is 5000.
         *
         * @param socketTimeout The socket timeout in milliseconds.
         *
         * @return This builder.
         */
        public Builder socketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        /**
         * Builds the Webhook object.
         *
         * @return A new Webhook instance with this builder's properties.
         */
        public Webhook build() {
            return new Webhook(this);
        }
    }

    /**
     * Represents the webhook URL type.
     */
    public enum Type {
        ANSWER("answer_url"),
        FALLBACK_ANSWER("fallback_answer_url"),
        EVENT("event_url"),
        INBOUND("inbound_url"),
        STATUS("status_url"),
        UNKNOWN("unknown");

        private final String name;

        private static final Map<String, Type> TYPE_INDEX =
            Arrays.stream(Type.values()).collect(Collectors.toMap(
                    Type::getName, Function.identity()
            ));

        Type(String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }

        @JsonCreator
        public static Type fromName(String name) {
            return TYPE_INDEX.getOrDefault(name.toLowerCase(), UNKNOWN);
        }
    }
}
