/*
 *   Copyright 2023 Vonage
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
import com.vonage.client.Jsonable;

/**
 * Represents the response to a "searchNumbers" request from the Vonage API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchNumbersResponse implements Jsonable {
    private int count = 0;
    private AvailableNumber[] numbers = new AvailableNumber[]{};

    /**
     * @return  the number of responses returned by the Vonage API.
     */
    public int getCount() {
        return count;
    }

    /**
     * Obtain an array of matching numbers than are available to buy.
     * @return Array of available numbers
     */
    public AvailableNumber[] getNumbers() {
        return numbers;
    }

    public static SearchNumbersResponse fromJson(String json) {
        return Jsonable.fromJson(json, SearchNumbersResponse.class);
    }

}
