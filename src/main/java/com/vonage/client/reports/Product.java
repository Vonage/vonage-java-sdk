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
package com.vonage.client.reports;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents a Vonage product type for Reports API requests.
 */
public enum Product {
    SMS("SMS"),
    SMS_TRAFFIC_CONTROL("SMS-TRAFFIC-CONTROL"),
    VOICE_CALL("VOICE-CALL"),
    VOICE_FAILED("VOICE-FAILED"),
    VOICE_TTS("VOICE-TTS"),
    IN_APP_VOICE("IN-APP-VOICE"),
    WEBSOCKET_CALL("WEBSOCKET-CALL"),
    ASR("ASR"),
    AMD("AMD"),
    VERIFY_API("VERIFY-API"),
    VERIFY_V2("VERIFY-V2"),
    NUMBER_INSIGHT("NUMBER-INSIGHT"),
    CONVERSATION_EVENT("CONVERSATION-EVENT"),
    CONVERSATION_MESSAGE("CONVERSATION-MESSAGE"),
    MESSAGES("MESSAGES"),
    VIDEO_API("VIDEO-API"),
    NETWORK_API_EVENT("NETWORK-API-EVENT"),
    REPORTS_USAGE("REPORTS-USAGE");

    private final String value;

    Product(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Product fromValue(String value) {
        if (value == null) return null;
        for (Product p : Product.values()) {
            if (p.value.equalsIgnoreCase(value)) return p;
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
