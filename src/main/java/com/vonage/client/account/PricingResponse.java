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
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.math.BigDecimal;
import java.util.List;

/**
 * Pricing information for a specific country.
 */
public class PricingResponse extends JsonableBaseObject {
    private String dialingPrefix;
    private BigDecimal defaultPrice;
    private String currency;
    @JsonUnwrapped private Country country;
    private List<Network> networks;

    @Deprecated
    public PricingResponse() {
    }

    /**
     * Dialing prefix for the country.
     *
     * @return The dialing prefix as a string.
     */
    @JsonProperty("dialingPrefix")
    public String getDialingPrefix() {
        return dialingPrefix;
    }

    /**
     * Default price for the country.
     *
     * @return The default price.
     */
    @JsonProperty("defaultPrice")
    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    /**
     * Currency that your account is being billed in (EUR by default).
     *
     * @return The billing currency code as a string.
     */
    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    /**
     * Details of the country this pricing information is for.
     *
     * @return The country.
     */
    @JsonProperty("country")
    public Country getCountry() {
        return country;
    }

    /**
     * List of supported networks in the country.
     *
     * @return The list of networks.
     */
    @JsonProperty("networks")
    public List<Network> getNetworks() {
        return networks;
    }

    /**
     * Creates an instance of this class from a JSON payload.
     *
     * @param json The JSON string to parse.
     * @return An instance of this class with the fields populated, if present.
     * @deprecated This will be removed in the next major release.
     */
    @Deprecated
    public static PricingResponse fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
