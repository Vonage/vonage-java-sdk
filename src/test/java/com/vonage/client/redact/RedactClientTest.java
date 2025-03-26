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
package com.vonage.client.redact;

import com.vonage.client.AbstractClientTest;
import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.RestEndpoint;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class RedactClientTest extends AbstractClientTest<RedactClient> {

    public RedactClientTest() {
        client = new RedactClient(wrapper);
    }

    @Test
    public void testSuccessfulResponse() throws Exception {
        RedactRequest redactRequest = new RedactRequest("test-id", Product.SMS);
        redactRequest.setType(Type.INBOUND);
        stubResponseAndRun(204, () -> client.redactTransaction(redactRequest));
        stubResponse(204);
        client.redactTransaction(redactRequest.getId(), redactRequest.getProduct(), redactRequest.getType());
        stubResponseAndRun(204, () -> client.redactTransaction("test-id", Product.VERIFY));
    }

    @Test
    public void testRedactResponseException() throws Exception {
        assert401ApiResponseException(RedactResponseException.class, () ->
                client.redactTransaction(UUID.randomUUID().toString(), Product.MESSAGES)
        );
    }

    @Test
    public void testInvalidRedactRequests() {
        assertThrows(IllegalArgumentException.class, () -> client.redactTransaction(
                "test-id", Product.SMS
        ));
        assertThrows(IllegalArgumentException.class, () -> client.redactTransaction(
                "test-id", null
        ));
        assertThrows(IllegalArgumentException.class, () -> client.redactTransaction(
                new RedactRequest(null, Product.SMS)
        ));
    }

    @Test
    public void testWrongCredentials() throws Exception {
        RedactRequest redactRequest = new RedactRequest("test-id", Product.VOICE);
        stubResponseAndAssertThrows(401, () ->
                client.redactTransaction(redactRequest),
                RedactResponseException.class
        );
        stubResponseAndAssertThrows(401, () ->
                client.redactTransaction(redactRequest.getId(), redactRequest.getProduct()),
                RedactResponseException.class
        );
    }

    @Test
    public void testPrematureRedactionOrUnauthorized() throws Exception {
        RedactRequest redactRequest = new RedactRequest("test-id", Product.VOICE);
        stubResponseAndAssertThrows(403, () ->
                client.redactTransaction(redactRequest), RedactResponseException.class
        );
        stubResponseAndAssertThrows(403, () ->
                client.redactTransaction(redactRequest.getId(), redactRequest.getProduct()),
                RedactResponseException.class
        );
    }

    @Test
    public void testInvalidId() throws Exception {
        RedactRequest redactRequest = new RedactRequest("test-id", Product.VOICE);
        stubResponseAndAssertThrows(404, () ->
                client.redactTransaction(redactRequest), RedactResponseException.class
        );
        stubResponseAndAssertThrows(404, () ->
                client.redactTransaction(redactRequest.getId(), redactRequest.getProduct()),
                RedactResponseException.class
        );
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
                return List.of(ApiKeyHeaderAuthMethod.class);
            }

            @Override
            protected Class<? extends Exception> expectedResponseExceptionType() {
                return RedactResponseException.class;
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
                RedactRequest request = new RedactRequest("test-id", Product.SMS);
                request.setType(Type.INBOUND);
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
