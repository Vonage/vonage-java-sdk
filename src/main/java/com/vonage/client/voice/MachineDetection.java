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
 * Represents machine detection behaviour.
 */
public enum MachineDetection {
    /**
     * Vonage sends an HTTP request to the event URL with the Call event machine.
     */
    CONTINUE,

    /**
     * End the call on encounter.
     */
    HANGUP,

    /**
     * Unmapped / invalid value.
     */
    UNKNOWN;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * Convert a string to a MachineDetection enum.
     *
     * @param name The string to convert to a MachineDetection enum.
     *
     * @return The machine detection mode as an enum, or {@linkplain #UNKNOWN} if an invalid value is provided.
     */
    @JsonCreator
    public static MachineDetection fromString(String name) {
        try {
            return MachineDetection.valueOf(name.toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            return UNKNOWN;
        }
    }
}
