/*
 * Copyright (c) 2011-2019 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.application.capabilities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nexmo.client.common.Webhook;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Messages extends Capability {
    private Messages() {

    }

    private Messages(Builder builder) {
        this.webhooks = builder.webhooks;
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
         * Add a webhook for the Nexmo API to use. See https://developer.nexmo.com/concepts/guides/webhooks. Each
         * Capability can only have a single webhook of each type. Any futher adding of webhooks will override an
         * already existing one of that type.
         *
         * @param type    The {@link Webhook.Type} of webhook to add.
         * @param webhook The webhook containing the URL and {@link com.nexmo.client.common.HttpMethod}.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder addWebhook(Webhook.Type type, Webhook webhook) {
            if (this.webhooks == null) {
                this.webhooks = new LinkedHashMap<>();
            }

            this.webhooks.put(type, webhook);
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
            if (this.webhooks == null) {
                this.webhooks = new LinkedHashMap<>();
            }

            this.webhooks.remove(type);

            if (this.webhooks.isEmpty()) {
                this.webhooks = null;
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
