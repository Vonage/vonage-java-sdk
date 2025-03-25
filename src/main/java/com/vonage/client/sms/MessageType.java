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
import com.vonage.client.Jsonable;

/**
 * Represents the SMS message type.
 *
 * @since 8.3.0
 */
public enum MessageType {
    TEXT, UNICODE, BINARY;

    /**
     * Convert a string value to a MessageType enum.
     *
     * @param name The string value to convert.
     *
     * @return The message type as an enum, or {@code null} if invalid.
     */
    @JsonCreator
    public static MessageType fromString(String name) {
        return Jsonable.fromString(name, MessageType.class);
    }

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
