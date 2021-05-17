/*
 *   Copyright 2020 Vonage
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

import java.util.HashMap;
import java.util.Map;

@JsonDeserialize(using = RoamingDeseriazlizer.class)
public class RoamingDetails {
    public enum RoamingStatus {
        UNKNOWN, ROAMING, NOT_ROAMING;

        private static final Map<String, RoamingStatus> ROAMING_STATUS_INDEX = new HashMap<>();

        static {
            for (RoamingStatus roamingStatus : RoamingStatus.values()) {
                ROAMING_STATUS_INDEX.put(roamingStatus.name(), roamingStatus);
            }
        }

        @JsonCreator
        public static RoamingStatus fromString(String name) {
            RoamingStatus foundRoamingStatus = ROAMING_STATUS_INDEX.get(name.toUpperCase());
            return (foundRoamingStatus != null) ? foundRoamingStatus : UNKNOWN;
        }
    }

    public RoamingDetails(RoamingStatus status, String roamingCountryCode, String roamingNetworkCode, String roamingNetworkName){
        this.status = status;
        this.roamingCountryCode = roamingCountryCode;
        this.roamingNetworkCode = roamingNetworkCode;
        this.roamingNetworkName = roamingNetworkName;
    }

    private RoamingStatus status;
    private String roamingCountryCode;
    private String roamingNetworkCode;
    private String roamingNetworkName;

    public RoamingStatus getStatus() {
        return status;
    }

    @JsonProperty("roaming_country_code")
    public String getRoamingCountryCode() {
        return roamingCountryCode;
    }

    @JsonProperty("roaming_network_code")
    public String getRoamingNetworkCode() {
        return roamingNetworkCode;
    }

    @JsonProperty("roaming_network_name")
    public String getRoamingNetworkName() {
        return roamingNetworkName;
    }
}
