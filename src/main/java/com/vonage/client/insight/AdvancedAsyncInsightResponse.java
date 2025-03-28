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
import com.vonage.client.JsonableBaseObject;
import java.math.BigDecimal;

/**
 * Response object constructed from the JSON payload returned for Basic number insight requests.
 */
public class AdvancedAsyncInsightResponse extends JsonableBaseObject {
    private InsightStatus status;
    private String requestId, number, errorText;
    private BigDecimal requestPrice, remainingBalance;

    AdvancedAsyncInsightResponse() {}

    /**
     * @return The status code of the message, as an enum.
     */
    @JsonProperty("status")
    public InsightStatus getStatus() {
        return status;
    }

    /**
     * @return The unique identifier for your request.
     * This is an alphanumeric string of up to 40 characters.
     */
    @JsonProperty("request_id")
    public String getRequestId() {
        return requestId;
    }

    /**
     * @return The phone number the insight request was made for.
     */
    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    /**
     * @return The price charged for the request.
     */
    @JsonProperty("request_price")
    public BigDecimal getRequestPrice() {
        return requestPrice;
    }

    /**
     * @return The remaining balance in your account.
     */
    @JsonProperty("remaining_balance")
    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    /**
     * @return The error text if the request was unsuccessful.
     */
    @JsonProperty("error_text")
    public String getErrorText() {
        return errorText;
    }
}
