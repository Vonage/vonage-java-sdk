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

import com.vonage.client.common.Webhook;

/**
 * Verify capability configuration settings.
 *
 * @since 8.6.0
 */
public final class Verify extends Capability {

    private Verify() {
    }

    private Verify(Builder builder) {
        super(builder);
    }

    @Override
    public Type getType() {
        return Type.VERIFY;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends Capability.Builder<Verify, Builder> {

        private Builder() {}

        @Override
        public Builder addWebhook(Webhook.Type type, Webhook webhook) {
            return super.addWebhook(type, webhook);
        }

        /**
         * Builds the Verify object with this builder's properties.
         *
         * @return A new Verify capability.
         */
        @Override
        public Verify build() {
            return new Verify(this);
        }
    }
}
