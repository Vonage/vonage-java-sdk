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
 * Represents the call terminator in {@linkplain EventWebhook#getDisconnectedBy()}.
 *
 * @since 8.16.2
 */
public enum DisconnectedBy {
    /**
     * The call was terminated by the Voice API platform,
     * for example the NCCO finished its last action and call was disconnected.
     */
    PLATFORM,

    /**
     * The call was terminated by the user,
     * for example the user hung up the call, rejected the call, or didn't answer.
     */
    USER,

    /**
     * The call was terminator is unmapped by this enum.
     */
    UNKNOWN;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * Converts the string representation of the enum to the enum value.
     *
     * @param name The disconnected_by field from the webhook as a string.
     *
     * @return The enum value mapping, or {@code null} if the string is null.
     */
    @JsonCreator
    public static DisconnectedBy fromString(String name) {
        if (name == null) return null;
        try {
            return valueOf(name.toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            return UNKNOWN;
        }
    }
}
