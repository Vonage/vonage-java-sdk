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

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration representing the type of number.
 */
public enum Type {
    LANDLINE("landline"),
    MOBILE_LVN("mobile-lvn"),
    LANDLINE_TOLL_FREE("landline-toll-free"),
    UNKNOWN("unknown");

    private static final Map<String, Type> TYPE_INDEX = new HashMap<>();

    static {
        for (Type type : Type.values()) {
            TYPE_INDEX.put(type.type, type);
        }
    }

    private String type;

    Type(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public static Type fromString(String type) {
        Type foundType = TYPE_INDEX.get(type);
        return (foundType != null) ? foundType : UNKNOWN;
    }
}
