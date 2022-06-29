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
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;

/**
 * Response object constructed from the JSON payload returned for Advanced number insight requests.
 */
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
     * @return Whether the number has been ported, as an enum.
     */
    @JsonProperty("ported")
    public PortedStatus getPorted() {
        return ported;
    }

    /**
     * @return Shows if all information about a phone number has been returned.
     * <code>0</code> is success, <code>1</code> is a partial success (some fields populated),
     * <code>2</code> is failure.
     *
     */
    @JsonProperty("lookup_outcome")
    public Integer getLookupOutcome() {
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
     * @return Full name of the person or business who owns the phone number, or "unknown" if this
     * information is not available. This parameter is only present if cnam had a value of
     * <code>true</code> in the request.
     */
    @JsonProperty("caller_name")
    public String getCallerName() {
        return callerName;
    }

    /**
     * @return First name of the person who owns the phone number if the owner is an individual.
     * This parameter is only present if cnam had a value of <code>true</code> in the request.
     */
    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return Last name of the person who owns the phone number if the owner is an individual.
     * This parameter is only present if cnam had a value of <code>true</code> in the request.
     */
    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    /**
     * @return The caller type, as an enum.
     */
    @JsonProperty("caller_type")
    public CallerType getCallerType() {
        return callerType;
    }

    /**
     * @return Real-time data about the number if it was requested, <code>null</code> otherwise.
     */
    @JsonProperty("real_time_data")
    public RealTimeData getRealTimeData() {
        return realTimeData;
    }

    /**
     * Enum representing whether the user has changed carrier for the number.
     * The assumed status means that the information supplier has replied to the request but has not
     * explicitly reported that the number is ported. Note that this enum may be <code>null</code>.
     */
    public enum PortedStatus {
        UNKNOWN, PORTED, NOT_PORTED, ASSUMED_NOT_PORTED, ASSUMED_PORTED;

        @JsonCreator
        public static PortedStatus fromString(String name) {
            if (name.equalsIgnoreCase("null")) {
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

    /**
     * Enum representing the existence of a number.
     * <code>UNKNOWN</code> means the number could not be validated. valid means the number is valid.
     * <code>NOT_VALID</code> means the number is not valid.
     * <code>INFERRED_NOT_VALID</code> means that the number could not be determined as valid or invalid
     * via an external system and the best guess is that the number is invalid.
     * This is applicable to mobile numbers only.
     */
    public enum Validity {
        UNKNOWN, VALID, NOT_VALID, INFERRED, INFERRED_NOT_VALID;

        @JsonCreator
        public static Validity fromString(String name) {
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

    /**
     * Enum representing whether you can call number now. This is applicable to mobile numbers only.
     * Note that this enum may be <code>null</code>.
     */
    public enum Reachability {
        UNKNOWN, REACHABLE, UNDELIVERABLE, ABSENT, BAD_NUMBER, BLACKLISTED;

        @JsonCreator
        public static Reachability fromString(String name) {
            if (name.equalsIgnoreCase("null")) {
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

    /**
     * Real time data about the number.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RealTimeData {

        static class ActiveStatusDeserializer extends JsonDeserializer<Boolean> {
            @Override
            public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String text = p.getText();
                if (text == null) return null;
                switch (text.toLowerCase()) {
                    default: return null;
                    case "true": case "active":
                        return true;
                    case "false": case "inactive":
                        return false;
                }
            }
        }

        @JsonDeserialize(using = ActiveStatusDeserializer.class)
        protected Boolean activeStatus;
        protected String handsetStatus;

        /**
         * @return Whether the end-user's phone number is active within an operator's network.
         * Note that this could be <code>null</code>.
         */
        @JsonProperty("active_status")
        public Boolean getActiveStatus() {
            return activeStatus;
        }

        /**
         * @return Whether the end-user's handset is turned on or off.
         */
        @JsonProperty("handset_status")
        public String getHandsetStatus() {
            return handsetStatus;
        }
    }
}
