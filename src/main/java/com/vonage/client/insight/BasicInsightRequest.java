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

public class BasicInsightRequest extends BaseInsightRequest {

    BasicInsightRequest(Builder builder) {
        super(builder.number, builder.country);
    }

    /**
     * Construct a BasicInsightRequest with a number.
     *
     * @param number A single phone number that you need insight about in national or international format.
     *
     * @return A new BasicInsightRequest object.
     */
    public static BasicInsightRequest withNumber(String number) {
        return new Builder(number).build();
    }

    /**
     * Construct a BasicInsightRequest with a number and country.
     *
     * @param number  A single phone number that you need insight about in national or international format.
     * @param country If a number does not have a country code, or it is uncertain, set the two-character country code.
     *
     * @return A new BasicInsightRequest object.
     */
    public static BasicInsightRequest withNumberAndCountry(String number, String country) {
        return new Builder(number).country(country).build();
    }

    /**
     * This method is the starting point for constructing a Basic Insight request.
     *
     * @param number A single phone number that you need insight about in national or international format.
     *
     * @return A new {@link Builder} instance.
     */
    public static Builder builder(String number) {
        return new Builder(number);
    }

    /**
     * This method is the starting point for constructing a Basic Insight request.
     * Note that the number field must be set.
     *
     * @return A new {@link Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected String number, country;

        protected Builder(String number) {
            this.number = number;
        }

        protected Builder() {}

        /**
         * @param number A single phone number that you need insight about in national or international format.
         *
         * @return This builder.
         */
        public Builder number(String number) {
            this.number = number;
            return this;
        }

        /**
         * @param country If a number does not have a country code, or it is uncertain, set the two-character country code.
         *
         * @return This builder.
         */
        public Builder country(String country) {
            this.country = country;
            return this;
        }

        /**
         * @return A new {@link BasicInsightRequest} object from the stored builder options.
         */
        public BasicInsightRequest build() {
            return new BasicInsightRequest(this);
        }
    }
}
