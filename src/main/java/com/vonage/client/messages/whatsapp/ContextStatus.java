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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
/**
 * Represents the WhatsApp {@code context_status} field in {@link com.vonage.client.messages.InboundMessage}.
 * <p>
 * Describes whether there is a context for this inbound message or not. If there is a context, and it is available,
 * the context details will be contained in a context object. If there is a context, but it is unavailable,
 * or if there is no context for message (none), then there will be no context object included in the body.
 *
 * @since 8.1.0
 */
public enum ContextStatus {
    NONE,
    AVAILABLE,
    UNAVAILABLE;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static ContextStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        return ContextStatus.valueOf(value.toUpperCase());
    }
}
