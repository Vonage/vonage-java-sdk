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

import com.vonage.client.AbstractClientTest;
import com.vonage.client.DynamicEndpoint;
import com.vonage.client.OrderedMap;
import static com.vonage.client.OrderedMap.entry;
import com.vonage.client.RestEndpoint;
import static com.vonage.client.TestUtils.TEST_BASE_URI;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import static com.vonage.client.auth.camara.FraudPreventionDetectionScope.CHECK_SIM_SWAP;
import static com.vonage.client.auth.camara.FraudPreventionDetectionScope.RETRIEVE_SIM_SWAP_DATE;
import com.vonage.client.common.HttpMethod;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

public class NetworkAuthClientTest extends AbstractClientTest<NetworkAuthClient> {
    final URI redirectUrl = URI.create("http://example.org/redirect");
    final String authReqId = "arid/"+UUID.randomUUID(),
            msisdn = "447700900001", state = "MyApp_state123";

    public NetworkAuthClientTest() {
        client = new NetworkAuthClient(wrapper);
    }

    void assert403ResponseException(Executable invocation) throws Exception {
        final int status = 403;
        String message = "",
                code = "PERMISSION_DENIED", responseJson = STR."""
            {
               "status": \{status},
               "code": "\{code}",
               "message": "Client does not have sufficient permissions to perform this action"
            }
        """;

        var parsed = assertApiResponseException(status, responseJson, NetworkAuthResponseException.class, invocation);
        assertEquals(code, parsed.getCode());
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testNetworkAuthMethod() throws Exception {
        var fbam = new NetworkAuthMethod(client, new BackendAuthRequest("+34 91 12345678", RETRIEVE_SIM_SWAP_DATE));
        wrapper.getAuthCollection().add(fbam);
        var endpoint = DynamicEndpoint.<Void, String> builder(String.class)
                .wrapper(wrapper).requestMethod(HttpMethod.POST)
                .authMethod(NetworkAuthMethod.class)
                .pathGetter((_, _) -> TEST_BASE_URI).build();

        var expectedResponse = "Hello, GNP!";
        stubNetworkResponse(expectedResponse);

        assertEquals(expectedResponse, endpoint.execute(null));
    }

    @Test
    public void testSendAuthRequest() throws Exception {
        final int expiresIn = 120, interval = 3;
        final String state = "Unique app id";
        String responseJson = STR."""
            {
               "auth_req_id": "\{authReqId}",
               "expires_in": "\{expiresIn}",
               "interval": "\{interval}"
            }
        """, msisdn = "+49 151 1234567";

        var backendScope = FraudPreventionDetectionScope.RETRIEVE_SIM_SWAP_DATE;
        var backend = new BackendAuthRequest(msisdn, backendScope);
        var frontend = new FrontendAuthRequest(msisdn, redirectUrl, state);

        for (var request : new AuthRequest[]{backend, frontend}) {
            stubResponse(200, responseJson);
            var parsed = client.sendAuthRequest(request);
            testJsonableBaseObject(parsed);
            assertEquals(authReqId, parsed.getAuthReqId());
            assertEquals(expiresIn, parsed.getExpiresIn());
            assertEquals(interval, parsed.getInterval());
        }

        assertThrows(NullPointerException.class, () -> new BackendAuthRequest(msisdn, null));
        assertThrows(NullPointerException.class, () -> new BackendAuthRequest(null, backendScope));
        assertThrows(IllegalArgumentException.class, () -> new BackendAuthRequest("foo", backendScope));

        assertThrows(NullPointerException.class, () -> new FrontendAuthRequest(null, redirectUrl, state));
        assertThrows(NullPointerException.class, () -> new FrontendAuthRequest(msisdn, null, state));
        assertNull(new FrontendAuthRequest(msisdn, redirectUrl, null).makeParams().get("state"));

        stubResponseAndAssertThrows(200, responseJson,
                () -> client.sendAuthRequest(null), IllegalArgumentException.class
        );

        assert403ResponseException(() -> client.sendAuthRequest(backend));
    }

    @Test
    public void testTokenRequest() throws Exception {
        Integer expires = 29;
        String access = "accessTokStr1", refresh = "F5", type = "bearer";
        var responseJson = STR."""
            {
               "access_token": "\{access}",
               "token_type": "\{type}",
               "refresh_token": "\{refresh}",
               "expires_in": \{expires}
            }
        """;

        stubResponse(200, responseJson);
        var parsed = client.sendTokenRequest(authReqId);
        testJsonableBaseObject(parsed);
        assertEquals(access, parsed.getAccessToken());
        assertEquals(refresh, parsed.getRefreshToken());
        assertEquals(type, parsed.getTokenType());
        assertEquals(expires, parsed.getExpiresIn());

        stubResponseAndAssertThrows(200, responseJson,
                () -> client.sendTokenRequest(null), NullPointerException.class
        );

        assert403ResponseException(() -> client.sendTokenRequest(authReqId));
    }

    @Test
    public void testBackendAuthEndpoint() throws Exception {
        new NetworkAuthEndpointTestSpec<BackendAuthRequest, AuthResponse>() {

            @Override
            protected RestEndpoint<BackendAuthRequest, AuthResponse> endpoint() {
                return client.backendAuth;
            }

            @Override
            protected String expectedEndpointUri(BackendAuthRequest request) {
                return "/oauth2/bc-authorize";
            }

            @Override
            protected BackendAuthRequest sampleRequest() {
                return new BackendAuthRequest(msisdn, CHECK_SIM_SWAP);
            }

            @Override
            protected Map<String, ?> sampleQueryParams() {
                return new OrderedMap(
                        entry("login_hint", "tel:+" + msisdn),
                        entry("scope", "dpv:FraudPreventionAndDetection#check-sim-swap")
                );
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }
        }
        .runTests();
    }

    @Test
    public void testFrontendAuthEndpoint() throws Exception {
        new NetworkAuthEndpointTestSpec<FrontendAuthRequest, AuthResponse>() {

            @Override
            protected RestEndpoint<FrontendAuthRequest, AuthResponse> endpoint() {
                return client.frontendAuth;
            }

            @Override
            protected String customBaseUri() {
                // This is hardcoded
                return expectedDefaultBaseUri();
            }

            @Override
            protected String expectedEndpointUri(FrontendAuthRequest request) {
                return "/oauth2/auth";
            }

            @Override
            protected FrontendAuthRequest sampleRequest() {
                return new FrontendAuthRequest(msisdn, redirectUrl, state);
            }

            @Override
            protected Map<String, ?> sampleQueryParams() {
                return new OrderedMap(
                        entry("login_hint", "tel:+" + msisdn),
                        entry("scope", "dpv:FraudPreventionAndDetection#number-verification-verify-read"),
                        entry("redirect_uri", redirectUrl.toString()),
                        entry("state", state),
                        entry("response_type", "code")
                );
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }
        }
        .runTests();
    }

    @Test
    public void testTokenRequestEndpoint() throws Exception {
        new NetworkAuthEndpointTestSpec<TokenRequest, TokenResponse>() {

            @Override
            protected RestEndpoint<TokenRequest, TokenResponse> endpoint() {
                return client.tokenRequest;
            }

            @Override
            protected String expectedEndpointUri(TokenRequest request) {
                return "/oauth2/token";
            }

            @Override
            protected TokenRequest sampleRequest() {
                return new TokenRequest(authReqId);
            }

            @Override
            protected Map<String, ?> sampleQueryParams() {
                return new OrderedMap(
                        entry("grant_type", "urn:openid:params:grant-type:ciba"),
                        entry("auth_req_id", authReqId)
                );
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }
        }
        .runTests();
    }
}
