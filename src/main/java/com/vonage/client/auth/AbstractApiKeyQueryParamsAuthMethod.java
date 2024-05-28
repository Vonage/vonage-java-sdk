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

import com.vonage.client.auth.hashutils.AbstractAuthMethod;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Intermediate class for auth methods that use query params and have an API key.
 */
abstract class AbstractApiKeyQueryParamsAuthMethod extends AbstractAuthMethod implements QueryParamsAuthMethod, ApiKeyAuthMethod {
    final String apiKey;

    AbstractApiKeyQueryParamsAuthMethod(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public Map<String, String> getAuthParams(RequestQueryParams requestParams) {
        Map<String, String> params = new LinkedHashMap<>(4);
        params.put("api_key", apiKey);
        return params;
    }
}
