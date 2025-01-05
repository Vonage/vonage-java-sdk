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

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.JsonableBaseObject;
import java.math.BigDecimal;

/**
 * Represents a network in {@link PricingResponse}.
 */
public class Network extends JsonableBaseObject {
    private Type type;
    private BigDecimal price;
    private String currency, mcc, mnc, code, name;

    @Deprecated
    public Network() {
    }

    /**
     * Network type.
     *
     * @return The type of network as an enum.
     */
    @JsonProperty("type")
    public Type getType() {
        return type;
    }

    /**
     * Cost to send a message or make a call to this network
     *
     * @return The network price, or {@code null} if unknown.
     */
    @JsonProperty("price")
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Currency code for the network price.
     *
     * @return The currency code as a string, or {@code null} if unknown.
     */
    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    /**
     * Mobile Country Code of the operator.
     *
     * @return The network operator's mobile country code as a string, or {@code null} if unknown.
     */
    @JsonProperty("mcc")
    public String getMcc() {
        return mcc;
    }

    /**
     * Mobile Network Code of the operator.
     *
     * @return The network operator's code as a string, or {@code null} if unknown.
     */
    @JsonProperty("mnc")
    public String getMnc() {
        return mnc;
    }

    /**
     * Mobile Country Code and Mobile Network Code combined to give a unique reference for the operator.
     *
     * @return The network code (MCC and MNC) as a string, or {@code null} if unknown.
     */
    @JsonProperty("networkCode")
    public String getCode() {
        return code;
    }

    /**
     * Company/organisational name of the operator.
     *
     * @return The network operator name, or {@code null} if unknown.
     */
    @JsonProperty("networkName")
    public String getName() {
        return name;
    }

    /**
     * Represents the type of network.
     */
    public enum Type {
        MOBILE,
        LANDLINE,
        @Deprecated PAGER,
        LANDLINE_TOLLFREE,
        UNKNOWN;

        @Override
        @JsonValue
        public String toString() {
            return name().toLowerCase();
        }

        /**
         * Converts a string representation of the network type to an enum.
         *
         * @param type The network type as a string.
         * @return The network type as an enum, or {@link Type#UNKNOWN} if unknown.
         */
        @JsonCreator
        public static Type fromString(String type) {
            try {
                return Type.valueOf(type.toUpperCase());
            }
            catch (IllegalArgumentException ex) {
                return UNKNOWN;
            }
        }
    }
}
