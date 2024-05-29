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
package com.vonage.client.auth;

import java.util.Base64;

/**
 * API key and secret in the header.
 *
 * @since 8.8.0
 */
public class ApiKeyHeaderAuthMethod extends BasicAuthMethod implements ApiKeyAuthMethod {
    private static final int SORT_KEY = 40;

    private final String apiKey, apiSecret;

    public ApiKeyHeaderAuthMethod(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Converts this to a {@linkplain QueryParamsAuthMethod}.
     *
     * @return A new {@linkplain ApiKeyQueryParamsAuthMethod} with this object's API key and secret.
     */
    public QueryParamsAuthMethod asQueryParams() {
        return new ApiKeyQueryParamsAuthMethod(apiKey, apiSecret);
    }

    @Override
    protected String getBasicToken() {
        return Base64.getEncoder().encodeToString((apiKey + ":" + apiSecret).getBytes());
    }

    @Override
    public int getSortKey() {
        return SORT_KEY;
    }
}
