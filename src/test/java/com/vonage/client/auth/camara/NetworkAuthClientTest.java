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
import com.vonage.client.RestEndpoint;
import com.vonage.client.TestUtils;
import static com.vonage.client.TestUtils.*;
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
        String message = "Client does not have sufficient permissions to perform this action",
                code = "PERMISSION_DENIED",
                responseJson = "{\"status\": "+status+", \"code\": \""+code+"\",\"message\": \""+message+"\"}";

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
                .pathGetter((de, req) -> TEST_BASE_URI).build();

        var expectedResponse = "Hello, GNP!";
        stubBackendNetworkResponse(expectedResponse);

        assertEquals(expectedResponse, endpoint.execute(null));
    }

    @Test
    public void testMakeOIDCBackendRequest() throws Exception {
        final int expiresIn = 120, interval = 3;
        String msisdn = "+49 151 1234567", responseJson = "{\"auth_req_id\": \"" + authReqId +
                "\", \"expires_in\": \""+expiresIn+"\", \"interval\": \""+interval+"\"}";

        var scope = FraudPreventionDetectionScope.RETRIEVE_SIM_SWAP_DATE;
        var request = new BackendAuthRequest(msisdn, scope);

        stubResponse(200, responseJson);
        var parsed = client.makeOpenIDConnectRequest(request);
        testJsonableBaseObject(parsed);
        assertEquals(authReqId, parsed.getAuthReqId());
        assertEquals(expiresIn, parsed.getExpiresIn());
        assertEquals(interval, parsed.getInterval());

        assertThrows(NullPointerException.class, () -> new BackendAuthRequest(msisdn, null));
        assertThrows(NullPointerException.class, () -> new BackendAuthRequest(null, scope));
        assertThrows(IllegalArgumentException.class, () -> new BackendAuthRequest("foo", scope));

        stubResponseAndAssertThrows(200, responseJson,
                () -> client.makeOpenIDConnectRequest((BackendAuthRequest) null),
                NullPointerException.class
        );

        assert403ResponseException(() -> client.makeOpenIDConnectRequest(request));
    }

    @Test
    public void testMakeOIDCFrontendRequest() throws Exception {
        var request = new FrontendAuthRequest(msisdn, redirectUrl, APPLICATION_ID, state);

        stubResponse(302);
        client.makeOpenIDConnectRequest(request);

        assertThrows(NullPointerException.class, () -> new FrontendAuthRequest(null, redirectUrl, APPLICATION_ID, state));
        assertThrows(NullPointerException.class, () -> new FrontendAuthRequest(msisdn, null, APPLICATION_ID, state));
        assertThrows(NullPointerException.class, () -> new FrontendAuthRequest(msisdn, redirectUrl, null, state));
        assertNull(new FrontendAuthRequest(msisdn, redirectUrl, APPLICATION_ID, null).makeParams().get("state"));

        stubResponseAndAssertThrows(302,
                () -> client.makeOpenIDConnectRequest((FrontendAuthRequest) null),
                NullPointerException.class
        );

        assert403ResponseException(() -> client.makeOpenIDConnectRequest(request));
    }

    @Test
    public void testCamaraToken() throws Exception {
        Integer expires = 29;
        String access = "accessTokStr1", refresh = "F5", type = "bearer";
        var responseJson = "{\n"+
               "\"access_token\": \""+access+"\",\n"+
               "\"token_type\": \""+type+"\",\n" +
               "\"refresh_token\": \""+refresh+"\",\n" +
               "\"expires_in\": "+expires+"\n}";

        var request = new TokenRequest(authReqId);

        stubResponse(200, responseJson);
        var parsed = client.getCamaraToken(request);
        testJsonableBaseObject(parsed);
        assertEquals(access, parsed.getAccessToken());
        assertEquals(refresh, parsed.getRefreshToken());
        assertEquals(type, parsed.getTokenType());
        assertEquals(expires, parsed.getExpiresIn());

        stubResponseAndAssertThrows(200, responseJson,
                () -> client.getCamaraToken(null), NullPointerException.class
        );

        assert403ResponseException(() -> client.getCamaraToken(request));
    }

    @Test
    public void testBackendAuthEndpoint() throws Exception {
        new NetworkAuthEndpointTestSpec<BackendAuthRequest, BackendAuthResponse>() {

            @Override
            protected RestEndpoint<BackendAuthRequest, BackendAuthResponse> endpoint() {
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
            protected Map<String, String> sampleQueryParams() {
                return Map.of(
                        "login_hint", "tel:+" + msisdn,
                        "scope", "openid dpv:FraudPreventionAndDetection#check-sim-swap"
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
        new NetworkAuthEndpointTestSpec<FrontendAuthRequest, URI>() {

            @Override
            protected RestEndpoint<FrontendAuthRequest, URI> endpoint() {
                return client.frontendAuth;
            }

            @Override
            protected String expectedEndpointUri(FrontendAuthRequest request) {
                return "/oauth2/auth";
            }

            @Override
            protected FrontendAuthRequest sampleRequest() {
                return new FrontendAuthRequest(msisdn, redirectUrl, APPLICATION_ID, state);
            }

            @Override
            protected Map<String, ?> sampleQueryParams() {
                return Map.of(
                        "client_id", TestUtils.APPLICATION_ID_STR,
                        "login_hint", "tel:+" + msisdn,
                        "scope", "openid dpv:FraudPreventionAndDetection#number-verification-verify-read",
                        "redirect_uri", redirectUrl.toString(),
                        "state", state,
                        "response_type", "code"
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
            protected Map<String, String> sampleQueryParams() {
                return Map.of(
                        "grant_type", "urn:openid:params:grant-type:ciba",
                        "auth_req_id", authReqId
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
