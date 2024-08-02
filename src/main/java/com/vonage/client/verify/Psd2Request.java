/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.verify;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;
import java.util.Map;

/**
 * Describes a PSD2 verify request.
 *
 * @since 5.5.0
 */
public class Psd2Request extends BaseRequest {
    private final Double amount;
    private final String payee;
    private final Workflow workflow;

    private Psd2Request(Builder builder) {
        super(builder.number, builder.length, builder.locale, builder.country, builder.pinExpiry, builder.nextEventWait);
        if ((payee = builder.payee) == null || payee.isEmpty() || payee.length() > 18) {
            throw new IllegalArgumentException("Payee is required and cannot exceed 18 characters.");
        }
        workflow = builder.workflow;
        amount = builder.amount;
    }

    /**
     * @return The decimal amount of the payment to be confirmed, in Euros.
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
     * @return The predefined sequence of SMS and TTS (Text To Speech) actions to use
     * in order to convey the PIN to your user.
     */
    public Workflow getWorkflow() {
        return workflow;
    }

    /**
     * Enumeration representing different verification workflows. See the
     * <a href="https://developer.nexmo.com/verify/guides/workflows-and-events">Workflow and Events documentation</a>
     * for more details.
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
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     * @since 7.9.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Entry point for constructing an instance of this class, with the required parameters.
     *
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

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        params.put("payee", payee);
        params.put("amount", Double.toString(amount));
        if (workflow != null) {
            params.put("workflow_id", Integer.toString(workflow.id));
        }
        return params;
    }

    public static class Builder {
        private Double amount;
        private String payee, number, country;
        private Workflow workflow;
        private Locale locale;
        private Integer length,  pinExpiry, nextEventWait;

        /**
         * @param number The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
         *               format.
         * @param amount The decimal amount of the payment to be confirmed, in Euros.
         * @param payee  An alphanumeric string to indicate to the user the name of the recipient that they
         *               are confirming a payment to.
         */
        public Builder(String number, Double amount, String payee) {
            this.number = number;
            this.payee = payee;
            this.amount = amount;
        }

        private Builder() {
        }

        /**
         * (REQUIRED)
         * @param payee Username of the payment recipient.
         *
         * @return This builder.
         *
         * @since 7.9.0
         */
        public Builder payee(String payee) {
            this.payee = payee;
            return this;
        }

        /**
         * (REQUIRED)
         * @param number The recipient's phone number in E.164 format.
         *
         * @return This builder.
         *
         * @since 7.9.0
         */
        public Builder number(String number) {
            this.number = number;
            return this;
        }

        /**
         * (REQUIRED)
         * @param amount The decimal amount of the payment to be confirmed, in Euros.
         *
         * @return This builder.
         *
         * @since 7.9.0
         */
        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        /**
         * @param workflow Selects the predefined sequence of SMS and TTS (Text To Speech) actions to use
         *                 in order to convey the PIN to your user. For example, an id of 1 identifies the
         *                 workflow SMS - TTS - TTS. For a list of all workflows and their associated ids,
         *                 please visit the
         *                 <a href="https://developer.nexmo.com/verify/guides/workflows-and-events">developer portal.</a>
         * @return This builder.
         */
        public Builder workflow(Workflow workflow){
            this.workflow = workflow;
            return this;
        }

        /**
         * @param locale (optional) Override the default locale used for verification. By default the locale is determined
         *        from the country code included in {@code number}
         * @return This builder.
         */
        public Builder locale(Locale locale) {
            this.locale = locale;
            return this;
        }

        /**
         * @param length (optional) The length of the verification code to be sent to the user. Must be either 4 or 6. Use
         *               -1 to use the default value.
         * @return This builder.
         */
        public Builder length(Integer length) {
            this.length = length;
            return this;
        }

        /**
         * @param pinExpiry (optional) the PIN validity time from generation, in seconds. Default is 300 seconds
         * @return This builder.
         */
        public Builder pinExpiry(Integer pinExpiry) {
            this.pinExpiry = pinExpiry;
            return this;
        }

        /**
         * @param nextEventWait (optional) the wait time between attempts to deliver the PIN. A number between 600-900.
         * @return This builder.
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
         * @return This builder.
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
