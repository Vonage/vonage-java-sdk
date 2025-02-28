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
 * Represents the direction of a call.
 */
public enum CallDirection {
    /**
     * Outgoing call.
     */
    OUTBOUND,

    /**
     * Incoming call.
     */
    INBOUND,

    /**
     * Unknown call direction.
     */
    UNKNOWN;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * Convert a string to a CallDirection enum.
     *
     * @param name The call direction as a string.
     *
     * @return The call direction as an enum, or {@link #UNKNOWN} if an invalid value was provided.
     */
    @JsonCreator
    public static CallDirection fromString(String name) {
        try {
            return CallDirection.valueOf(name.toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            return UNKNOWN;
        }
    }
}