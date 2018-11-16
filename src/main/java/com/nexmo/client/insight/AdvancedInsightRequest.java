/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
package com.nexmo.client.insight;

public class AdvancedInsightRequest extends BaseInsightRequest {
    private AdvancedInsightRequest(Builder builder) {
        this.number = builder.number;
        this.country = builder.country;
        this.cnam = builder.cnam;
        this.ipAddress = builder.ipAddress;
    }

    public Boolean getCnam() {
        return cnam;
    }

    public String getIpAddress() {
        return ipAddress;
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
         * @param ipAddress The IP address of the user. If supplied, we will compare this to the country the user's phone
         *                  is located in and return an error if it does not match.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
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
