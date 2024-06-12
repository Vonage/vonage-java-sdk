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

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.auth.NoAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.Objects;

/**
 * Used for obtaining access tokens for use with Vonage CAMARA APIs.
 */
public class NetworkAuthClient {
    final RestEndpoint<BackendAuthRequest, BackendAuthResponse> backendAuth;
    final RestEndpoint<FrontendAuthRequest, Void> frontendAuth;
    final RestEndpoint<TokenRequest, TokenResponse> tokenRequest;

    /**
     * Create a new NetworkAuthClient.
     *
     * @param wrapper Http Wrapper used to create authentication requests.
     */
    public NetworkAuthClient(HttpWrapper wrapper) {
        @SuppressWarnings("unchecked")
        final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            Endpoint(String path, HttpMethod method, R... type) {
                super(DynamicEndpoint.<T, R> builder(type)
                        .responseExceptionType(NetworkAuthResponseException.class)
                        .wrapper(wrapper).requestMethod(method).urlFormEncodedContentType(true)
                        .authMethod(method == HttpMethod.POST ? JWTAuthMethod.class : NoAuthMethod.class)
                        .pathGetter((de, req) -> (method == HttpMethod.POST ?
                                de.getHttpWrapper().getHttpConfig().getApiEuBaseUri() :
                                "https://oidc.idp.vonage.com")
                                + "/oauth2/" + path
                        )
                );
            }
        }

        frontendAuth = new Endpoint<>("auth", HttpMethod.GET);
        backendAuth = new Endpoint<>("bc-authorize", HttpMethod.POST);
        tokenRequest = new Endpoint<>("token", HttpMethod.POST);
    }

    private <R> R validateRequest(R request) {
        return Objects.requireNonNull(request, "Request is required.");
    }

    public void makeOpenIDConnectRequest(FrontendAuthRequest request) {
        frontendAuth.execute(validateRequest(request));
    }

    public BackendAuthResponse makeOpenIDConnectRequest(BackendAuthRequest request) {
        return backendAuth.execute(validateRequest(request));
    }

    /**
     * Obtains a new access token for a Back-End auth request.
     *
     * @param request The token request parameters.
     * @return The access token as a string.
     * @throws NetworkAuthResponseException If an error was encountered during the workflow.
     * @since 8.9.0
     */
    public String getCamaraAccessToken(TokenRequest request) {
        TokenResponse tr = tokenRequest.execute(validateRequest(request));
        return tr.getAccessToken();
    }
}
