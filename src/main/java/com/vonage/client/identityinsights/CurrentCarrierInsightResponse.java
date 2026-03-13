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
package com.vonage.client.identityinsights;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Response containing information about the current carrier assigned to a phone number.
 * 
 * @since 9.1.0
 */
public class CurrentCarrierInsightResponse extends JsonableBaseObject {
    private String name, countryCode, networkCode;
    private NetworkType networkType;
    private InsightStatus status;
    
    protected CurrentCarrierInsightResponse() {}
    
    /**
     * Gets the full name of the current carrier.
     * 
     * @return The carrier name, or {@code null} if unavailable.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }
    
    /**
     * Gets the type of network.
     * 
     * @return The network type, or {@code null} if unavailable.
     */
    @JsonProperty("network_type")
    public NetworkType getNetworkType() {
        return networkType;
    }
    
    /**
     * Gets the country code (ISO 3166-1 alpha-2).
     * 
     * @return The country code, or {@code null} if unavailable.
     */
    @JsonProperty("country_code")
    public String getCountryCode() {
        return countryCode;
    }
    
    /**
     * Gets the network code (MCC + MNC).
     * 
     * @return The network code, or {@code null} if unavailable.
     */
    @JsonProperty("network_code")
    public String getNetworkCode() {
        return networkCode;
    }
    
    /**
     * Gets the status of this insight.
     * 
     * @return The insight status.
     */
    @JsonProperty("status")
    public InsightStatus getStatus() {
        return status;
    }
}
