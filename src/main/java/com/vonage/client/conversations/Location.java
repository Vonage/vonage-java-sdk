/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Describes parameters for a Location message in {@link MessageEvent#getLocation()}.
 */
public final class Location extends JsonableBaseObject {
    private Double longitude, latitude;
    private String name, address;

    Location() {}

    Location(Builder builder) {
        longitude = builder.longitude;
        latitude = builder.latitude;
        name = builder.name;
        address = builder.address;
    }

    /**
     * Longitude of the location.
     *
     * @return The longitude as a Double, or {@code null} if unspecified.
     */
    @JsonProperty("longitude")
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Latitude of the location.
     *
     * @return The latitude as a Double, or {@code null} if unspecified.
     */
    @JsonProperty("latitude")
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Name of the location.
     *
     * @return The name, or {@code null} if unspecified.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Full location address.
     *
     * @return The address as a string, or {@code null} if unspecified.
     */
    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for setting Location parameters.
     */
    public static final class Builder {
        private Double longitude, latitude;
        private String name, address;

        private Builder() {}

        /**
         * Longitude of the location.
         *
         * @param longitude The longitude as a double.
         *
         * @return This builder.
         */
        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        /**
         * Latitude of the location.
         *
         * @param latitude The latitude as a double.
         *
         * @return This builder.
         */
        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        /**
         * Name of the location.
         *
         * @param name The name.
         *
         * @return This builder.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Full address.
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
         * Builds the {@linkplain Location}.
         *
         * @return A new Location instance, populated with all fields from this builder.
         */
        public Location build() {
            return new Location(this);
        }
    }
}
