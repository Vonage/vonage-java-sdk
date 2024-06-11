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
import com.vonage.client.common.HttpMethod;
import java.util.Objects;

/**
 * Used for obtaining access tokens for use with Vonage CAMARA APIs.
 */
public class NetworkAuthClient {
    final RestEndpoint<BackendAuthRequest, AuthResponse> backendAuth;
    final RestEndpoint<FrontendAuthRequest, AuthResponse> frontendAuth;
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
                        .wrapper(wrapper).requestMethod(method)
                        .authMethod(JWTAuthMethod.class).urlFormEncodedContentType(true)
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

    /**
     * First step in the three-legged OAuth2 flow. Initiates an OIDC request,
     * automatically determining the appropriate endpoint based on the provided parameters.
     *
     * @param request The request parameters.
     * @return The server response wrapped in an object.
     * @throws IllegalArgumentException If the request is an unhandled type.
     */
    AuthResponse sendAuthRequest(AuthRequest request) {
        if (request instanceof BackendAuthRequest) {
            return backendAuth.execute((BackendAuthRequest) request);
        }
        else if (request instanceof FrontendAuthRequest) {
            return frontendAuth.execute((FrontendAuthRequest) request);
        }
        else {
            throw new IllegalArgumentException("Unknown auth request type: "+request);
        }
    }

    /**
     * Second step in the three-legged OAuth2 flow. Obtains an access token from the given request ID.
     *
     * @param authRequestId The result from {@link AuthResponse#getAuthReqId()}
     * @return The API response, which contains the access token.
     */
    TokenResponse sendTokenRequest(String authRequestId) {
        return tokenRequest.execute(new TokenRequest(authRequestId));
    }

    /**
     * Obtains a new access token.
     *
     * @param authRequest The request parameters.
     * @return The access token as a string.
     * @throws NetworkAuthResponseException If an error was encountered during the workflow.
     * @since 8.9.0
     */
    public String getCamaraAccessToken(AuthRequest authRequest) {
        AuthResponse ar = sendAuthRequest(authRequest);
        TokenResponse tr = sendTokenRequest(ar.getAuthReqId());
        return tr.getAccessToken();
    }
}
