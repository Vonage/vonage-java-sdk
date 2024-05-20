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

import org.apache.commons.codec.binary.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This AuthMethod uses API key and secret either as URL parameters, or as
 * {@code Basic} in the header.
 */
public class TokenAuthMethod extends BasicAuthMethod implements QueryParamsAuthMethod {
    private static final int SORT_KEY = 30;

    private final String apiKey, apiSecret;

    public TokenAuthMethod(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public Map<String, String> asQueryParams() {
        Map<String, String> params = new LinkedHashMap<>(4);
        params.put("api_key", apiKey);
        params.put("api_secret", apiSecret);
        return params;
    }

    @Override
    protected String getBasicToken() {
        return Base64.encodeBase64String((apiKey + ":" + apiSecret).getBytes());
    }

    @Override
    public int getSortKey() {
        return SORT_KEY;
    }
}
