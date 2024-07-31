/*
 *   Copyright 2024 Vonage
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
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration representing the type of number.
 */
public enum Type {
    LANDLINE,
    MOBILE_LVN,
    LANDLINE_TOLL_FREE,
    UNKNOWN;

    /**
     * Serialized name.
     *
     * @return The string value.
     * @deprecated Use {@link #toString()}
     */
    @Deprecated
    public String getType() {
        return toString();
    }

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase().replace('_', '-');
    }

    /**
     * Creates an instance of this enum from its serialized string representation.
     *
     * @param type The number type as a string.
     * @return An enum representation of the number type.
     */
    @JsonCreator
    public static Type fromString(String type) {
        if (type == null) return null;
        try {
            return Type.valueOf(type.toUpperCase().replace('-', '_'));
        }
        catch (IllegalArgumentException ex) {
            return UNKNOWN;
        }
    }
}
