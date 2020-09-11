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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vonage.client.common.Webhook;

import java.util.Map;

/**
 * Represents a capability of a Vonage Application
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Capability {
    protected Map<Webhook.Type, Webhook> webhooks;

    protected Capability() {
        // Needed for Reflection
    }

    @JsonIgnore
    abstract public Type getType();

    public Map<Webhook.Type, Webhook> getWebhooks() {
        return webhooks;
    }

    public enum Type {
        VOICE,
        RTC,
        MESSAGES,
        VBC
    }
}
