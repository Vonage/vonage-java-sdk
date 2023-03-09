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
import com.vonage.client.VonageBadRequestException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.fail;

public class RedactClientTest extends ClientTest<RedactClient> {
    @Before
    public void setUp() {
        client = new RedactClient(wrapper);
    }

    @Test
    public void testSuccessfulResponse() {
        try {
            wrapper.setHttpClient(stubHttpClient(204, ""));
            RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.SMS);
            redactRequest.setType(RedactRequest.Type.INBOUND);

            client.redactTransaction(redactRequest);
            client.redactTransaction(redactRequest.getId(), redactRequest.getProduct(), redactRequest.getType());
        } catch (Exception e) {
            fail("No exceptions should be thrown.");
        }
    }

    @Test(expected = VonageBadRequestException.class)
    public void testWrongCredentials() throws Exception {
        wrapper.setHttpClient(stubHttpClient(401, ""));
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.VOICE);
        client.redactTransaction(redactRequest);
        client.redactTransaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test(expected = VonageBadRequestException.class)
    public void testPrematureRedactionOrUnauthorized() throws Exception {
        wrapper.setHttpClient(stubHttpClient(403, ""));
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.VOICE);
        client.redactTransaction(redactRequest);
        client.redactTransaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test(expected = VonageBadRequestException.class)
    public void testInvalidId() throws Exception {
        wrapper.setHttpClient(stubHttpClient(404, ""));
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.VOICE);
        client.redactTransaction(redactRequest);
        client.redactTransaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test(expected = VonageBadRequestException.class)
    public void testInvalidJsonInvalidProduct() throws Exception {
        wrapper.setHttpClient(stubHttpClient(422, ""));
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.VOICE);
        client.redactTransaction(redactRequest);
        client.redactTransaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test(expected = VonageBadRequestException.class)
    public void testRateLimit() throws Exception {
        wrapper.setHttpClient(stubHttpClient(429, ""));
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.VOICE);
        client.redactTransaction(redactRequest);
        client.redactTransaction(redactRequest.getId(), redactRequest.getProduct());
    }

}
