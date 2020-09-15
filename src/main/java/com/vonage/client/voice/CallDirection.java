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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum CallDirection {
    OUTBOUND,
    INBOUND,
    UNKNOWN;

    private static final Map<String, CallDirection> CALL_DIRECTION_INDEX = new HashMap<>();

    static {
        for (CallDirection callDirection : CallDirection.values()) {
            CALL_DIRECTION_INDEX.put(callDirection.name(), callDirection);
        }
    }

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static CallDirection fromString(String name) {
        CallDirection foundCallDirection = CALL_DIRECTION_INDEX.get(name.toUpperCase());
        return (foundCallDirection != null) ? foundCallDirection : UNKNOWN;
    }
}