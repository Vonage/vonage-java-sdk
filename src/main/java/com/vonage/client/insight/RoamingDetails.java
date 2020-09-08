/*
 * Copyright (c) 2020 Vonage
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
package com.vonage.client.insight;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class RoamingDetails {
    public enum RoamingStatus {
        UNKNOWN, ROAMING, NOT_ROAMING;

        private static final Map<String, RoamingStatus> ROAMING_STATUS_INDEX = new HashMap<>();

        static {
            for (RoamingStatus roamingStatus : RoamingStatus.values()) {
                ROAMING_STATUS_INDEX.put(roamingStatus.name(), roamingStatus);
            }
        }

        @JsonCreator
        public static RoamingStatus fromString(String name) {
            RoamingStatus foundRoamingStatus = ROAMING_STATUS_INDEX.get(name.toUpperCase());
            return (foundRoamingStatus != null) ? foundRoamingStatus : UNKNOWN;
        }
    }

    private RoamingStatus status;
    private String roamingCountryCode;
    private String roamingNetworkCode;
    private String roamingNetworkName;

    public RoamingStatus getStatus() {
        return status;
    }

    @JsonProperty("roaming_country_code")
    public String getRoamingCountryCode() {
        return roamingCountryCode;
    }

    @JsonProperty("roaming_network_code")
    public String getRoamingNetworkCode() {
        return roamingNetworkCode;
    }

    @JsonProperty("roaming_network_name")
    public String getRoamingNetworkName() {
        return roamingNetworkName;
    }
}
