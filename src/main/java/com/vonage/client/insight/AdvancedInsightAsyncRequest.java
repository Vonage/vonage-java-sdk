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

import java.net.URI;
import java.util.Map;

/**
 * Represents an asynchronous advanced insight request. The main response body will be returned to the callback URL.
 *
 * @since 9.0.0
 */
public final class AdvancedInsightAsyncRequest extends BaseInsightRequest {
    private final URI callback;

    AdvancedInsightAsyncRequest(Builder builder) {
        super(builder.number, builder.country);
        cnam = builder.cnam;
        if (builder.callback == null || builder.callback.trim().isEmpty()) {
            throw new IllegalArgumentException("Callback URL is required.");
        }
        callback = URI.create(builder.callback);
    }

    /**
     * This method is the starting point for constructing an Advanced Insight 
     * Note that the number and callback fields must be set.
     *
     * @return A new {@link Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    public URI getCallback() {
        return callback;
    }

    public Boolean getCnam() {
        return cnam;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        params.put("callback", callback.toString());
        return params;
    }

    public static final class Builder {
        private Boolean cnam;
        private String number, country, callback;

        private Builder() {}

        /**
         * @param number A single phone number that you need insight about in national or international format.
         *              This field is REQUIRED.
         * @return This builder.
         */
        public Builder number(String number) {
            this.number = number;
            return this;
        }

        /**
         * @param country If a number does not have a country code, or it is uncertain, set the two-character country
         *                code.
         *
         * @return This builder.
         */
        public Builder country(String country) {
            this.country = country;
            return this;
        }

        /**
         * @param cnam Indicates if the name of the person who owns the phone number should also be looked up and
         *             returned. Set to true to receive phone number owner name in the response. This is only available
         *             for US numbers and incurs an additional charge.
         *
         * @return This builder.
         */
        public Builder cnam(boolean cnam) {
            this.cnam = cnam;
            return this;
        }

        /**
         * @param url The URL that Vonage will send a request to when the insight lookup is finished.
         *
         * @return This builder.
         */
        public Builder callback(String url) {
            this.callback = url;
            return this;
        }

        /**
         * @return A new {@link AdvancedInsightAsyncRequest} object from the stored builder options.
         */
        public AdvancedInsightAsyncRequest build() {
            return new AdvancedInsightAsyncRequest(this);
        }
    }
}
