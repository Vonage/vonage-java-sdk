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

import com.vonage.client.AbstractQueryParamsRequest;
import com.vonage.client.common.E164;
import java.util.Map;

/**
 * Base class for number management queries.
 *
 * @since 8.10.0
 */
abstract class BaseNumberRequest extends AbstractQueryParamsRequest {
    private final String country, msisdn;

    protected BaseNumberRequest(String country, String msisdn) {
        this.country = validateCountry(country);
        this.msisdn = new E164(msisdn).toString();
    }

    /**
     * Two character country code in ISO 3166-1 alpha-2 format.
     *
     * @return The number's country code.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Phone number in E.164 format.
     *
     * @return The MSISDN as a string.
     */
    public String getMsisdn() {
        return msisdn;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        conditionalAdd("country", country);
        conditionalAdd("msisdn", msisdn);
        return params;
    }

    static String validateCountry(String country) {
        if (country == null || country.length() != 2) {
            throw new IllegalArgumentException("Country code is required in ISO 3166-1 alpha-2 format.");
        }
        return country.toUpperCase();
    }
}
