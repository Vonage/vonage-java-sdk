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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents authorization configuration for WebSocket connections in Voice API.
 * Defines how the Authorization HTTP header is set in the WebSocket opening handshake.
 *
 * @since 9.1.0
 */
public class Authorization extends JsonableBaseObject {
    private static final int MAX_VALUE_LENGTH = 8191;
    private static final Pattern VALUE_PATTERN = Pattern.compile("^[\\x20-\\x7E]+$");

    private final Type type;
    private final String value;

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    Authorization() {
        this.type = null;
        this.value = null;
    }

    private Authorization(Builder builder) {
        this.type = Objects.requireNonNull(builder.type, "Authorization type is required.");
        
        if (type == Type.CUSTOM) {
            this.value = Objects.requireNonNull(builder.value, 
                "Authorization value is required when type is 'custom'.");
            
            if (value.length() > MAX_VALUE_LENGTH) {
                throw new IllegalArgumentException(
                    "Authorization value must not exceed " + MAX_VALUE_LENGTH + " characters.");
            }
            
            if (!VALUE_PATTERN.matcher(value).matches()) {
                throw new IllegalArgumentException(
                    "Authorization value must contain only printable ASCII characters (\\x20-\\x7E).");
            }
        } else {
            // For type 'vonage', ignore any provided value
            this.value = builder.value;
        }
    }

    /**
     * The authorization mode.
     *
     * @return The authorization type.
     */
    @JsonProperty("type")
    public Type getType() {
        return type;
    }

    /**
     * The raw header value to include in the Authorization header.
     * Required only when type is {@link Type#CUSTOM}. Ignored when type is {@link Type#VONAGE}.
     *
     * @return The authorization value, or {@code null} if not set.
     */
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    /**
     * Creates a new Authorization with type 'vonage'.
     * The Voice API will include the same JWT used for signed webhooks in the Authorization header.
     *
     * @return A new Authorization instance with type 'vonage'.
     */
    public static Authorization vonage() {
        return builder(Type.VONAGE).build();
    }

    /**
     * Creates a new Authorization with type 'custom'.
     * The provided value will be sent verbatim in the Authorization header.
     *
     * @param value The raw header value to include, e.g. "Bearer abc123" or "ApiKey X9Z...".
     *              Must be less than 8192 characters and contain only printable ASCII characters.
     *
     * @return A new Authorization instance with type 'custom'.
     */
    public static Authorization custom(String value) {
        return builder(Type.CUSTOM).value(value).build();
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param type The authorization type.
     *
     * @return A new Builder.
     */
    public static Builder builder(Type type) {
        return new Builder(type);
    }

    /**
     * Authorization type enum.
     */
    public enum Type {
        /**
         * The Voice API includes the same JWT used for signed webhooks in the Authorization header
         * ("Bearer &lt;JWT&gt;").
         */
        @JsonProperty("vonage") VONAGE,

        /**
         * A developer-supplied Authorization header value is sent verbatim.
         */
        @JsonProperty("custom") CUSTOM;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    /**
     * Builder for constructing an Authorization instance.
     */
    public static class Builder {
        private final Type type;
        private String value;

        Builder(Type type) {
            this.type = type;
        }

        /**
         * Sets the raw header value to include in the Authorization header.
         * Required only when type is {@link Type#CUSTOM}. Ignored when type is {@link Type#VONAGE}.
         *
         * @param value The raw header value.
         *
         * @return This builder.
         */
        public Builder value(String value) {
            this.value = value;
            return this;
        }

        /**
         * Builds the Authorization with this builder's properties.
         *
         * @return A new Authorization instance.
         */
        public Authorization build() {
            return new Authorization(this);
        }
    }
}
