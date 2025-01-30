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
package com.vonage.client.insight;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vonage.client.JsonableBaseObject;

/**
 * Information about the roaming status for number. This is applicable to mobile numbers only.
 */
@JsonDeserialize(using = RoamingDeserializer.class)
public class RoamingDetails extends JsonableBaseObject {
    private final RoamingStatus status;
    private final String roamingCountryCode, roamingNetworkCode, roamingNetworkName;

    /**
     * Represents whether the number is outside its home carrier network, as an enum.
     */
    public enum RoamingStatus {
        UNKNOWN, ROAMING, NOT_ROAMING;

        @JsonCreator
        public static RoamingStatus fromString(String name) {
            try {
                return RoamingStatus.valueOf(name.toUpperCase());
            }
            catch (IllegalArgumentException ex) {
                return UNKNOWN;
            }
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @Deprecated
    public RoamingDetails(RoamingStatus status, String roamingCountryCode, String roamingNetworkCode, String roamingNetworkName) {
        this.status = status;
        this.roamingCountryCode = roamingCountryCode;
        this.roamingNetworkCode = roamingNetworkCode;
        this.roamingNetworkName = roamingNetworkName;
    }

    /**
     * @return The roaming status, as an enum.
     */
    @JsonProperty("status")
    public RoamingStatus getStatus() {
        return status;
    }

    /**
     * @return If number is roaming, this is the code of the country the number is roaming in.
     */
    @JsonProperty("roaming_country_code")
    public String getRoamingCountryCode() {
        return roamingCountryCode;
    }

    /**
     * @return If the number is roaming, this is the ID of the carrier network the number is roaming in.
     */
    @JsonProperty("roaming_network_code")
    public String getRoamingNetworkCode() {
        return roamingNetworkCode;
    }

    /**
     * @return If the number is roaming, this is the name of the carrier network the number is roaming in.
     */
    @JsonProperty("roaming_network_name")
    public String getRoamingNetworkName() {
        return roamingNetworkName;
    }
}
