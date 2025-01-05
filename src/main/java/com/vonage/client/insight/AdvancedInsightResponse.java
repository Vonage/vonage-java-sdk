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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;

/**
 * Response object constructed from the JSON payload returned for Advanced number insight requests.
 */
public class AdvancedInsightResponse extends StandardInsightResponse {
    private Validity validNumber;
    private Reachability reachability;
    private LookupOutcome lookupOutcome;
    private String lookupOutcomeMessage;
    private RoamingDetails roaming;
    private RealTimeData realTimeData;
    private String errorText;

    public static AdvancedInsightResponse fromJson(String json) {
        return Jsonable.fromJson(json);
    }

    /**
     * @return Whether the number exists, as an enum.
     */
    @JsonProperty("valid_number")
    public Validity getValidNumber() {
        return validNumber;
    }

    /**
     * @return Whether the number can be called, as an enum.
     */
    @JsonProperty("reachable")
    public Reachability getReachability() {
        return reachability;
    }

    /**
     * @return The outcome, as an enum.
     */
    @JsonProperty("lookup_outcome")
    public LookupOutcome getLookupOutcome() {
        return lookupOutcome;
    }

    /**
     * @return Shows if all information about a phone number has been returned, as a String.
     */
    @JsonProperty("lookup_outcome_message")
    public String getLookupOutcomeMessage() {
        return lookupOutcomeMessage;
    }

    /**
     * @return The roaming information, as an enum.
     */
    @JsonProperty("roaming")
    public RoamingDetails getRoaming() {
        return roaming;
    }

    /**
     * @return Real-time data about the number if it was requested, {@code null} otherwise.
     */
    @JsonProperty("real_time_data")
    public RealTimeData getRealTimeData() {
        return realTimeData;
    }

    /**
     * @return The status description of your request.
     * This field is equivalent to status_message field in the other endpoints.
     */
    @JsonProperty("error_text")
    public String getErrorText() {
        return errorText;
    }
}
