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
package com.vonage.client.insight;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarrierDetails {
    private String networkCode, name, country;
    private NetworkType networkType;

    /**
     * @return The mobile country code for the carrier the number is associated with.
     * Unreal numbers are marked as null and the request is rejected altogether if
     * the number is impossible according to the E.164 guidelines.
     */
    @JsonProperty("network_code")
    public String getNetworkCode() {
        return networkCode;
    }

    /**
     * @return The full name of the carrier that the number is associated with.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * @return The country that the number is associated with. This is in ISO 3166-1 alpha-2 format.
     */
    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    /**
     * @return The network type, as an enum.
     */
    @JsonProperty("network_type")
    public NetworkType getNetworkType() {
        return networkType;
    }

    /**
     * Enum representing the type of network that the number is associated with.
     * Note that this enum may be {@code null}.
     */
    public enum NetworkType {
        MOBILE,
        LANDLINE,
        LANDLINE_PREMIUM,
        LANDLINE_TOLLFREE,
        VIRTUAL,
        UNKNOWN,
        PAGER;

        @JsonCreator
        public static NetworkType fromString(String name) {
            if (name.equalsIgnoreCase("null")) {
                return null;
            }
            try {
                return NetworkType.valueOf(name.toUpperCase());
            }
            catch (IllegalArgumentException iax) {
                return UNKNOWN;
            }
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
