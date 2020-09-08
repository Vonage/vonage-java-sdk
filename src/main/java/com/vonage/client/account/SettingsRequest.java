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
package com.vonage.client.account;

public class SettingsRequest {
    private String incomingSmsUrl;
    private String deliveryReceiptUrl;

    /**
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
     */
    public static SettingsRequest withIncomingSmsUrl(String incomingSmsUrl) {
        return new SettingsRequest(incomingSmsUrl, null);
    }

    /**
     * @param deliveryReceiptUrl The URL where Vonage will send a webhook when an incoming SMS is received when a
     *                           number-specific URL is not configured. Set to an empty string to unset the value.
     *
     * @return An SettingsRequest with only the delivery receipt URL set.
     */
    public static SettingsRequest withDeliveryReceiptUrl(String deliveryReceiptUrl) {
        return new SettingsRequest(null, deliveryReceiptUrl);
    }

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
}
