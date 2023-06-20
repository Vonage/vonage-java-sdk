/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.verify;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageResponseParseException;
import java.io.IOException;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckResponse {
    private String requestId, eventId, currency, errorText;
    private final VerifyStatus status;
    private BigDecimal price, estimatedPriceMessagesSent;

    @JsonCreator
    public CheckResponse(@JsonProperty(value = "status", required = true) VerifyStatus status) {
        this.status = status;
    }

    /**
     * @return The {@code request_id} that you received in the response to the Verify request
     * and used in the Verify check request.
     */
    @JsonProperty("request_id")
    public String getRequestId() {
        return requestId;
    }

    /**
     * @return The ID of the verification event, such as an SMS or TTS call.
     */
    @JsonProperty("event_id")
    public String getEventId() {
        return eventId;
    }

    /**
     * @return A value of {@link VerifyStatus#OK} indicates that your user entered the correct code.
     * Otherwise, check {@link #getErrorText()}.
     */
    public VerifyStatus getStatus() {
        return status;
    }

    /**
     * @return The cost incurred for this request.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @return The currency code.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return If the status is non-zero, this explains the error encountered.
     */
    @JsonProperty("error_text")
    public String getErrorText() {
        return errorText;
    }

    /**
     * @return This field may not be present, depending on your pricing model.
     * The value indicates the cost (in EUR) of the calls made and messages sent for the verification process.
     * This value may be updated during and shortly after the request completes because user input events can
     * overlap with message/call events. When this field is present, the total cost of the verification is the
     * sum of this field and the {@code price} field.
     *
     * @since 7.1.0
     */
    @JsonProperty("estimated_price_messages_sent")
    public BigDecimal getEstimatedPriceMessagesSent() {
        return estimatedPriceMessagesSent;
    }

    public static CheckResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, CheckResponse.class);
        } catch (IOException jme) {
            throw new VonageResponseParseException("Failed to produce CheckResponse from json.", jme);
        }
    }
}
