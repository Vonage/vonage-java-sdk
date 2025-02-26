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
package com.vonage.client.voice;

/**
 * Represents the order in which calls are returned in {@link VoiceClient#listCalls(CallsFilter)}.
 *
 * @deprecated Replaced by {@link com.vonage.client.common.SortOrder}.
 */
@Deprecated
public enum CallOrder {
    ASCENDING("asc"), DESCENDING("desc");

    private final String callOrder;

    CallOrder(String callOrder) {
        this.callOrder = callOrder;
    }

    /**
     * Converts this enum into its string value.
     *
     * @return The call order as a string.
     */
    public String getCallOrder() {
        return callOrder;
    }
}
