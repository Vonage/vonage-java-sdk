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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;

/**
 * Response data for the {@link AccountClient#getBalance()} method.
 */
public class BalanceResponse extends JsonableBaseObject {
    private double value;
    private boolean autoReload;

    protected BalanceResponse() {}

    /**
     * Account balance in EUR as a double.
     *
     * @return The balance in Euros.
     */
    @JsonProperty("value")
    public double getValue() {
        return value;
    }

    /**
     * Whether balance auto-reloading is enabled.
     *
     * @return {@code true} if auto-reloading is enabled, {@code false} otherwise.
     */
    @JsonProperty("autoReload")
    public boolean isAutoReload() {
        return autoReload;
    }

    @Deprecated
    public static BalanceResponse fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
