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
package com.vonage.client.account;

import com.vonage.client.AbstractQueryParamsRequest;
import java.util.Map;

/**
 * Request wrapper for updating account settings using {@linkplain AccountClient#updateSettings(SettingsRequest)}.
 */
public class SettingsRequest extends AbstractQueryParamsRequest {
    private final String incomingSmsUrl, deliveryReceiptUrl;

    /**
     * Constructor.
     *
     * @param incomingSmsUrl     The URL where Vonage will send a webhook when an incoming SMS is received when a
     *                           number-specific URL is not configured. Set to an empty string to unset the value.
     * @param deliveryReceiptUrl The URL where Vonage will send a webhook when an incoming SMS is received when a
     *                           number-specific URL is not configured. Set to an empty string to unset the value.
     */
    public SettingsRequest(String incomingSmsUrl, String deliveryReceiptUrl) {
        this.incomingSmsUrl = incomingSmsUrl;
        this.deliveryReceiptUrl = deliveryReceiptUrl;
    }

    /**
     * URL where Vonage will send a webhook when an incoming SMS is received when a
     * number-specific URL is not configured.
     *
     * @return The incoming SMS URL, or {@code null} if unspecified.
     */
    public String getIncomingSmsUrl() {
        return incomingSmsUrl;
    }

    /**
     * URL where Vonage will send a webhook when a delivery receipt is received when a
     * number-specific URL is not configured.
     *
     * @return The delivery receipt URL, or {@code null} if unspecified.
     */
    public String getDeliveryReceiptUrl() {
        return deliveryReceiptUrl;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        conditionalAdd("moCallBackUrl", incomingSmsUrl);
        conditionalAdd("drCallBackUrl", deliveryReceiptUrl);
        return params;
    }
}
