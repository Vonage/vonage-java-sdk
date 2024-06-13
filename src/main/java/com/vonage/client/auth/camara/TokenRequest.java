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
package com.vonage.client.auth.camara;

import com.vonage.client.QueryParamsRequest;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the request parameters for the intermediate (second) step in an OAuth2 three-legged workflow.
 */
public final class TokenRequest implements QueryParamsRequest {
    private final Map<String, String> params = new LinkedHashMap<>(4);

    /**
     * Creates a new Back-End token request.
     *
     * @param authReqId The auth request ID, as obtained from {@link BackendAuthResponse#getAuthReqId()}.
     */
    public TokenRequest(String authReqId) {
        params.put("grant_type", "urn:openid:params:grant-type:ciba");
        params.put("auth_req_id", Objects.requireNonNull(authReqId, "Auth request ID is required."));
    }

    /**
     * Creates a new Front-End token request.
     *
     * @param redirectUrl The Redirect URI set in the Vonage Application.
     * @param code The authentication code that is used to exchange for an access token.
     */
    public TokenRequest(URI redirectUrl, String code) {
        params.put("grant_type", "authorization_code");
        params.put("code", Objects.requireNonNull(code, "Code is required."));
        params.put("redirect_uri", Objects.requireNonNull(redirectUrl, "Redirect URI is required.").toString());
    }

    @Override
    public Map<String, String> makeParams() {
        return params;
    }
}
