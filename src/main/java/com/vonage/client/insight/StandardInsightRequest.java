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

public class StandardInsightRequest extends BaseInsightRequest {
    private StandardInsightRequest(Builder builder) {
        number = builder.number;
        country = builder.country;
        cnam = builder.cnam;
    }

    public static Builder builder(String number) {
        return new Builder(number);
    }

    public Boolean getCnam() {
        return cnam;
    }

    /**
     * Construct a StandardInsightRequest with a number.
     *
     * @param number A single phone number that you need insight about in national or international format.
     *
     * @return A new {@link StandardInsightRequest} object.
     */
    public static StandardInsightRequest withNumber(String number) {
        return new Builder(number).build();
    }

    /**
     * Construct a StandardInsightRequest with a number and country.
     *
     * @param number  A single phone number that you need insight about in national or international format.
     * @param country If a number does not have a country code or it is uncertain, set the two-character country code.
     *
     * @return A new {@link StandardInsightRequest} object.
     */
    public static StandardInsightRequest withNumberAndCountry(String number, String country) {
        return new Builder(number).country(country).build();
    }

    public static class Builder {
        protected String number;
        protected String country;
        protected Boolean cnam;

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
         * @param country If a number does not have a country code or it is uncertain, set the two-character country code.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder country(String country) {
            this.country = country;
            return this;
        }

        /**
         * @param cnam Indicates if the name of the person who owns the phone number should also be looked up and returned.
         *             Set to true to receive phone number owner name in the response. This is only available for US numbers
         *             and incurs an additional charge.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder cnam(Boolean cnam) {
            this.cnam = cnam;
            return this;
        }

        /**
         * @return A new {@link StandardInsightRequest} object from the stored builder options.
         */
        public StandardInsightRequest build() {
            return new StandardInsightRequest(this);
        }
    }
}
