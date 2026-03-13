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
 * Response containing multiple insights for a phone number.
 * 
 * @since 9.1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentityInsightsResponse extends JsonableBaseObject {
    private String requestId;
    private Insights insights;
    
    protected IdentityInsightsResponse() {}
    
    /**
     * Gets the unique request identifier.
     * 
     * @return The request ID.
     */
    @JsonProperty("request_id")
    public String getRequestId() {
        return requestId;
    }
    
    /**
     * Gets the insights object containing all requested insights.
     * 
     * @return The insights.
     */
    @JsonProperty("insights")
    public Insights getInsights() {
        return insights;
    }
    
    /**
     * Container for all insight responses.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Insights extends JsonableBaseObject {
        private FormatInsightResponse format;
        private SimSwapInsightResponse simSwap;
        private OriginalCarrierInsightResponse originalCarrier;
        private CurrentCarrierInsightResponse currentCarrier;
        
        protected Insights() {}
        
        /**
         * Gets the format insight response.
         * 
         * @return The format insight, or {@code null} if not requested.
         */
        @JsonProperty("format")
        public FormatInsightResponse getFormat() {
            return format;
        }
        
        /**
         * Gets the SIM swap insight response.
         * 
         * @return The SIM swap insight, or {@code null} if not requested.
         */
        @JsonProperty("sim_swap")
        public SimSwapInsightResponse getSimSwap() {
            return simSwap;
        }
        
        /**
         * Gets the original carrier insight response.
         * 
         * @return The original carrier insight, or {@code null} if not requested.
         */
        @JsonProperty("original_carrier")
        public OriginalCarrierInsightResponse getOriginalCarrier() {
            return originalCarrier;
        }
        
        /**
         * Gets the current carrier insight response.
         * 
         * @return The current carrier insight, or {@code null} if not requested.
         */
        @JsonProperty("current_carrier")
        public CurrentCarrierInsightResponse getCurrentCarrier() {
            return currentCarrier;
        }
    }
}
