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
package com.vonage.client.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.users.channels.Channels;
import java.net.URI;
import java.util.Map;

/**
 * Represents a Vonage User (both request and response).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Jsonable {
    private String id, name, displayName;
    private URI imageUrl;
    private Properties properties;
    private Channels channels;

    User() {
    }

    User(Builder builder) {
        name = builder.name;
        displayName = builder.displayName;
        imageUrl = builder.imageUrl;
        channels = builder.channels;
        if (builder.customData != null) {
            properties = new Properties();
            properties.customData = builder.customData;
        }
    }

    /**
     * Unique user ID.
     *
     * @return The user ID as a string, or {@code null} if unknown.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Unique name of the user.
     *
     * @return The user's name.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * A string to be displayed as username. It does not need to be unique.
     *
     * @return The user's friendly name.
     */
    @JsonProperty("display_name")
    public String getDisplayName() {
        return displayName;
    }

    /**
     * An image URL to associate with the user.
     *
     * @return The image URL, or {@code null} if not specified.
     */
    @JsonProperty("image_url")
    public URI getImageUrl() {
        return imageUrl;
    }

    /**
     * Additional properties for the user.
     *
     * @return The properties object, or {@code null} if not applicable.
     */
    @JsonProperty("properties")
    public Properties getProperties() {
        return properties;
    }

    /**
     * The communication channels available to the user.
     *
     * @return The channels object, or {@code null} if unknown.
     */
    @JsonProperty("channels")
    public Channels getChannels() {
        return channels;
    }

    /**
     * Entry point for creating an instance of this class.
     * 
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name, displayName;
        private URI imageUrl;
        private Map<String, Object> customData;
        private final Channels channels = new Channels();

        Builder() {}

        /**
         * Unique name for a user.
         *
         * @param name The user's name.
         *
         * @return This builder.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Set the display name for the user. This does nbot need to be unique.
         *
         * @param displayName The user's display name.
         *
         * @return This builder.
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * An image URL to associate with the user.
         *
         * @param imageUrl The image URL as a string.
         *
         * @return This builder.
         */
        public Builder imageUrl(String imageUrl) {
            this.imageUrl = URI.create(imageUrl);
            return this;
        }

        /**
         * Additional freeform data to associate with the user.
         *
         * @param customData Custom key/value pairs map.
         *
         * @return This builder.
         */
        public Builder customData(Map<String, Object> customData) {
            this.customData = customData;
            return this;
        }

        /**
         * Builds the Application object.
         *
         * @return A new Application containing the configured properties.
         */
        public User build() {
            return new User(this);
        }
    }

    /**
     * Represents the "properties" field of a User object.
     */
    public static class Properties {
        private Map<String, Object> customData;

        /**
         * Custom key/value pairs to associate with the user.
         *
         * @return The custom data as a Map, or {@code null} if not specified.
         */
        @JsonProperty("custom_data")
        public Map<String, ?> getCustomData() {
            return customData;
        }
    }
}
