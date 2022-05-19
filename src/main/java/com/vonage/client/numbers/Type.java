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
package com.vonage.client.numbers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enumeration representing the type of number.
 */
public enum Type {
    LANDLINE("landline"),
    MOBILE_LVN("mobile-lvn"),
    LANDLINE_TOLL_FREE("landline-toll-free"),
    UNKNOWN("unknown");

    private static final Map<String, Type> TYPE_INDEX =
        Arrays.stream(Type.values()).collect(Collectors.toMap(
                Type::getType, Function.identity()
        ));

    private final String type;

    Type(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public static Type fromString(String type) {
        return TYPE_INDEX.getOrDefault(type, UNKNOWN);
    }
}
