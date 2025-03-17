/*
 * Copyright 2025 Vonage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vonage.client.voice.ncco;

import com.vonage.client.JsonableBaseObject;

/**
 * DTMF (Dial Tone Multi Frequency) settings for Input Actions that will be added to a NCCO object.
 */
public class DtmfSettings extends JsonableBaseObject {
    private Integer timeOut, maxDigits;
    private Boolean submitOnHash;

    /**
     * Constructor used by Jackson.
     */
    DtmfSettings() {}

    private DtmfSettings(Builder builder) {
        submitOnHash = builder.submitOnHash;
        if ((timeOut = builder.timeOut) != null && (timeOut < 0 || timeOut > 10)) {
            throw new IllegalArgumentException("'timeOut' must be positive and less than 10 seconds.");
        }
        if ((maxDigits = builder.maxDigits) != null && (maxDigits < 0 || maxDigits > 20)) {
            throw new IllegalArgumentException("'maxDigits' must be positive and less than 20.");
        }
    }

    /**
     * Time to wait in seconds before submitting the event. Default value is 3.
     *
     * @return The DTMF input timeout in seconds as an integer, or {@code null} if unspecified.
     */
    public Integer getTimeOut() {
        return timeOut;
    }

    /**
     * The number of digits the user can press. The maximum value is 20, the default is 4 digits.
     *
     * @return The number of digits as an integer, or {@code null} if unspecified.
     */
    public Integer getMaxDigits() {
        return maxDigits;
    }

    /**
     * Determines if the callee's activity is sent to your webhook endpoint after pressing the hash key.
     *
     * @return {@code true} if the input is submitted on {@code #}, or {@code null} if unspecified.
     */
    public Boolean isSubmitOnHash() {
        return submitOnHash;
    }

    /**
     * Set to {@code true} so the callee's activity is sent to your webhook endpoint at {@code eventUrl}
     * after they press {@code #}. If # is not pressed the result is submitted after {@code timeOut} seconds.
     * The default value is {@code false}. That is, the result is sent to your webhook endpoint after
     * {@code timeOut} seconds.
     *
     * @param submitOnHash Whether to submit the input after pressing the hash key.
     * @deprecated Use the {@linkplain #builder()}. This will be removed in the next major release.
     */
    @Deprecated
    public void setSubmitOnHash(Boolean submitOnHash) {
        this.submitOnHash = submitOnHash;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @return A new Builder.
     * @since 8.9.4
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for specifying DTMF settings.
     *
     * @since 8.9.4
     */
    public static final class Builder {
        private Integer timeOut, maxDigits;
        private Boolean submitOnHash;

        private Builder() {}

        /**
         * The result of the callee's activity is sent to the {@code eventUrl} webhook endpoint
         * {@code timeOut} seconds after the last action. The default value is 3. Max is 10.
         *
         * @param timeOut The DTMF input timeout in seconds as an int.
         * @return This builder.
         */
        public Builder timeOut(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * The number of digits the user can press. The maximum value is 20, the default is 4 digits.
         *
         * @param maxDigits The number of digits as an int.
         * @return This builder.
         */
        public Builder maxDigits(int maxDigits) {
            this.maxDigits = maxDigits;
            return this;
        }

        /**
         * Set to {@code true} so the callee's activity is sent to your webhook endpoint at {@code eventUrl}
         * after they press {@code #}. If # is not pressed the result is submitted after {@code timeOut}
         * seconds. The default value is {@code false}. That is, the result is sent to your webhook
         * endpoint after {@code timeOut} seconds.
         *
         * @param submitOnHash Whether to submit the input after pressing the hash key.
         * @return This builder.
         */
        public Builder submitOnHash(boolean submitOnHash) {
            this.submitOnHash = submitOnHash;
            return this;
        }

        /**
         * Builds the DtmfSettings with this builder's properties.
         *
         * @return A new DtmfSettings instance.
         */
        public DtmfSettings build() {
            return new DtmfSettings(this);
        }
    }
}