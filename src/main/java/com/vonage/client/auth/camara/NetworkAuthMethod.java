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

import com.vonage.client.auth.BearerAuthMethod;
import com.vonage.client.common.E164;

/**
 * Auth method for Vonage Network APIs. Designed to be replaced on each request.
 */
public final class NetworkAuthMethod extends BearerAuthMethod {
    private final NetworkAuthClient networkAuthClient;
    private TokenRequest tokenRequest;
    private BackendAuthRequest backendParams;

    /**
     * Creates a new Bearer auth method which uses the specified params to exchange for an access token.
     *
     * @param client The network auth client to use.
     * @param request The token request parameters.
     */
    public NetworkAuthMethod(NetworkAuthClient client, TokenRequest request) {
        this.networkAuthClient = client;
        this.tokenRequest = request;
    }

    /**
     * Creates a new Bearer auth method which uses the specified params to
     * automatically obtain the token parameters in order to exchange them for an access token.
     *
     * @param client The network auth client to use.
     * @param request The initial Back-End request parameters to use for obtaining the tokens.
     */
    public NetworkAuthMethod(NetworkAuthClient client, BackendAuthRequest request) {
        this.networkAuthClient = client;
        this.backendParams = request;
    }

    @Override
    protected String getBearerToken() {
        if (backendParams != null) {
            tokenRequest = new TokenRequest(networkAuthClient.makeOpenIDConnectRequest(backendParams));
        }
        return networkAuthClient.getCamaraAccessToken(tokenRequest);
    }

    @Override
    public int getSortKey() {
        return 5;
    }
}
