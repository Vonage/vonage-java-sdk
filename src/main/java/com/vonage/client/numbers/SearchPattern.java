/*
 *   Copyright 2020 Vonage
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

/**
 * Provided to calls that match substrings, to indicate which part of the string should be considered a match.
 */
public enum SearchPattern {
    STARTS_WITH(0),
    ANYWHERE(1),
    ENDS_WITH(2);

    private final int value;

    SearchPattern(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
