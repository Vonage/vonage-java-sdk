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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.E164;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Request to retrieve multiple insights for a phone number.
 * 
 * @since 9.1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentityInsightsRequest extends JsonableBaseObject {
    private String phoneNumber, purpose;
    private Map<String, Object> insights;
    
    /**
     * Creates a new identity insights request.
     * 
     * @param phoneNumber Phone number in E.164 format.
     */
    public IdentityInsightsRequest(String phoneNumber) {
        this.phoneNumber = new E164(Objects.requireNonNull(phoneNumber, "Phone number is required")).toString();
        this.insights = new HashMap<String, Object>();
    }
    
    /**
     * Gets the phone number.
     * 
     * @return The phone number in E.164 format.
     */
    @JsonProperty("phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Gets the purpose for the request.
     * 
     * @return The purpose.
     */
    @JsonProperty("purpose")
    public String getPurpose() {
        return purpose;
    }
    
    /**
     * Sets the purpose for the request.
     * Required for Network Registry insights.
     * 
     * @param purpose The purpose (e.g., "FraudPreventionAndDetection").
     * @return This request for method chaining.
     */
    public IdentityInsightsRequest purpose(String purpose) {
        this.purpose = purpose;
        return this;
    }
    
    /**
     * Gets the requested insights.
     * 
     * @return Map of insight names to their request objects.
     */
    @JsonProperty("insights")
    public Map<String, Object> getInsights() {
        return insights;
    }
    
    /**
     * Requests the format insight.
     * 
     * @return This request for method chaining.
     */
    public IdentityInsightsRequest format() {
        insights.put("format", new HashMap<String, Object>());
        return this;
    }
    
    /**
     * Requests the SIM swap insight with default period (240 hours).
     * 
     * @return This request for method chaining.
     */
    public IdentityInsightsRequest simSwap() {
        return simSwap(null);
    }
    
    /**
     * Requests the SIM swap insight with specified period.
     * 
     * @param period Period in hours (1-2400), or {@code null} for default (240).
     * @return This request for method chaining.
     */
    public IdentityInsightsRequest simSwap(Integer period) {
        insights.put("sim_swap", period != null ? new SimSwapInsightRequest(period) : new HashMap<String, Object>());
        return this;
    }
    
    /**
     * Requests the original carrier insight.
     * 
     * @return This request for method chaining.
     */
    public IdentityInsightsRequest originalCarrier() {
        insights.put("original_carrier", new HashMap<String, Object>());
        return this;
    }
    
    /**
     * Requests the current carrier insight.
     * 
     * @return This request for method chaining.
     */
    public IdentityInsightsRequest currentCarrier() {
        insights.put("current_carrier", new HashMap<String, Object>());
        return this;
    }
}
