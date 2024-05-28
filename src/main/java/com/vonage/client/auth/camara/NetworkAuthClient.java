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
import java.util.UUID;

/**
 * Used for obtaining access tokens for use with Vonage CAMARA APIs.
 */
public class NetworkAuthClient {
    RestEndpoint<BackendAuthRequest, BackendAuthResponse> backendAuth;
    RestEndpoint<TokenRequest, TokenResponse> tokenRequest;

    /**
     * Create a new NetworkAuthClient.
     *
     * @param wrapper Http Wrapper used to create authentication requests.
     */
    public NetworkAuthClient(HttpWrapper wrapper) {
        @SuppressWarnings("unchecked")
        final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            Endpoint(String path, R... type) {
                super(DynamicEndpoint.<T, R> builder(type)
                        .responseExceptionType(NetworkAuthResponseException.class)
                        .wrapper(wrapper).requestMethod(HttpMethod.POST)
                        .authMethod(JWTAuthMethod.class).urlFormEncodedContentType(true)
                        .pathGetter((de, req) -> de.getHttpWrapper().getHttpConfig()
                                .getApiEuBaseUri() + "/oauth2/" + path
                        )
                );
            }
        }

        backendAuth = new Endpoint<>("bc-authorize");
        tokenRequest = new Endpoint<>("token");
    }

    BackendAuthResponse sendAuthRequest(BackendAuthRequest request) {
        return backendAuth.execute(Objects.requireNonNull(request));
    }

    TokenResponse sendTokenRequest(String authRequestId) {
        return tokenRequest.execute(new TokenRequest(authRequestId));
    }

    public String getCamaraAccessToken(String msisdn, FraudPreventionDetectionScope scope) {
        return sendTokenRequest(sendAuthRequest(
                new BackendAuthRequest(msisdn, scope)).getAuthReqId()
        ).getAccessToken();
    }
}
