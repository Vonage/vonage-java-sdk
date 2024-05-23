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
import com.vonage.client.RestEndpoint;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import static com.vonage.client.auth.camara.FraudPreventionDetectionScope.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;
import java.util.Map;
import java.util.UUID;

public class NetworkAuthClientTest extends AbstractClientTest<NetworkAuthClient> {
    final UUID authReqId = UUID.randomUUID();

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

    @Test
    public void testGetCamaraAccessToken() throws Exception {
        var accessToken = "youMayProceed";
        stubResponse(200,
                "{\"auth_req_id\": \""+authReqId+"\"}",
                "{\"access_token\": \""+accessToken+"\"}"
        );

        assertEquals(accessToken, client.getCamaraAccessToken("+447900000001", RETRIEVE_SIM_SWAP_DATE));
    }

    @Test
    public void testBackendAuth() throws Exception {
        final int expiresIn = 120, interval = 3;
        String responseJson = STR."""
            {
               "auth_req_id": "\{authReqId}",
               "expires_in": "\{expiresIn}",
               "interval": "\{interval}"
            }
        """, msisdn = "+49 151 1234567";
        var scope = FraudPreventionDetectionScope.RETRIEVE_SIM_SWAP_DATE;
        var request = new BackendAuthRequest(msisdn, scope);

        stubResponse(200, responseJson);
        var parsed = client.sendAuthRequest(request);

        testJsonableBaseObject(parsed);
        assertEquals(authReqId, parsed.getAuthReqId());
        assertEquals(expiresIn, parsed.getExpiresIn());
        assertEquals(interval, parsed.getInterval());

        assertThrows(NullPointerException.class, () -> new BackendAuthRequest(msisdn, null));
        assertThrows(NullPointerException.class, () -> new BackendAuthRequest(null, scope));
        assertThrows(IllegalArgumentException.class, () -> new BackendAuthRequest("foo", scope));

        stubResponseAndAssertThrows(200, responseJson,
                () -> client.sendAuthRequest(null), NullPointerException.class
        );

        assert403ResponseException(() -> client.sendAuthRequest(request));
    }

    @Test
    public void testTokenRequest() throws Exception {
        String access = "accessTokStr1", refresh = "F5", type = "magical";
        var responseJson = STR."""
            {
               "access_token": "\{access}",
               "token_type": "\{type}",
               "refresh_token": "\{refresh}"
            }
        """;

        stubResponse(200, responseJson);
        var parsed = client.sendTokenRequest(authReqId);
        testJsonableBaseObject(parsed);
        assertEquals(access, parsed.getAccessToken());
        assertEquals(refresh, parsed.getRefreshToken());
        assertEquals(type, parsed.getTokenType());

        stubResponseAndAssertThrows(200, responseJson,
                () -> client.sendTokenRequest(null), NullPointerException.class
        );

        assert403ResponseException(() -> client.sendTokenRequest(authReqId));
    }

    @Test
    public void testBackendAuthEndpoint() throws Exception {
        new NetworkAuthEndpointTestSpec<BackendAuthRequest, BackendAuthResponse>() {
            final String msisdn = "447700900000";

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
                        "scope", "dpv:FraudPreventionAndDetection#check-sim-swap"
                );
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
                        "auth_req_id", "arid/" + authReqId
                );
            }
        }
        .runTests();
    }
}
