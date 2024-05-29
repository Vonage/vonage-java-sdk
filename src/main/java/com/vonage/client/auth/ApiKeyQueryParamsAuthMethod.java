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

import java.util.Map;

/**
 * API key and secret in query parameters.
 *
 * @since 8.8.0
 */
public class ApiKeyQueryParamsAuthMethod extends AbstractApiKeyQueryParamsAuthMethod {
    private static final int SORT_KEY = 30;

    private final String apiSecret;

    public ApiKeyQueryParamsAuthMethod(String apiKey, String apiSecret) {
        super(apiKey);
        this.apiSecret = apiSecret;
    }

    /**
     * Converts this to a {@linkplain BasicAuthMethod}.
     *
     * @return A new {@linkplain ApiKeyHeaderAuthMethod} with this object's API key and secret.
     */
    public BasicAuthMethod asBasicHeader() {
        return new ApiKeyHeaderAuthMethod(apiKey, apiSecret);
    }

    @Override
    public Map<String, String> getAuthParams(RequestQueryParams requestParams) {
        Map<String, String> params = super.getAuthParams(requestParams);
        params.put("api_secret", apiSecret);
        return params;
    }

    @Override
    public int getSortKey() {
        return SORT_KEY;
    }
}
