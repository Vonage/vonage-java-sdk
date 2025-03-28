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
 * Voice capability configuration settings.
 */
public final class Voice extends Capability {
    private Region region;
    private Boolean signedCallbacks;
    private Integer conversationsTtl, legPersistenceTime;

    private Voice() {
    }

    private Voice(Builder builder) {
        super(builder);
        region = builder.region;
        signedCallbacks = builder.signedCallbacks;
        if ((conversationsTtl = builder.conversationsTtl) != null && (conversationsTtl < 0 || conversationsTtl > 744)) {
            throw new IllegalArgumentException("Conversations TTL cannot be negative.");
        }
        if ((legPersistenceTime = builder.legPersistenceTime) != null && (legPersistenceTime < 0 || legPersistenceTime > 31)) {
            throw new IllegalArgumentException("Leg persistence time must be positive and less than 31 days.");
        }
    }

    /**
     * The region which all inbound, programmable SIP and SIP connect calls will be sent to unless the call is sent
     * to a regional endpoint, if the call is using a regional endpoint this will override the application setting.
     *
     * @return The region to process calls through as an enum, or {@code null} if not specified (the default).
     *
     * @since 7.7.0
     */
    @JsonProperty("region")
    public Region getRegion() {
        return region;
    }

    /**
     * Whether to use signed webhooks for this capability. See
     * <a href=https://developer.vonage.com/en/getting-started/concepts/webhooks#decoding-signed-webhooks>
     * the documentation on webhooks</a> for details.
     *
     * @return {@code true} if webhooks are signed, or {@code null} if unknown (the default).
     *
     * @since 7.7.0
     */
    @JsonProperty("signed_callbacks")
    public Boolean getSignedCallbacks() {
        return signedCallbacks;
    }

    /**
     * The length of time the named conversation will remain active for after creation, in hours. 0 means infinite.
     *
     * @return The time-to-live of the named conversation, or {@code null} if unknown / not applicable (the default).
     *
     * @since 7.7.0
     */
    @JsonProperty("conversations_ttl")
    public Integer getConversationsTtl() {
        return conversationsTtl;
    }

    /**
     * Persistence duration for conversation legs, in days. Maximum value is 31, default is 7.
     *
     * @return The leg persistence time in dats as an integer, or {@code null} if unknown.
     *
     * @since 8.12.0
     */
    @JsonProperty("leg_persistence_time")
    public Integer getLegPersistenceTime() {
        return legPersistenceTime;
    }

    @Override
    public Type getType() {
        return Type.VOICE;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Capability.Builder<Voice, Builder> {
        private Region region;
        private Boolean signedCallbacks;
        private Integer conversationsTtl, legPersistenceTime;

        /**
         * Selecting a region means all inbound, programmable SIP and SIP connect calls will be sent to the selected
         * region unless the call is sent to a regional endpoint, if the call is using a regional endpoint this will
         * override the application setting. This is an optional parameter.
         *
         * @param region The region to process calls through as an enum.
         *
         * @return This builder.
         * @since 7.7.0
         */
        public Builder region(Region region) {
            this.region = region;
            return this;
        }

        /**
         * Whether to use signed webhooks. See
         * <a href=https://developer.vonage.com/en/getting-started/concepts/webhooks#decoding-signed-webhooks>
         * the documentation on webhooks</a> for details.
         *
         * @param signed {@code true} to use signed webhooks, {@code false} otherwise.
         *
         * @return This builder.
         * @since 7.7.0
         */
        public Builder signedCallbacks(boolean signed) {
            this.signedCallbacks = signed;
            return this;
        }

        /**
         * The length of time named conversations will remain active for after creation, in hours.
         * 0 means infinite. Maximum value is 744 (i.e. 31 days).
         *
         * @param ttl The conversations time-to-live in hours.
         *
         * @return This builder.
         * @since 7.7.0
         */
        public Builder conversationsTtl(int ttl) {
            this.conversationsTtl = ttl;
            return this;
        }

        /**
         * Persistence duration for conversation legs, in days. Maximum value is 31, default is 7.
         *
         * @param legPersistenceTime The leg persistence time in days.
         *
         * @return This builder.
         * @since 8.12.0
         */
        public Builder legPersistenceTime(int legPersistenceTime) {
            this.legPersistenceTime = legPersistenceTime;
            return this;
        }

        /**
         * Set the {@code answer_url} webhook for this capability.
         *
         * @param webhook The webhook properties, or {@code null} to remove.
         *
         * @return This builder.
         * @since 9.0.0
         */
        public Builder answer(Webhook webhook) {
            return webhook(Webhook.Type.ANSWER, webhook);
        }

        /**
         * Set the {@code event_url} webhook for this capability.
         *
         * @param webhook The webhook properties, or {@code null} to remove.
         *
         * @return This builder.
         * @since 9.0.0
         */
        public Builder fallbackAnswer(Webhook webhook) {
            return webhook(Webhook.Type.FALLBACK_ANSWER, webhook);
        }

        /**
         * Set the {@code event_url} webhook for this capability.
         *
         * @param webhook The webhook properties, or {@code null} to remove.
         *
         * @return This builder.
         * @since 9.0.0
         */
        public Builder event(Webhook webhook) {
            return webhook(Webhook.Type.EVENT, webhook);
        }

        /**
         * Builds the Voice object.
         *
         * @return A new Voice capability containing the configured properties.
         */
        @Override
        public Voice build() {
            return new Voice(this);
        }
    }
}