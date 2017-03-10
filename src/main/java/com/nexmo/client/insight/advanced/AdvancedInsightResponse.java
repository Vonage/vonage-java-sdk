/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.insight.advanced;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.insight.RoamingDetails;
import com.nexmo.client.insight.standard.StandardInsightResponse;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdvancedInsightResponse extends StandardInsightResponse {
    private Validity validNumber;
    private Reachability reachability;
    private PortedStatus ported;
    private Integer lookupOutcome;
    private String lookupOutcomeMessage;
    private RoamingDetails roaming;

    public static AdvancedInsightResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, AdvancedInsightResponse.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce BasicInsightResponse from json.", jpe);
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

    public enum PortedStatus {
        UNKNOWN,
        PORTED,
        NOT_PORTED,
        ASSUMED_NOT_PORTED,
        ASSUMED_PORTED;

        @JsonCreator
        public static PortedStatus fromString(String name) {
            return PortedStatus.valueOf(name.toUpperCase());
        }

    }

    public enum Validity {
        UNKNOWN,
        VALID,
        NOT_VALID;

        @JsonCreator
        public static Validity fromString(String name) {
            return Validity.valueOf(name.toUpperCase());
        }
    }

    public enum Reachability {
        UNKNOWN,
        REACHABLE,
        UNDELIVERABLE,
        ABSENT,
        BAD_NUMBER,
        BLACKLISTED;

        @JsonCreator
        public static Reachability fromString(String name) {
            return Reachability.valueOf(name.toUpperCase());
        }
    }

}
