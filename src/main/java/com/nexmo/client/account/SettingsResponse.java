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
package com.nexmo.client.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.VonageUnexpectedException;

import java.io.IOException;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettingsResponse {
    @JsonProperty("mo-callback-url")
    private String incomingSmsUrl;
    @JsonProperty("dr-callback-url")
    private String deliveryReceiptUrl;
    @JsonProperty("max-outbound-request")
    private Integer maxOutboundMessagesPerSecond;
    @JsonProperty("max-inbound-request")
    private Integer maxInboundMessagesPerSecond;
    @JsonProperty("max-calls-per-second")
    private Integer maxApiCallsPerSecond;

    /**
     * @return The URL where Vonage will send a webhook when an incoming SMS is received when a number-specific URL is
     * not configured.
     */
    public String getIncomingSmsUrl() {
        return incomingSmsUrl;
    }

    /**
     * @return The URL where Vonage will send a webhook when a delivery receipt is received when a number-specific URL is
     * not configured.
     */
    public String getDeliveryReceiptUrl() {
        return deliveryReceiptUrl;
    }

    /**
     * @return The maximum number of outbound messages per second.
     */
    public Integer getMaxOutboundMessagesPerSecond() {
        return maxOutboundMessagesPerSecond;
    }

    /**
     * @return The maximum number of inbound messages per second.
     */
    public Integer getMaxInboundMessagesPerSecond() {
        return maxInboundMessagesPerSecond;
    }

    /**
     * @return The maximum number of API calls per second.
     */
    public Integer getMaxApiCallsPerSecond() {
        return maxApiCallsPerSecond;
    }

    public static SettingsResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, SettingsResponse.class);
        } catch (IOException e) {
            throw new VonageUnexpectedException("Failed to produce SettingsResponse from json.", e);
        }
    }
}
