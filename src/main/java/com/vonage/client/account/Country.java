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
package com.vonage.client.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Groups the country details in {@link PricingResponse}.
 */
public class Country extends JsonableBaseObject {
    private String code, displayName, name;

    /**
     * Two-letter country code.
     *
     * @return The country code as a string.
     */
    @JsonProperty("countryCode")
    public String getCode() {
        return code;
    }

    /**
     * Readable alternate country name.
     *
     * @return The country display name.
     */
    @JsonProperty("countryDisplayName")
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Country name.
     *
     * @return The country name.
     */
    @JsonProperty("countryName")
    public String getName() {
        return name;
    }
}
