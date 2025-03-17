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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents the response to a "searchNumbers" request from the Vonage API.
 */
public class SearchNumbersResponse extends JsonableBaseObject {
    private int count;
    private AvailableNumber[] numbers;

    /**
     * Total amount of numbers available in the pool.
     * Note that this is not the same as the size of {@linkplain #getNumbers()}.
     *
     * @return The total number of owned numbers.
     */
    @JsonProperty("count")
    public int getCount() {
        return count;
    }

    /**
     * A paginated array of available numbers and their details.
     * Number of results will depend on what you set in {@linkplain SearchNumbersFilter#getSize()}.
     *
     * @return The available numbers as an array.
     */
    @JsonProperty("numbers")
    public AvailableNumber[] getNumbers() {
        return numbers != null ? numbers : new AvailableNumber[0];
    }
}
