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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.Jsonable;

/**
 * Represents the input mode for {@link InputAction}.
 *
 * @since 8.12.0
 */
public enum InputMode {
    /**
     * Process input synchronously (the default). This is the standard behaviour if not set.
     */
    SYNCHRONOUS,

    /**
     * Process input asynchronously. For DTMF, this means that the input is sent to the event URL webhook one digit
     * at a time. Consequently, the {@link DtmfSettings} are ignored.
     */
    ASYNCHRONOUS;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * Convert a string into an {@link InputMode} enum.
     *
     * @param name The input mode name as a string.
     *
     * @return The {@link InputMode} enum, or {@code null} if invalid.
     */
    @JsonCreator
    public static InputMode fromString(String name) {
        return Jsonable.fromString(name, InputMode.class);
    }
}