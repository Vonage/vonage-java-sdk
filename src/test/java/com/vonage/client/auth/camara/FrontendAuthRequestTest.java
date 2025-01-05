/*
 *   Copyright 2025 Vonage
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

import static com.vonage.client.TestUtils.APPLICATION_ID;
import static com.vonage.client.TestUtils.APPLICATION_ID_STR;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.util.Map;

public class FrontendAuthRequestTest {
    final URI redirectUrl = URI.create("http://example.org/redirect");
    final String msisdn = "447700900003", state = "MyApp_state123";
    final FrontendAuthRequest allParamsRequest = new FrontendAuthRequest(msisdn, redirectUrl, APPLICATION_ID, state);

    @Test
    public void testValidation() {
        assertThrows(NullPointerException.class, () -> new FrontendAuthRequest(null, redirectUrl, APPLICATION_ID, state));
        assertThrows(NullPointerException.class, () -> new FrontendAuthRequest(msisdn, null, APPLICATION_ID, state));
        assertThrows(NullPointerException.class, () -> new FrontendAuthRequest(msisdn, redirectUrl, null, state));
        assertNull(new FrontendAuthRequest(msisdn, redirectUrl, APPLICATION_ID, null).makeParams().get("state"));
    }

    @Test
    public void testMakeParams() {
        var expectedParams = Map.of(
                "client_id", APPLICATION_ID_STR,
                "login_hint", "tel:+" + msisdn,
                "scope", "openid dpv:FraudPreventionAndDetection#number-verification-verify-read",
                "redirect_uri", redirectUrl.toString(),
                "state", state,
                "response_type", "code"
        );
        assertEquals(expectedParams, allParamsRequest.makeParams());
    }

    @Test
    public void testBuildUrl() {
        assertEquals(URI.create("https://oidc.idp.vonage.com/oauth2/auth?" +
                        "login_hint=tel%3A%2B"+msisdn+"&scope=openid+dpv%3AFraudPreventionAndDetection" +
                        "%23number-verification-verify-read&client_id="+APPLICATION_ID_STR +
                        "&redirect_uri=http%3A%2F%2Fexample.org%2Fredirect&response_type=code&state=" + state
                ),
                allParamsRequest.buildOidcUrl()
        );
    }
}
