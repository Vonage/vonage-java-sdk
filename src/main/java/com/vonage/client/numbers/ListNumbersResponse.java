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
import java.util.List;

/**
 * Response from a request to list the numbers currently being rented buy an account.
 */
class ListNumbersResponse extends JsonableBaseObject {
    @JsonProperty("count") private Integer count;
    @JsonProperty("numbers") private List<OwnedNumber> numbers;

    /**
     * A paginated array of owned numbers and their details.
     * Number of results will depend on what you set in {@linkplain ListNumbersFilter#getSize()}.
     *
     * @return The owned numbers as an array.
     */
    public List<OwnedNumber> getNumbers() {
        return numbers != null ? numbers : null;
    }
}
