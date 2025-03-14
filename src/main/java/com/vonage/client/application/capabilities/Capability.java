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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.common.Webhook;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a capability of a Vonage Application
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public abstract class Capability extends JsonableBaseObject {
    protected Map<Webhook.Type, Webhook> webhooks;

    protected Capability() {
    }

    protected Capability(Builder<?, ?> builder) {
        webhooks = builder.webhooks;
    }

    /**
     * The capability's type.
     *
     * @return This capability's type as an enum.
     */
    @JsonIgnore
    abstract public Type getType();

    /**
     * Webhooks grouped by type.
     *
     * @return The webhooks as a Map, or {@code null} if there are none.
     */
    @JsonProperty("webhooks")
    public Map<Webhook.Type, Webhook> getWebhooks() {
        return webhooks;
    }

    /**
     * Represents the API this capability relates to.
     */
    public enum Type {

        /**
         * Voice API
         */
        VOICE,

        /**
         * RTC
         */
        RTC,

        /**
         * Messages API
         */
        MESSAGES,

        /**
         * VBC
         */
        VBC,

        /**
         * Verify API
         *
         * @since 8.6.0
         */
        VERIFY,

        /**
         * Network APIs
         *
         * @since 8.12.0
         */
        NETWORK
    }

    @SuppressWarnings("unchecked")
    protected static abstract class Builder<C extends Capability, B extends Builder<C, B>> {
        Map<Webhook.Type, Webhook> webhooks;

        /**
         * Add a webhook for the Vonage API to use. Each Capability can only have a single webhook of each type.
         * See <a href="https://developer.vonage.com/concepts/guides/webhooks"> the webhooks guide</a> for details.
         *
         * @param type    The {@link Webhook.Type} of webhook to add.
         * @param webhook The webhook containing the URL and {@link HttpMethod}.
         *
         * @return This builder.
         */
        B addWebhook(Webhook.Type type, Webhook webhook) {
            if (webhooks == null) {
                webhooks = new LinkedHashMap<>();
            }
            webhooks.put(type, webhook);
            return (B) this;
        }

        /**
         * Remove a webhook.
         *
         * @param type The {@link Webhook.Type} to remove.
         *
         * @return This builder.
         */
        B removeWebhook(Webhook.Type type) {
            if (webhooks != null) {
                webhooks.remove(type);
                if (webhooks.isEmpty()) {
                    webhooks = null;
                }
            }
            return (B) this;
        }

        /**
         * Constructs the capability with this builder's properties.
         *
         * @return A new instance of the capability.
         */
        public abstract C build();
    }
}
