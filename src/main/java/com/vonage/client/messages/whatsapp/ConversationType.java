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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.messages.MessageStatus;

/**
 * Represents the conversation category as returned by {@link MessageStatus#getWhatsappConversationType()}.
 *
 * @since 8.1.0
 */
public enum ConversationType {
    MARKETING,
    UTILITY,
    AUTHENTICATION,
    REFERRAL_CONVERSION,
    SERVICE;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static ConversationType fromString(String value) {
        if (value == null) return null;
        return ConversationType.valueOf(value.toUpperCase());
    }
}
