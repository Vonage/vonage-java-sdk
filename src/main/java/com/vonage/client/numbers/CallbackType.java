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
package com.vonage.client.numbers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.vonage.client.Jsonable;

/**
 * Represents the callback type for voice.
 *
 * @since 9.0.0 Moved from {@linkplain UpdateNumberRequest}.
 */
public enum CallbackType {
    SIP,

    TEL,

    APP;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * Creates the enum from its string representation.
     *
     * @param type The serialized callback type as a string.
     *
     * @return Enum representation of the callback type, or {@code null} if invalid.
     *
     * @since 8.10.0
     */
    @JsonCreator
    public static CallbackType fromString(String type) {
        return Jsonable.fromString(type, CallbackType.class);
    }
}
