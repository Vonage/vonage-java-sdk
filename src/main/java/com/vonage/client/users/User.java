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
package com.vonage.client.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.users.channels.Channel;
import com.vonage.client.users.channels.Channels;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Represents a Vonage User (both request and response).
 */
public class User extends BaseUser {
    @JsonProperty("display_name") private String displayName;
    @JsonProperty("image_url") private URI imageUrl;
    @JsonProperty("channels") private Channels channels;
    @JsonProperty("properties") private Properties properties;
    @JsonProperty("custom_data") private Map<String, ?> customData;

    protected User() {
    }

    protected User(Builder builder) {
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
     * A string to be displayed as username. It does not need to be unique.
     *
     * @return The user's friendly name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * An image URL to associate with the user.
     *
     * @return The image URL, or {@code null} if not specified.
     */
    public URI getImageUrl() {
        return imageUrl;
    }

    /**
     * Custom key/value pairs to associate with the user.
     *
     * @return The custom data as a Map, or {@code null} if not specified.
     */
    @JsonIgnore
    public Map<String, ?> getCustomData() {
        return properties != null ? properties.customData : customData;
    }

    /**
     * The communication channels available to the user.
     *
     * @return The channels object, or {@code null} if unknown.
     */
    public Channels getChannels() {
        return channels;
    }

    /**
     * Constructs a user from the JSON payload.
     *
     * @param json The JSON structure containing the fields of this class.
     *
     * @return A new User instance.
     */
    public static User fromJson(String json) {
        return Jsonable.fromJson(json);
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
        private Map<String, ?> customData;
        private Channels channels;

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
         * Arbitrary freeform data to associate with the user. Note that this is
         * not additive: the value set here will replace any existing custom data when updating a user.
         *
         * @param customData Custom key/value pairs map.
         *
         * @return This builder.
         */
        public Builder customData(Map<String, ?> customData) {
            this.customData = customData;
            return this;
        }

        /**
         * Sets the communication channels for this user.
         *
         * @param channels The channels to associate with the user.
         *
         * @return This builder.
         */
        public Builder channels(Channel... channels) {
            return channels(Arrays.asList(channels));
        }

        /**
         * Sets the communication channels for this user.
         *
         * @param channels The collection of channels to associate with the user.
         *
         * @return This builder.
         */
        public Builder channels(Collection<? extends Channel> channels) {
            this.channels = new Channels(channels);
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
    static class Properties extends JsonableBaseObject {
        @JsonProperty("custom_data") Map<String, ?> customData;
    }
}
