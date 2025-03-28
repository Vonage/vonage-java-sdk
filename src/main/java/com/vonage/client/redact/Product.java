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
package com.vonage.client.redact;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the product that a transaction belongs to.
 *
 * @since 9.0.0 Moved to its own file.
 */
public enum Product {
    SMS("sms"),
    VOICE("voice"),
    NUMBER_INSIGHTS("number-insight"),
    VERIFY("verify"),
    VERIFY_SDK("verify-sdk"),
    MESSAGES("messages"),
    WORKFLOW("workflow");

    private final String value;

    Product(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
