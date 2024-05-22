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
package com.vonage.client.conversion;

import com.vonage.client.AbstractClientTest;
import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageApiResponseException;
import com.vonage.client.auth.ApiKeyQueryParamsAuthMethod;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConversionClientTest extends AbstractClientTest<ConversionClient> {

    public ConversionClientTest() {
        client = new ConversionClient(wrapper);
    }

    @Test
    public void testSuccessfulResponse() throws Exception {
        stubResponse(200);
        client.submitConversion(ConversionRequest.Type.VOICE,
                             "MESSAGE-ID",
                             true,
                             new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
    }

    @Test
    public void testWrongCredentials() throws Exception {
        stubResponse(401);
        assertThrows(VonageApiResponseException.class, () -> client.submitConversion(
                    ConversionRequest.Type.SMS,
                     "MESSAGE-ID",
                    true,
                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12")
        ));
    }

    @Test
    public void testConversionEndpoint() throws Exception {
        new DynamicEndpointTestSpec<ConversionRequest, Void>() {

            @Override
            protected RestEndpoint<ConversionRequest, Void> endpoint() {
                return client.conversionEndpoint;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
                return Arrays.asList(SignatureAuthMethod.class, ApiKeyQueryParamsAuthMethod.class);
            }

            @Override
            protected Class<? extends Exception> expectedResponseExceptionType() {
                return VonageApiResponseException.class;
            }

            @Override
            protected String expectedDefaultBaseUri() {
                return "https://api.nexmo.com";
            }

            @Override
            protected String expectedEndpointUri(ConversionRequest request) {
                return "/conversions/" + request.getType().name().toLowerCase();
            }

            @Override
            protected ConversionRequest sampleRequest() {
                try {
                    return new ConversionRequest(ConversionRequest.Type.SMS,
                            "MESSAGE-ID",
                            true,
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12")
                    );
                }
                catch (ParseException px) {
                    throw new IllegalStateException(px);
                }
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                LinkedHashMap<String, String> params = new LinkedHashMap<>(4);
                params.put("message-id", "MESSAGE-ID");
                params.put("delivered", "true");
                params.put("timestamp", "2014-03-04 10:11:12");
                return params;
            }

            @Override
            public void runTests() throws Exception {
                super.runTests();
                testVoice();
            }

            void testVoice() throws Exception {
                String timestamp = "2023-08-28 10:48:12";
                ConversionRequest request = new ConversionRequest(ConversionRequest.Type.VOICE,
                        "messAG3-1D",
                        false,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp)
                );

                assertNotNull(request.getTimestamp());
                LinkedHashMap<String, String> expectedParams = new LinkedHashMap<>(4);
                expectedParams.put("message-id", request.getMessageId());
                expectedParams.put("delivered", String.valueOf(request.isDelivered()));
                expectedParams.put("timestamp", timestamp);
                assertRequestUriAndBody(request, expectedParams);
            }
        }
        .runTests();
    }
}
