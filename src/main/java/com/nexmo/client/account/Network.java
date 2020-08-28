/*
 * Copyright (c) 2011-2018 Vonage Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Network {
    private Type type;
    private BigDecimal price;
    private String currency;
    private String mcc;
    private String mnc;
    private String code;
    private String name;

    public Type getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMcc() {
        return mcc;
    }

    public String getMnc() {
        return mnc;
    }

    @JsonProperty("networkCode")
    public String getCode() {
        return code;
    }

    @JsonProperty("networkName")
    public String getName() {
        return name;
    }

    enum Type {
        MOBILE, LANDLINE, PAGER, LANDLINE_TOLLFREE, UNKNOWN;

        private static final Map<String, Type> typesIndex = new HashMap<>();

        static {
            for (Type type : Type.values()) {
                typesIndex.put(type.toString(), type);
            }
        }

        @Override
        @JsonValue
        public String toString() {
            return name().toLowerCase();
        }

        @JsonCreator
        public static Type fromString(String type) {
            Type foundType = typesIndex.get(type);
            return (foundType != null) ? foundType : Type.UNKNOWN;
        }
    }
}
