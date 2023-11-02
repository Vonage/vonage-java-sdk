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
package com.vonage.client.insight;

import java.util.Map;

public class AdvancedInsightRequest extends BaseInsightRequest {
    private final boolean async;
    private final String callback;
    private final Boolean realTimeData;

    private AdvancedInsightRequest(Builder builder) {
        super(builder.number, builder.country);
        cnam = builder.cnam;
        callback = builder.callback;
        if (!(async = builder.async)) {
            realTimeData = builder.realTimeData;
        }
        else {
            realTimeData = null;
            if (callback == null || callback.isEmpty()) {
                throw new IllegalStateException("You must define a callback URL when using asynchronous insights.");
            }
        }
    }

    /**
     * This method is the starting point for constructing an Advanced Insight 
     *
     * @param number A single phone number that you need insight about in national or international format.
     *
     * @return A new {@link Builder} instance.
     */
    public static Builder builder(String number) {
        return new Builder(number);
    }

    /**
     * This method is the starting point for constructing an Advanced Insight 
     * Note that the number field must be set.
     *
     * @return A new {@link Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    public Boolean getRealTimeData() {
        return realTimeData;
    }

    public Boolean getCnam() {
        return cnam;
    }

    public boolean isAsync() {
        return async;
    }

    public String getCallback() {
        return callback;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        if (callback != null) {
            params.put("callback", callback);
        }
        if (realTimeData != null) {
            params.put("real_time_data", realTimeData.toString());
        }
        return params;
    }

    /**
     * Construct an AdvancedInsightRequest with a number.
     *
     * @param number A single phone number that you need insight about in national or international format.
     *
     * @return A new AdvancedInsightRequest object.
     */
    public static AdvancedInsightRequest withNumber(String number) {
        return new Builder(number).build();
    }

    /**
     * Construct a AdvancedInsightRequest with a number and country.
     *
     * @param number  A single phone number that you need insight about in national or international format.
     * @param country If a number does not have a country code, or it is uncertain, set the two-character country code.
     *
     * @return A new AdvancedInsightRequest object.
     */
    public static AdvancedInsightRequest withNumberAndCountry(String number, String country) {
        return new Builder(number).country(country).build();
    }

    public static class Builder {
        protected boolean async;
        protected Boolean cnam, realTimeData;
        protected String number, country, callback;

        protected Builder(String number) {
            this.number = number;
        }

        protected Builder() {}

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
         * @param async True if the call should be done asynchronously. When setting this value to true, the {@link
         *              Builder#callback(String)} parameter must also be set.
         *
         * @return This builder.
         */
        public Builder async(boolean async) {
            this.async = async;
            return this;
        }

        /**
         * @param url The URL that Vonage will send a request to when the insight lookup is finished.
         *            This only takes effect when {@link #async(boolean)} is {@code true}.
         *
         * @return This builder.
         */
        public Builder callback(String url) {
            this.callback = url;
            return this;
        }

        /**
         * @param realTimeData A boolean to choose whether to get real time data back in the response.
         *                     This only applies when {@link #async(boolean)} is {@code false}.
         *
         * @return This builder.
         */
        public Builder realTimeData(boolean realTimeData) {
            this.realTimeData = realTimeData;
            return this;
        }

        /**
         * @return A new {@link AdvancedInsightRequest} object from the stored builder options.
         */
        public AdvancedInsightRequest build() {
            return new AdvancedInsightRequest(this);
        }
    }
}
