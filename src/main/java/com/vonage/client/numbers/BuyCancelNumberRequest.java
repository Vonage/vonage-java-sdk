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
package com.vonage.client.numbers;

import java.util.Map;

/**
 * Base class for {@linkplain NumbersClient#buyNumber(String, String, String)} and
 * {@linkplain NumbersClient#cancelNumber(String, String, String)} method request parameters.
 *
 * @since 8.10.0
 */
class BuyCancelNumberRequest extends BaseNumberRequest {
    private final String targetApiKey;

    BuyCancelNumberRequest(String country, String msisdn, String targetApiKey) {
        super(country, msisdn);
        this.targetApiKey = targetApiKey;
    }

    /**
     * The subaccount API key to perform this action on.
     *
     * @return The account API key for this request, or {@code null} if unspecified / the main account.
     * @since 8.10.0
     */
    public String getTargetApiKey() {
        return targetApiKey;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        if (targetApiKey != null) {
            params.put("target_api_key", targetApiKey);
        }
        return params;
    }
}
