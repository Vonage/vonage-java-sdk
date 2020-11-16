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
package com.vonage.client.verify;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

/**
 * Describes a PSD2 verify request when passed to {@link Psd2Endpoint}
 * @since 5.5.0
 */
public class Psd2Request extends BaseRequest {

    private Double amount;
    private String payee;
    private Workflow workflow;


    private Psd2Request(Builder builder){
        super(builder.number,builder.length, builder.locale, builder.country, builder.pinExpiry, builder.nextEventWait);
        amount = builder.amount;
        payee = builder.payee;
        workflow = builder.workflow;
    }

    /**
     * @return The decimal amount of the payment to be confirmed, in Euros
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @return An alphanumeric string to indicate to the user the name of the recipient that they are confirming a payment to.
     */
    public String getPayee() {
        return payee;
    }

    /**
     * @return The predefined sequence of SMS and TTS (Text To Speech) actions to use in order to convey the PIN to your
     * user.
     */
    public Workflow getWorkflow() {
        return workflow;
    }

    /**
     * Enumeration representing different verification workflows.
     * <p>
     * See: https://developer.nexmo.com/verify/guides/workflows-and-events for more details.
     */
    public enum Workflow {
        /**
         * The default workflow.
         */
        SMS_TTS_TTS(1),
        SMS_SMS_TTS(2),
        TTS_TTS(3),
        SMS_SMS(4),
        SMS_TTS(5),
        SMS(6),
        TTS(7);

        private final int id;

        Workflow(int id) {
            this.id = id;
        }

        @JsonValue
        public int getId() {
            return id;
        }
    }

    /**
     * @param number The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param amount The decimal amount of the payment to be confirmed, in Euros.
     * @param payee  An alphanumeric string to indicate to the user the name of the recipient that they
     *               are confirming a payment to.
     * @return A new Builder to start building.
     */
    public static Builder builder(String number, Double amount, String payee) {
        return new Builder(number, amount, payee);
    }

    @Override
    public String toString() {
        return "Psd2Request{" +
                super.toString() +
                ", amount=" + amount +
                ", payee='" + payee + '\'' +
                ", workflow=" + workflow +
                '}';
    }

    public static class Builder {

        private Double amount;
        private String payee;
        private Workflow workflow;
        private String number;
        private Locale locale;
        private Integer length;
        private Integer pinExpiry;
        private Integer nextEventWait;
        private String country;

        /**
         * @param number The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
         *               format.
         * @param amount The decimal amount of the payment to be confirmed, in Euros.
         * @param payee  An alphanumeric string to indicate to the user the name of the recipient that they
         *               are confirming a payment to.
         */
        public Builder(String number, Double amount, String payee){
            this.number = number;
            this.payee = payee;
            this.amount = amount;
        }

        /**
         * @param workflow Selects the predefined sequence of SMS and TTS (Text To Speech) actions to use
         *                 in order to convey the PIN to your user. For example, an id of 1 identifies the
         *                 workflow SMS - TTS - TTS. For a list of all workflows and their associated ids,
         *                 please visit the
         *                 <a href="https://developer.nexmo.com/verify/guides/workflows-and-events">developer portal.</a>
         * @return {@link Builder}
         */
        public Builder workflow(Workflow workflow){
            this.workflow = workflow;
            return this;
        }

        /**
         * @param locale (optional) Override the default locale used for verification. By default the locale is determined
         *        from the country code included in {@code number}
         * @return {@link Builder}
         */
        public Builder locale(Locale locale) {
            this.locale = locale;
            return this;
        }

        /**
         * @param length (optional) The length of the verification code to be sent to the user. Must be either 4 or 6. Use
         *               -1 to use the default value.
         * @return {@link Builder}
         */
        public Builder length(Integer length) {
            this.length = length;
            return this;
        }

        /**
         * @param pinExpiry (optional) the PIN validity time from generation, in seconds. Default is 300 seconds
         * @return {@link Builder}
         */
        public Builder pinExpiry(Integer pinExpiry) {
            this.pinExpiry = pinExpiry;
            return this;
        }

        /**
         * @param nextEventWait (optional) the wait time between attempts to deliver the PIN. A number between 600-900.
         * @return {@link Builder}
         */
        public Builder nextEventWait(Integer nextEventWait) {
            this.nextEventWait = nextEventWait;
            return this;
        }

        /**
         * The country for the destination phone number.
         * <p>
         * If you wish to used localised number formats or you are not sure if number is correctly formatted, set this to a
         * two-character country code. For example, GB, US. Verify will work out the international phone number for you.
         * </p>
         *
         * @param country  a String containing a 2-character country code
         * @return {@link Builder}
         */
        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Psd2Request build() {
            return new Psd2Request(this);
        }

    }
}
