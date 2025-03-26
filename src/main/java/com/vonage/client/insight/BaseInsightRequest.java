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

import com.vonage.client.QueryParamsRequest;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseInsightRequest implements QueryParamsRequest {
    protected final String number, country;
    Boolean cnam;

    protected BaseInsightRequest(String number, String country) {
        if ((this.number = number) == null || number.length() < 2) {
            throw new IllegalStateException("Must provide a number for insight.");
        }
        if ((this.country = country) != null && country.length() != 2) {
            throw new IllegalArgumentException("Country code must be 2 letters long.");
        }
    }

    public String getNumber() {
        return number;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = new LinkedHashMap<>(8);
        params.put("number", number);
        if (country != null) {
            params.put("country", country);
        }
        if (cnam != null) {
            params.put("cnam", cnam.toString());
        }
        return params;
    }
}