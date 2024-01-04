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
 * Describes a Verify request.
 */
public class VerifyRequest extends BaseRequest {
    private final String brand, from, pinCode;
    private final Workflow workflow;

    public VerifyRequest(Builder builder) {
        super(builder.number, builder.length, builder.locale, builder.country, builder.pinExpiry, builder.nextEventWait);
        if ((brand = builder.brand) != null && brand.length() > 18) {
            throw new IllegalArgumentException("Brand '"+brand+"' is longer than 18 characters.");
        }
        if ((pinCode = builder.pinCode) != null && (pinCode.length() < 4 || pinCode.length() > 10)) {
            throw new IllegalArgumentException("Pin code must be between 4 and 10 characters.");
        }
        from = builder.senderId;
        workflow = builder.workflow;
    }

    /**
     * @return The name of the company or app to be verified for.
     */
    public String getBrand() {
        return this.brand;
    }

    /**
     * @return the short alphanumeric string to specify the SenderID for SMS sent by Verify, or {@code null} if one was
     * not provided. This value is specified in some {@link BaseRequest}  sub-class constructors.
     * <p>
     * If this value is {@code null</tt>, the sender_id used will be <tt>VERIFY}.
     */
    public String getFrom() {
        return from;
    }

    /**
     * @return The predefined sequence of SMS and TTS (Text To Speech) actions to use in order to convey the PIN to your
     * user.
     */
    public Workflow getWorkflow() {
        return workflow;
    }

    /**
     * A custom PIN to send to the user. If a PIN is not provided, Verify will generate a random PIN for you.
     * This feature is not enabled by default - please discuss with your Account Manager if you would like it enabled.
     *
     * @return The custom pin code as a string, or {@code null} if not set.
     *
     * @since 7.5.0
     */
    public String getPinCode() {
        return pinCode;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        if (brand != null) {
            params.put("brand", brand);
        }
        if (from != null) {
            params.put("sender_id", from);
        }
        if (pinCode != null) {
            params.put("pin_code", pinCode);
        }
        if (workflow != null) {
            params.put("workflow_id", String.valueOf(workflow.id));
        }
        return params;
    }

    /**
     * Enumeration representing different verification workflows.
     * <p>
     * See: <a href="https://developer.nexmo.com/verify/guides/workflows-and-events">https://developer.nexmo.com/verify/guides/workflows-and-events</a> for more details.
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

    @Override
    public String toString() {
        return "VerifyRequest{" +
                super.toString() +
                ", brand='" + brand + '\'' +
                ", workflow=" + workflow +
                '}';
    }

    /**
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *        format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *        characters.
     * @return A new Builder to start building.
     */
    public static Builder builder(String number, String brand) {
        return new Builder(number, brand);
    }

    /**
     * Builder to create a Two Factor Authentication request
     * @since 5.5.0
     */
    public static class Builder {
        private String brand, senderId, number, country, pinCode;
        private Integer length, pinExpiry, nextEventWait;
        private Workflow workflow;
        private Locale locale;

        /**
         * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
         *               format.
         * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
         *               characters.
         */
        public Builder(String number, String brand) {
            this.number = number;
            this.brand = brand;
        }

        /**
         * @param senderId the short alphanumeric string to specify the SenderID for SMS sent by Verify.
         *
         * @return This builder.
         */
        public Builder senderId(String senderId) {
            this.senderId = senderId;
            return this;
        }

        /**
         * Set the predefined sequence of SMS and TTS (Text To Speech) actions to use in order to convey the PIN to your
         * user. See <a href="https://developer.vonage.com/verify/guides/workflows-and-events">https://developer.vonage.com/verify/guides/workflows-and-events</a>
         *
         * @param workflow The workflow to use for conveying the PIN to your user.
         * @return This builder.
         */
        public Builder workflow(Workflow workflow) {
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

        /**
         * A custom PIN to send to the user. If a PIN is not provided, Verify will generate a random PIN for you. This
         * feature is not enabled by default - please discuss with your Account Manager if you would like it enabled.
         *
         * @param pinCode The custom code as a string.
         *
         * @return This builder.
         * @since 7.5.0
         */
        public Builder pinCode(String pinCode) {
            this.pinCode = pinCode;
            return this;
        }

        /**
         * Builds the VerifyRequest.
         *
         * @return A new VerifyRequest with this builder's properties.
         */
        public VerifyRequest build() {
            return new VerifyRequest(this);
        }
    }
}
