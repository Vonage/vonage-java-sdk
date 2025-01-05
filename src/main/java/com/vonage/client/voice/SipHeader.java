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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents standard headers for SIP Connect endpoint.
 *
 * @since 8.9.0
 */
public enum SipHeader {
    USER_TO_USER;

    @JsonValue
    @Override
    public String toString() {
        return "User-to-User";
    }

    /**
     * Converts a SIP header string to a known enum.
     *
     * @param value String representation of the SIP header.
     * @return The SIP header enum, or {@code null} if the value was empty.
     * @throws IllegalArgumentException If this enum type has no constant with the specified name.
     */
    @JsonCreator
    public static SipHeader fromString(String value) {
        if (value == null || value.isEmpty()) return null;
        return valueOf(value.toUpperCase().replace('-', '_'));
    }
}
