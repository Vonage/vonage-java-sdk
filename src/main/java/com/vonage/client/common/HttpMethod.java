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
package com.vonage.client.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.vonage.client.Jsonable;

/**
 * Enumeration representing various HTTP request methods used in Vonage APIs.
 */
public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH;

    /**
     * Convert a string to an HttpMethod enum.
     *
     * @param name The string to convert.
     *
     * @return The HTTP method as an enum, or {@code null} if the name is null.
     */
    @JsonCreator
    public static HttpMethod fromString(String name) {
        return Jsonable.fromString(name, HttpMethod.class);
    }
}
