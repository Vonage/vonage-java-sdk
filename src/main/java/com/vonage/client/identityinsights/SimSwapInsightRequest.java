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

/**
 * Request for SIM swap insight.
 * 
 * @since 9.1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimSwapInsightRequest extends JsonableBaseObject {
    private Integer period;
    
    /**
     * Creates a new SIM swap insight request with default period (240 hours).
     */
    public SimSwapInsightRequest() {}
    
    /**
     * Creates a new SIM swap insight request with specified period.
     * 
     * @param period Period in hours to check for SIM swap (1-2400).
     */
    public SimSwapInsightRequest(Integer period) {
        if (period != null && (period < 1 || period > 2400)) {
            throw new IllegalArgumentException("Period must be between 1 and 2400 hours.");
        }
        this.period = period;
    }
    
    /**
     * Gets the period to check for SIM swap.
     * 
     * @return The period in hours, or {@code null} for default (240).
     */
    @JsonProperty("period")
    public Integer getPeriod() {
        return period;
    }
    
    /**
     * Sets the period to check for SIM swap.
     * 
     * @param period Period in hours (1-2400).
     * @return This request for method chaining.
     */
    public SimSwapInsightRequest period(Integer period) {
        if (period != null && (period < 1 || period > 2400)) {
            throw new IllegalArgumentException("Period must be between 1 and 2400 hours.");
        }
        this.period = period;
        return this;
    }
}
