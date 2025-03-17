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
package com.vonage.client.application.capabilities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Rtc capability configuration settings.
 */
public final class Rtc extends Capability {
    private Boolean signedCallbacks;

    private Rtc() {
    }

    private Rtc(Builder builder) {
        super(builder);
        this.signedCallbacks = builder.signedCallbacks;
    }

    @Override
    public Type getType() {
        return Type.RTC;
    }

    /**
     * Whether to use signed webhooks. This is a way of verifying that the request is coming from Vonage.
     *
     * @return {@code true} if signed webhooks are used, {@code false} if not and {@code null} if unknown.
     *
     * @since 8.12.0
     */
    @JsonProperty("signed_callbacks")
    public Boolean getSignedCallbacks() {
        return signedCallbacks;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends Capability.Builder<Rtc, Builder> {
        private Boolean signedCallbacks;

        /**
         * Constructs a new Builder.
         */
        private Builder() {}

        /**
         * Set whether to use signed webhooks. This is a way of verifying that the request is coming from Vonage.
         *
         * @param signedCallbacks {@code true} if signed webhooks should be used.
         * @return This builder.
         * @since 8.12.0
         */
        public Builder signedCallbacks(boolean signedCallbacks) {
            this.signedCallbacks = signedCallbacks;
            return this;
        }

        @Override
        public Builder addWebhook(Webhook.Type type, Webhook webhook) {
            return super.addWebhook(type, webhook);
        }

        @Override
        public Builder removeWebhook(Webhook.Type type) {
            return super.removeWebhook(type);
        }

        /**
         * Builds the Rtc object.
         *
         * @return A new RTC capability containing the configured properties.
         */
        @Override
        public Rtc build() {
            return new Rtc(this);
        }
    }
}
