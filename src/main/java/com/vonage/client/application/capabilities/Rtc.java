/*
 *   Copyright 2023 Vonage
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vonage.client.common.Webhook;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Rtc extends Capability {

    private Rtc() {
    }

    private Rtc(Builder builder) {
        webhooks = builder.webhooks;
    }

    @Override
    public Type getType() {
        return Type.RTC;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Capability.Builder<Rtc, Builder> {

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
