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
package com.vonage.client.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

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
