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
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

/**
 * Represents the capabilities of a phone number.
 *
 * @since 8.10.0
 */
public enum Feature {
    SMS,
    MMS,
    VOICE;

    @JsonValue
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Converts the string representation of the feature to its enum value.
     *
     * @param feature The feature as a string.
     * @return The enum value of the feature.
     */
    @JsonCreator
    public static Feature fromString(String feature) {
        if (feature == null) return null;
        return valueOf(feature);
    }

    static Feature[] setFromString(String[] features) {
        return features == null ? null : Arrays.stream(features).map(Feature::fromString).toArray(Feature[]::new);
    }

    static String[] getToString(Feature[] features) {
        return features == null ? null : Arrays.stream(features).map(Feature::toString).toArray(String[]::new);
    }
}
