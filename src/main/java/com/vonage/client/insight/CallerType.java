/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.insight;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the type of the number's owner as an enum.
 * The value will be <code>BUSINESS</code> if the owner of a phone number is a business.
 * If the owner is an individual the value will be <code>CONSUMER</code>.
 * The value will be <code>UNKNOWN</code> if this information is not available.
 * This parameter is only present if cnam had a value of <code>true</code> in the request.
 */
public enum CallerType {
    BUSINESS,
    CONSUMER,
    UNKNOWN;

    @JsonCreator
    public static CallerType fromString(String name) {
        try {
            return CallerType.valueOf(name.toUpperCase());
        }
        catch (IllegalArgumentException iax) {
            return UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
