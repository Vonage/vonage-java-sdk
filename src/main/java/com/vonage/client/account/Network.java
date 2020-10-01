/*
 *   Copyright 2020 Vonage
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
public class Network {
    private Type type;
    private BigDecimal price;
    private String currency;
    private String mcc;
    private String mnc;
    private String code;
    private String name;

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

        private static final Map<String, Type> typesIndex =
                stream(Type.values()).collect(toMap(Type::toString, identity()));

        @Override
        @JsonValue
        public String toString() {
            return name().toLowerCase();
        }

        @JsonCreator
        public static Type fromString(String type) {
            return typesIndex.getOrDefault(type, Type.UNKNOWN);
        }
    }
}
