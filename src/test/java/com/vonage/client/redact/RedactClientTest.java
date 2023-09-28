/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.redact;

import com.vonage.client.ClientTest;
import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collection;

public class RedactClientTest extends ClientTest<RedactClient> {

    public RedactClientTest() {
        client = new RedactClient(wrapper);
    }

    @Test
    public void testSuccessfulResponse() throws Exception {
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.SMS);
        redactRequest.setType(RedactRequest.Type.INBOUND);
        stubResponseAndRun(204, () -> client.redactTransaction(redactRequest));
        stubResponse(204);
        client.redactTransaction(redactRequest.getId(), redactRequest.getProduct(), redactRequest.getType());
    }

    @Test
    public void testInvalidRedactRequests() {
        assertThrows(IllegalArgumentException.class, () -> client.redactTransaction(
                new RedactRequest("test-id", RedactRequest.Product.SMS)
        ));
        assertThrows(IllegalArgumentException.class, () -> client.redactTransaction(
                new RedactRequest("test-id", null)
        ));
        assertThrows(IllegalArgumentException.class, () -> client.redactTransaction(
                new RedactRequest(null, RedactRequest.Product.SMS)
        ));
    }

    @Test(expected = VonageBadRequestException.class)
    public void testWrongCredentials() throws Exception {
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.VOICE);
        stubResponseAndRun(401, () -> client.redactTransaction(redactRequest));
        stubResponse(401);
        client.redactTransaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test(expected = VonageBadRequestException.class)
    public void testPrematureRedactionOrUnauthorized() throws Exception {
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.VOICE);
        stubResponseAndRun(403, () -> client.redactTransaction(redactRequest));
        stubResponse(403);
        client.redactTransaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test(expected = VonageBadRequestException.class)
    public void testInvalidId() throws Exception {
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.VOICE);
        stubResponseAndRun(404, () -> client.redactTransaction(redactRequest));
        stubResponse(404);
        client.redactTransaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test(expected = VonageBadRequestException.class)
    public void testInvalidJsonInvalidProduct() throws Exception {
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.VOICE);
        stubResponseAndRun(422, () -> client.redactTransaction(redactRequest));
        stubResponse(422);
        client.redactTransaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test(expected = VonageBadRequestException.class)
    public void testRateLimit() throws Exception {
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.VOICE);
        stubResponseAndRun(429, () -> client.redactTransaction(redactRequest));
        stubResponse(429);
        client.redactTransaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test
    public void testRedactTransactionEndpoint() throws Exception {
        new DynamicEndpointTestSpec<RedactRequest, Void>() {

            @Override
            protected RestEndpoint<RedactRequest, Void> endpoint() {
                return client.redactTransaction;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
                return Arrays.asList(SignatureAuthMethod.class, TokenAuthMethod.class);
            }

            @Override
            protected Class<? extends Exception> expectedResponseExceptionType() {
                return VonageBadRequestException.class;
            }

            @Override
            protected String expectedDefaultBaseUri() {
                return "https://api.nexmo.com";
            }

            @Override
            protected String expectedEndpointUri(RedactRequest request) {
                return "/v1/redact/transaction";
            }

            @Override
            protected RedactRequest sampleRequest() {
                RedactRequest request = new RedactRequest("test-id", RedactRequest.Product.SMS);
                request.setType(RedactRequest.Type.INBOUND);
                return request;
            }

            @Override
            protected String sampleRequestBodyString() {
                return "{\"id\":\"test-id\",\"product\":\"sms\",\"type\":\"inbound\"}";
            }
        }
        .runTests();
    }
}
