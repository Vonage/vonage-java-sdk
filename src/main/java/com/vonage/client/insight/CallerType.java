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
 * Represents the type of the number's owner as an enum.
 * The value will be {@code BUSINESS} if the owner of a phone number is a business.
 * If the owner is an individual the value will be {@code CONSUMER}.
 * The value will be {@code UNKNOWN} if this information is not available.
 * This parameter is only present if cnam had a value of {@code true} in the request.
 */
public enum CallerType {
    BUSINESS,
    CONSUMER,
    @JsonEnumDefaultValue UNKNOWN;

    /**
     * Convert a string value to a CallerType enum.
     *
     * @param name The string value to convert.
     *
     * @return The caller type as an enum, or {@code null} if invalid.
     */
    @JsonCreator
    public static CallerType fromString(String name) {
        return Jsonable.fromString(name, CallerType.class);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
