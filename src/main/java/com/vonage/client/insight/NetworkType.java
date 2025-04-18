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
 * Enum representing the type of network that the number is associated with.
 *
 * @since 9.0.0 Moved to its own class / file from {@linkplain CarrierDetails}.
 */
public enum NetworkType {
    MOBILE,
    LANDLINE,
    LANDLINE_PREMIUM,
    LANDLINE_TOLLFREE,
    VIRTUAL,
    @JsonEnumDefaultValue UNKNOWN,
    PAGER;

    /**
     * Convert a string to a NetworkType enum.
     *
     * @param name The string to convert.
     * @return The NetworkType enum, or {@code null} if invalid.
     */
    @JsonCreator
    public static NetworkType fromString(String name) {
        return Jsonable.fromString(name, NetworkType.class);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
