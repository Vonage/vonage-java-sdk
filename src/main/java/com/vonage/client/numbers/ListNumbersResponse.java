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
 * Response from a request to list the numbers currently being rented buy an account.
 */
public class ListNumbersResponse extends JsonableBaseObject {
    private Integer count;
    private OwnedNumber[] numbers;

    /**
     * Total amount of numbers owned by the account.
     * Note that this may not be the same as the size of {@linkplain #getNumbers()}.
     *
     * @return The total number of owned numbers as an integer, or {@code null} if unknown.
     */
    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    /**
     * A paginated array of owned numbers and their details.
     * Number of results will depend on what you set in {@linkplain ListNumbersFilter#getSize()}.
     *
     * @return The owned numbers as an array.
     */
    @JsonProperty("numbers")
    public OwnedNumber[] getNumbers() {
        return numbers != null ? numbers : new OwnedNumber[0];
    }
}
