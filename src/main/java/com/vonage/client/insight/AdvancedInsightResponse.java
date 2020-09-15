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
import java.util.HashMap;
import java.util.Map;

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

    public enum PortedStatus {
        UNKNOWN, PORTED, NOT_PORTED, ASSUMED_NOT_PORTED, ASSUMED_PORTED;

        private static final Map<String, PortedStatus> PORTED_STATUS_INDEX = new HashMap<>();

        static {
            for (PortedStatus portedStatus : PortedStatus.values()) {
                PORTED_STATUS_INDEX.put(portedStatus.name(), portedStatus);
            }
        }

        @JsonCreator
        public static PortedStatus fromString(String name) {
            PortedStatus foundPortedStatus = PORTED_STATUS_INDEX.get(name.toUpperCase());
            return (foundPortedStatus != null) ? foundPortedStatus : UNKNOWN;
        }

    }

    public enum Validity {
        UNKNOWN, VALID, NOT_VALID;

        private static final Map<String, Validity> VALIDITY_INDEX = new HashMap<>();

        static {
            for (Validity validity : Validity.values()) {
                VALIDITY_INDEX.put(validity.name(), validity);
            }
        }

        @JsonCreator
        public static Validity fromString(String name) {
            Validity foundValidity = VALIDITY_INDEX.get(name.toUpperCase());
            return (foundValidity != null) ? foundValidity : Validity.UNKNOWN;
        }
    }

    public enum Reachability {
        UNKNOWN, REACHABLE, UNDELIVERABLE, ABSENT, BAD_NUMBER, BLACKLISTED;

        private static final Map<String, Reachability> REACHABILITY_INDEX = new HashMap<>();

        static {
            for (Reachability reachability : Reachability.values()) {
                REACHABILITY_INDEX.put(reachability.name(), reachability);
            }
        }

        @JsonCreator
        public static Reachability fromString(String name) {
            Reachability foundReachability = REACHABILITY_INDEX.get(name.toUpperCase());
            return (foundReachability != null) ? foundReachability : UNKNOWN;
        }
    }
}
