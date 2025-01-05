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

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Provided to calls that match substrings, to indicate which part of the string should be considered a match.
 */
public enum SearchPattern {
    /**
     * Search for numbers that start with the pattern (Note: all numbers are in E.164 format,
     * so the starting pattern includes the country code, such as 1 for USA).
     */
    STARTS_WITH(0),

    /**
     * Search for numbers that contain the pattern.
     */
    ANYWHERE(1),

    /**
     * Search for numbers that end with the pattern.
     */
    ENDS_WITH(2);

    private final int value;

    SearchPattern(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
