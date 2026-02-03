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
import java.time.Instant;

/**
 * Response containing SIM swap information for a phone number.
 * 
 * @since 9.1.0
 */
public class SimSwapInsightResponse extends JsonableBaseObject {
    private Instant latestSimSwapAt;
    private Boolean isSwapped;
    private InsightStatus status;
    
    protected SimSwapInsightResponse() {}
    
    /**
     * Gets the date and time of the latest SIM swap.
     * 
     * @return The latest SIM swap timestamp, or {@code null} if unavailable.
     */
    @JsonProperty("latest_sim_swap_at")
    public Instant getLatestSimSwapAt() {
        return latestSimSwapAt;
    }
    
    /**
     * Indicates whether the SIM card was swapped during the specified period.
     * 
     * @return {@code true} if swapped, {@code false} otherwise, or {@code null} if unknown.
     */
    @JsonProperty("is_swapped")
    public Boolean getIsSwapped() {
        return isSwapped;
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
