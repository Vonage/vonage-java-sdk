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
import com.vonage.client.messages.MessagesVersion;

/**
 * Messages capability configuration settings.
 */
public final class Messages extends Capability {
    private MessagesVersion version;

    private Messages() {
    }

    private Messages(Builder builder) {
        super(builder);
        version = builder.version;
    }

    @Override
    public Type getType() {
        return Type.MESSAGES;
    }

    /**
     * Gets the Messages API version for the application.
     *
     * @return The version as an enum, or {@code null} if unknown.
     * @since 9.0.0
     */
    @JsonProperty("version")
    public MessagesVersion getVersion() {
        return version;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Capability.Builder<Messages, Builder> {
        private MessagesVersion version;

        private Builder() {}

        /**
         * Set the Messages API version for the application.
         *
         * @param version The version as an enum.
         *
         * @return This builder.
         * @since 9.0.0
         */
        public Builder version(MessagesVersion version) {
            this.version = version;
            return this;
        }

        /**
         * Set the {@code inbound_url} webhook for this capability.
         *
         * @param webhook The webhook properties, or {@code null} to remove.
         *
         * @return This builder.
         * @since 9.0.0
         */
        public Builder inbound(Webhook webhook) {
            return webhook(Webhook.Type.INBOUND, webhook);
        }

        /**
         * Set the {@code status_url} webhook for this capability.
         *
         * @param webhook The webhook properties, or {@code null} to remove.
         *
         * @return This builder.
         * @since 9.0.0
         */
        public Builder status(Webhook webhook) {
            return webhook(Webhook.Type.STATUS, webhook);
        }

        /**
         * Builds the Messages object.
         *
         * @return A new Messages capability containing the configured properties.
         */
        @Override
        public Messages build() {
            return new Messages(this);
        }
    }
}
