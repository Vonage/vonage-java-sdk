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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdvancedInsightResponse extends StandardInsightResponse {
    private Validity validNumber;
    private Reachability reachability;
    private PortedStatus ported;
    private Integer lookupOutcome;
    private String lookupOutcomeMessage;
    private RoamingDetails roaming;
    private String callerName;
    private String firstName;
    private String lastName;
    private CallerType callerType;
    private RealTimeData realTimeData;

    public static AdvancedInsightResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, AdvancedInsightResponse.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce AdvancedInsightResponse from json.", jpe);
        }
    }

    @JsonProperty("valid_number")
    public Validity getValidNumber() {
        return validNumber;
    }

    @JsonProperty("reachable")
    public Reachability getReachability() {
        return reachability;
    }

    @JsonProperty("ported")
    public PortedStatus getPorted() {
        return ported;
    }

    @JsonProperty("lookup_outcome")
    public Integer getLookupOutcome() {
        return lookupOutcome;
    }

    @JsonProperty("lookup_outcome_message")
    public String getLookupOutcomeMessage() {
        return lookupOutcomeMessage;
    }

    @JsonProperty("roaming")
    public RoamingDetails getRoaming() {
        return roaming;
    }

    @JsonProperty("caller_name")
    public String getCallerName() {
        return callerName;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("caller_type")
    public CallerType getCallerType() {
        return callerType;
    }

    @JsonProperty("real_time_data")
    public RealTimeData getRealTimeData() {
        return realTimeData;
    }

    public enum PortedStatus {
        UNKNOWN, PORTED, NOT_PORTED, ASSUMED_NOT_PORTED, ASSUMED_PORTED;

        @JsonCreator
        public static PortedStatus fromString(String name) {
            if (name == null || name.equalsIgnoreCase("null")) {
                return null;
            }
            try {
                return PortedStatus.valueOf(name.toUpperCase());
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

    public enum Validity {
        UNKNOWN, VALID, NOT_VALID;

        @JsonCreator
        public static Validity fromString(String name) {
            if (name == null || name.equalsIgnoreCase("null")) {
                return null;
            }
            try {
                return Validity.valueOf(name.toUpperCase());
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

    public enum Reachability {
        UNKNOWN, REACHABLE, UNDELIVERABLE, ABSENT, BAD_NUMBER, BLACKLISTED;

        @JsonCreator
        public static Reachability fromString(String name) {
            if (name == null || name.equalsIgnoreCase("null")) {
                return null;
            }
            try {
                return Reachability.valueOf(name.toUpperCase());
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RealTimeData {
        protected Boolean activeStatus;
        protected String handsetStatus;

        @JsonProperty("active_status")
        public Boolean getActiveStatus() {
            return activeStatus;
        }

        @JsonProperty("handset_status")
        public String getHandsetStatus() {
            return handsetStatus;
        }
    }
}
