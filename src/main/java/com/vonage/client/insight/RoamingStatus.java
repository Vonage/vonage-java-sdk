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
package com.vonage.client.insight;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.vonage.client.Jsonable;

/**
 * Represents whether the number is outside its home carrier network, as an enum.
 *
 * @since 9.0.0 Moved to its own class / file from {@linkplain RoamingDetails}.
 */
public enum RoamingStatus {
    @JsonEnumDefaultValue UNKNOWN,
    ROAMING,
    NOT_ROAMING;

    /**
     * Convert a string to a RoamingStatus enum.
     *
     * @param name The string to convert.
     * @return The RoamingStatus enum, or {@code null} if invalid.
     */
    @JsonCreator
    public static RoamingStatus fromString(String name) {
        return Jsonable.fromString(name, RoamingStatus.class);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
