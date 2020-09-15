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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardInsightResponse extends BasicInsightResponse {
    private BigDecimal requestPrice;
    private BigDecimal remainingBalance;
    private BigDecimal refundPrice;
    private CarrierDetails originalCarrier;
    private CarrierDetails currentCarrier;
    private CallerIdentity callerIdentity;

    public static StandardInsightResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, StandardInsightResponse.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce StandardInsightResponse from json.", jpe);
        }
    }

    @JsonProperty("request_price")
    public BigDecimal getRequestPrice() {
        return requestPrice;
    }

    @JsonProperty("remaining_balance")
    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    @JsonProperty("original_carrier")
    public CarrierDetails getOriginalCarrier() {
        return originalCarrier;
    }

    @JsonProperty("current_carrier")
    public CarrierDetails getCurrentCarrier() {
        return currentCarrier;
    }

    @JsonProperty("caller_identity")
    public CallerIdentity getCallerIdentity() {
        return callerIdentity;
    }

    @JsonProperty("refund_price")
    public BigDecimal getRefundPrice() {
        return refundPrice;
    }
}
