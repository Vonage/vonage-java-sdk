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
package com.vonage.client.insight;

public class AdvancedInsightRequest extends BaseInsightRequest {
    private boolean async;
    private String callback;

    private AdvancedInsightRequest(Builder builder) {
        number = builder.number;
        country = builder.country;
        cnam = builder.cnam;
        ipAddress = builder.ipAddress;
        async = builder.async;
        callback = builder.callback;
    }

    public static Builder builder(String number) {
        return new Builder(number);
    }

    public Boolean getCnam() {
        return cnam;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public boolean isAsync() {
        return async;
    }

    public String getCallback() {
        return callback;
    }

    /**
     * Construct a AdvancedInsightRequest with a number.
     *
     * @param number A single phone number that you need insight about in national or international format.
     *
     * @return A new {@link AdvancedInsightRequest} object.
     */
    public static AdvancedInsightRequest withNumber(String number) {
        return new Builder(number).build();
    }

    /**
     * Construct a AdvancedInsightRequest with a number and country.
     *
     * @param number  A single phone number that you need insight about in national or international format.
     * @param country If a number does not have a country code or it is uncertain, set the two-character country code.
     *
     * @return A new {@link AdvancedInsightRequest} object.
     */
    public static AdvancedInsightRequest withNumberAndCountry(String number, String country) {
        return new Builder(number).country(country).build();
    }

    public static class Builder {
        protected String number;
        protected String country;
        protected Boolean cnam;
        protected String ipAddress;
        protected boolean async;
        protected String callback;

        /**
         * @param number A single phone number that you need insight about in national or international format.
         */
        public Builder(String number) {
            this.number = number;
        }

        /**
         * @param number A single phone number that you need insight about in national or international format.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder number(String number) {
            this.number = number;
            return this;
        }

        /**
         * @param country If a number does not have a country code or it is uncertain, set the two-character country
         *                code.
         *
         * @return The {@link Builder} to keep building.
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
         * @return The {@link Builder} to keep building.
         */
        public Builder cnam(Boolean cnam) {
            this.cnam = cnam;
            return this;
        }

        /**
         * @param ipAddress The IP address of the user. If supplied, we will compare this to the country the user's
         *                  phone is located in and return an error if it does not match.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        /**
         * @param async True if the call should be done asynchronously. When setting this value to true, the {@link
         *              Builder#callback(String)} parameter must also be set.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder async(boolean async) {
            this.async = async;
            return this;
        }

        /**
         * @param url The URL that Vonage will send a request to when the insight lookup is finished.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder callback(String url) {
            this.callback = url;
            return this;
        }

        /**
         * @return A new {@link AdvancedInsightRequest} object from the stored builder options.
         */
        public AdvancedInsightRequest build() {
            if (async && (callback == null || callback.isEmpty())) {
                throw new IllegalStateException("You must define a callback url when using asyncronous insights.");
            }
            return new AdvancedInsightRequest(this);
        }
    }
}
