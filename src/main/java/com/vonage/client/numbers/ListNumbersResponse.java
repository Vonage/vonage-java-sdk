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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;

/**
 * Response from a request to list the numbers currently being rented buy an account.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListNumbersResponse extends JsonableBaseObject {
    private int count;
    private OwnedNumber[] numbers;

    @JsonProperty("count")
    public int getCount() {
        return count;
    }

    @JsonProperty("numbers")
    public OwnedNumber[] getNumbers() {
        return numbers != null ? numbers : new OwnedNumber[0];
    }

    public static ListNumbersResponse fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
