/*
 *   Copyright 2022 Vonage
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

/**
 * Response object constructed from the JSON payload returned for Standard number insight requests.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardInsightResponse extends BasicInsightResponse {
    private BigDecimal requestPrice, remainingBalance, refundPrice;
    private CarrierDetails originalCarrier, currentCarrier;
    private PortedStatus ported;
    private CallerIdentity callerIdentity;
    private String callerName, firstName, lastName;
    private CallerType callerType;

    public static StandardInsightResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, StandardInsightResponse.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce StandardInsightResponse from json.", jpe);
        }
    }

    /**
     * @return The amount in EUR charged to your account.
     */
    @JsonProperty("request_price")
    public BigDecimal getRequestPrice() {
        return requestPrice;
    }

    /**
     * @return Your account balance in EUR after this request.
     */
    @JsonProperty("remaining_balance")
    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    /**
     * @return Information about the network the number was initially connected to.
     */
    @JsonProperty("original_carrier")
    public CarrierDetails getOriginalCarrier() {
        return originalCarrier;
    }

    /**
     * @return Information about the network the number is currently connected to.
     */
    @JsonProperty("current_carrier")
    public CarrierDetails getCurrentCarrier() {
        return currentCarrier;
    }

    /**
     * @return Whether the number has been ported, as an enum.
     */
    @JsonProperty("ported")
    public PortedStatus getPorted() {
        return ported;
    }

    /**
     * @return Information about the caller.
     */
    @JsonProperty("caller_identity")
    public CallerIdentity getCallerIdentity() {
        return callerIdentity;
    }

    /**
     * @return If there is an internal lookup error, the refund_price will reflect the lookup price.
     * If cnam is requested for a non-US number the refund_price will reflect the cnam price.
     * If both of these conditions occur, refund_price is the sum of the lookup price and cnam price.
     */
    @JsonProperty("refund_price")
    public BigDecimal getRefundPrice() {
        return refundPrice;
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
}
