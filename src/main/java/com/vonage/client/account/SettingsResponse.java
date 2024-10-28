/*
 *   Copyright 2024 Vonage
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;

/**
 * Details of the configured account settings.
 */
public class SettingsResponse extends JsonableBaseObject {
    private String incomingSmsUrl, deliveryReceiptUrl;
    private Integer maxOutboundMessagesPerSecond, maxInboundMessagesPerSecond, maxApiCallsPerSecond;

    @Deprecated
    public SettingsResponse() {}

    /**
     * URL where Vonage will send a webhook when an incoming SMS is received when a
     * number-specific URL is not configured.
     *
     * @return The default URL for inbound SMS webhooks.
     */
    @JsonProperty("mo-callback-url")
    public String getIncomingSmsUrl() {
        return incomingSmsUrl;
    }

    /**
     * URL where Vonage will send a webhook when a delivery receipt is received when a
     * number-specific URL is not configured.
     *
     * @return The default URL for delivery receipt webhooks.
     */
    @JsonProperty("dr-callback-url")
    public String getDeliveryReceiptUrl() {
        return deliveryReceiptUrl;
    }

    /**
     * Maximum number of outbound messages per second for the account.
     *
     * @return The maximum number of messages that can be sent per second.
     */
    @JsonProperty("max-outbound-request")
    public Integer getMaxOutboundMessagesPerSecond() {
        return maxOutboundMessagesPerSecond;
    }

    /**
     * Maximum number of inbound messages per second for the account.
     *
     * @return The maximum number of messages that can be received per second.
     */
    @JsonProperty("max-inbound-request")
    public Integer getMaxInboundMessagesPerSecond() {
        return maxInboundMessagesPerSecond;
    }

    /**
     * Maximum number of API calls per second for the account.
     *
     * @return The maximum number of API calls that can be made per second.
     */
    @JsonProperty("max-calls-per-second")
    public Integer getMaxApiCallsPerSecond() {
        return maxApiCallsPerSecond;
    }

    @Deprecated
    public static SettingsResponse fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
