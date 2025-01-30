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

import com.vonage.client.QueryParamsRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Request wrapper for updating account settings.
 *
 * @deprecated This will be made package-private in the next major release.
 */
@Deprecated
public class SettingsRequest implements QueryParamsRequest {
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
     * @param incomingSmsUrl The URL where Vonage will send a webhook when an incoming SMS is received when a
     *                       number-specific URL is not configured. Set to an empty string to unset the value.
     *
     * @return An SettingsRequest with only the incoming SMS URL set.
     * @deprecated This will be removed in the next major release.
     */
    @Deprecated
    public static SettingsRequest withIncomingSmsUrl(String incomingSmsUrl) {
        return new SettingsRequest(incomingSmsUrl, null);
    }

    /**
     * @param deliveryReceiptUrl The URL where Vonage will send a webhook when an incoming SMS is received when a
     *                           number-specific URL is not configured. Set to an empty string to unset the value.
     *
     * @return An SettingsRequest with only the delivery receipt URL set.
     * @deprecated This will be removed in the next major release.
     */
    @Deprecated
    public static SettingsRequest withDeliveryReceiptUrl(String deliveryReceiptUrl) {
        return new SettingsRequest(null, deliveryReceiptUrl);
    }

    /**
     * @return The URL where Vonage will send a webhook when an incoming SMS is received when a number-specific URL is
     * not configured.
     */
    @Deprecated
    public String getIncomingSmsUrl() {
        return incomingSmsUrl;
    }

    /**
     * @return The URL where Vonage will send a webhook when a delivery receipt is received when a number-specific URL is
     * not configured.
     */
    @Deprecated
    public String getDeliveryReceiptUrl() {
        return deliveryReceiptUrl;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = new LinkedHashMap<>(4);
        if (incomingSmsUrl != null) {
            params.put("moCallBackUrl", incomingSmsUrl);
        }
        if (deliveryReceiptUrl != null) {
            params.put("drCallBackUrl", deliveryReceiptUrl);
        }
        return params;
    }
}
