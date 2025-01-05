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
package com.vonage.client.sms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the SMS message type.
 *
 * @since 8.3.0
 */
public enum MessageType {
    TEXT, UNICODE, BINARY, UNKNOWN;

    @JsonCreator
    public static MessageType fromString(String name) {
        try {
            return MessageType.valueOf(name.toUpperCase());
        }
        catch (IllegalArgumentException | NullPointerException ex) {
            return UNKNOWN;
        }
    }

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
