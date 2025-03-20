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

/**
 * Messages capability configuration settings.
 */
public final class Messages extends Capability {

    private Messages() {
    }

    private Messages(Builder builder) {
        super(builder);
    }

    @Override
    public Type getType() {
        return Type.MESSAGES;
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

        @Override
        public Builder addWebhook(Webhook.Type type, Webhook webhook) {
            return super.addWebhook(type, webhook);
        }

        @Override
        public Builder removeWebhook(Webhook.Type type) {
            return super.removeWebhook(type);
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
