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
package com.vonage.client.camara.numberverification;

import com.vonage.client.*;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.camara.*;
import com.vonage.client.camara.CamaraResponseException;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.util.Set;

public class NumberVerificationClientTest extends AbstractClientTest<NumberVerificationClient> {
    final URI redirectUrl = URI.create("https://domain.example.org/redirect");
    final String msisdn = "346 661113334", code = "987123",
            trueResponse = "{\"devicePhoneNumberVerified\": true}";

    public NumberVerificationClientTest() {
        client = new NumberVerificationClient(wrapper);
    }

    void setUpCachedVerifyNumber() throws Exception {
        stubResponse(302);
        URI uri = client.initiateVerification(msisdn, redirectUrl, null);
        assertEquals(uri, new FrontendAuthRequest(
                msisdn, redirectUrl, TestUtils.APPLICATION_ID, null
            ).buildOidcUrl()
        );
        wrapper.getAuthCollection().add(new NetworkAuthMethod(
                new NetworkAuthClient(wrapper), new TokenRequest(redirectUrl, code)
        ));
    }

    void assert403CamaraResponseException(Executable invocation) throws Exception {
        final int status = 403;
        String code = "PERMISSION_DENIED", responseJson = "{\"status\": " +
                status+", \"code\": \""+code+"\",\"message\":\""+TestUtils.TEST_REASON+"\"}";

        var fromJsonEx = CamaraResponseException.fromJson(responseJson);

        stubFrontendNetworkResponse(status, responseJson);

        String failMsg = "Expected "+ CamaraResponseException.class.getSimpleName();

        try {
            invocation.execute();
            fail(failMsg);
        }
        catch (CamaraResponseException ex) {
            assertEquals(status, ex.getStatusCode());
            assertEquals(ex.getCode(), fromJsonEx.getCode());
        }
        catch (Throwable t) {
            fail(failMsg, t);
        }
    }

    @Test
    public void testVerifyNumberCached() throws Exception {
        stubFrontendNetworkResponse(trueResponse);
        assertThrows(IllegalStateException.class, () -> client.verifyNumber(code));

        setUpCachedVerifyNumber();
        stubFrontendNetworkResponse(trueResponse);
        assertTrue(client.verifyNumber(code));

        stubFrontendNetworkResponse(trueResponse);
        assertThrows(IllegalStateException.class, () -> client.verifyNumber(code));

        setUpCachedVerifyNumber();
        stubFrontendNetworkResponse("{\"devicePhoneNumberVerified\":false}");
        assertFalse(client.verifyNumber(code));

        setUpCachedVerifyNumber();
        stubFrontendNetworkResponse("{}");
        assertFalse(client.verifyNumber(code));

        setUpCachedVerifyNumber();
        stubFrontendNetworkResponse("{\"devicePhoneNumberVerified\":\"true\"}");
        assertTrue(client.verifyNumber(code));

        setUpCachedVerifyNumber();
        stubFrontendNetworkResponse("{\"devicePhoneNumberVerified\": \"false\"}");
        assertFalse(client.verifyNumber(code));

        setUpCachedVerifyNumber();
        stubFrontendNetworkResponse(trueResponse);
        assertThrows(NullPointerException.class, () -> client.verifyNumber(null));

        setUpCachedVerifyNumber();
        assert403CamaraResponseException(() -> client.verifyNumber(code));
    }

    @Test
    public void testVerifyNumberRaw() throws Exception {
        stubFrontendNetworkResponse(trueResponse);
        assertTrue(client.verifyNumber(msisdn, redirectUrl, code));

        stubFrontendNetworkResponse(trueResponse);
        assertThrows(NullPointerException.class, () -> client.verifyNumber(msisdn, redirectUrl, null));

        stubFrontendNetworkResponse(trueResponse);
        assertThrows(NullPointerException.class, () -> client.verifyNumber(msisdn, null, code));

        stubFrontendNetworkResponse(trueResponse);
        assertThrows(NullPointerException.class, () -> client.verifyNumber(null, redirectUrl, code));

        assert403CamaraResponseException(() -> client.verifyNumber(msisdn, redirectUrl, code));
    }

    @Test
    public void testVerifyNumberCacheClearedAfterRawCall() throws Exception {
        setUpCachedVerifyNumber();
        stubFrontendNetworkResponse(trueResponse);
        assertTrue(client.verifyNumber(msisdn, redirectUrl, code));

        stubFrontendNetworkResponse(trueResponse);
        assertThrows(IllegalStateException.class, () -> client.verifyNumber(code));
    }

    @Test
    public void testVerifyNumberEndpoint() throws Exception {
        new DynamicEndpointTestSpec<VerifyNumberRequest, VerifyNumberResponse>() {

            @Override
            protected RestEndpoint<VerifyNumberRequest, VerifyNumberResponse> endpoint() {
                return client.verifyNumber;
            }

            @Override
            protected Set<Class<? extends AuthMethod>> expectedAuthMethods() {
                return Set.of(NetworkAuthMethod.class);
            }

            @Override
            protected Class<? extends VonageApiResponseException> expectedResponseExceptionType() {
                return CamaraResponseException.class;
            }

            @Override
            protected String expectedDefaultBaseUri() {
                return "https://api-eu.vonage.com";
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected VerifyNumberRequest sampleRequest() {
                return new VerifyNumberRequest(msisdn, redirectUrl).withCode(code);
            }

            @Override
            protected String expectedEndpointUri(VerifyNumberRequest request) {
                return "/camara/number-verification/v031/verify";
            }

            @Override
            protected String sampleRequestBodyString() {
                return "{\"phoneNumber\":\"+"+msisdn.replace(" ", "")+"\"}";
            }

            @Override
            public void runTests() throws Exception {
                super.runTests();
                testRequestWithoutCode();
            }

            void testRequestWithoutCode() {
                var request = new VerifyNumberRequest(msisdn, redirectUrl);
                assertThrows(NullPointerException.class,
                        () -> assertRequestUriAndBody(request, sampleRequestBodyString())
                );
            }
        }
        .runTests();
    }
}
