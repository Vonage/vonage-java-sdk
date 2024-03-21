package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Describes parameters for a Location message in {@link MessageEvent#getLocation()}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Location extends JsonableBaseObject {
    private String longitude, latitude, name, address;

    Location() {}

    Location(Builder builder) {
        longitude = builder.longitude;
        latitude = builder.latitude;
        name = builder.name;
        address = builder.address;
    }

    /**
     *
     *
     * @return
     */
    @JsonProperty("longitude")
    public String getLongitude() {
        return longitude;
    }

    /**
     *
     *
     * @return
     */
    @JsonProperty("latitude")
    public String getLatitude() {
        return latitude;
    }

    /**
     *
     *
     * @return
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     *
     * @return
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

    public static final class Builder {
        private String longitude, latitude, name, address;

        private Builder() {}

        /**
         *
         *
         * @param longitude
         *
         * @return This builder.
         */
        public Builder longitude(String longitude) {
            this.longitude = longitude;
            return this;
        }

        /**
         *
         *
         * @param latitude
         *
         * @return This builder.
         */
        public Builder latitude(String latitude) {
            this.latitude = latitude;
            return this;
        }

        /**
         *
         *
         * @param name
         *
         * @return This builder.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         *
         *
         * @param address
         *
         * @return This builder.
         */
        public Builder address(String address) {
            this.address = address;
            return this;
        }

        /**
         *
         *
         * @return
         */
        public Location build() {
            return new Location(this);
        }
    }
}
