/*
 *   Copyright 2020 Vonage
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
import com.vonage.client.common.HttpMethod;
import com.vonage.client.common.Webhook;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Messages extends Capability {
    private Messages() {

    }

    private Messages(Builder builder) {
        webhooks = builder.webhooks;
    }

    @Override
    public Type getType() {
        return Type.MESSAGES;
    }

    /**
     * @return A new Builder to start building.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Map<Webhook.Type, Webhook> webhooks;

        /**
         * Add a webhook for the Vonage API to use. See https://developer.nexmo.com/concepts/guides/webhooks. Each
         * Capability can only have a single webhook of each type. Any futher adding of webhooks will override an
         * already existing one of that type.
         *
         * @param type    The {@link Webhook.Type} of webhook to add.
         * @param webhook The webhook containing the URL and {@link HttpMethod}.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder addWebhook(Webhook.Type type, Webhook webhook) {
            if (webhooks == null) {
                webhooks = new LinkedHashMap<>();
            }

            webhooks.put(type, webhook);
            return this;
        }

        /**
         * Remove a webhook.
         *
         * @param type The {@link Webhook.Type} to remove.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder removeWebhook(Webhook.Type type) {
            if (webhooks == null) {
                webhooks = new LinkedHashMap<>();
            }

            webhooks.remove(type);

            if (webhooks.isEmpty()) {
                webhooks = null;
            }

            return this;
        }

        /**
         * @return A new Messages capability containing the configured properties.
         */
        public Messages build() {
            return new Messages(this);
        }
    }
}
