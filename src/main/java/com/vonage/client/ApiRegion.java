/*
 *   Copyright 2024 Vonage
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
package com.vonage.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the different regions that the Vonage API can be accessed from.
 *
 * @since 8.11.0
 */
public enum ApiRegion {
    /**
     * The region for the Vonage API in Europe.
     */
    API_EU,

    /**
     * The region for the Vonage API in the US.
     */
    API_US,

    /**
     * The region for the Vonage API in Asia Pacific.
     */
    API_AP;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }

    @JsonCreator
    public static ApiRegion fromString(String region) {
        if (region == null) return null;
        return valueOf(region.toUpperCase().replace("-", "_"));
    }
}
