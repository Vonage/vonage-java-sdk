/*
 * Copyright (c) 2011-2018 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.redact;

import com.nexmo.client.ClientTest;
import com.nexmo.client.NexmoBadRequestException;
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
            wrapper.setHttpClient(this.stubHttpClient(204, ""));
            RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.SMS);
            redactRequest.setType(RedactRequest.Type.INBOUND);

            this.client.transaction(redactRequest);
            this.client.transaction(redactRequest.getId(), redactRequest.getProduct());
            this.client.transaction(redactRequest.getId(), redactRequest.getProduct(), redactRequest.getType());
        } catch (Exception e) {
            fail("No exceptions should be thrown.");
        }
    }

    @Test(expected = NexmoBadRequestException.class)
    public void testWrongCredentials() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(401, ""));
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.SMS);
        this.client.transaction(redactRequest);
        this.client.transaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test(expected = NexmoBadRequestException.class)
    public void testPrematureRedactionOrUnauthorized() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(403, ""));
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.SMS);
        this.client.transaction(redactRequest);
        this.client.transaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test(expected = NexmoBadRequestException.class)
    public void testInvalidId() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(404, ""));
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.SMS);
        this.client.transaction(redactRequest);
        this.client.transaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test(expected = NexmoBadRequestException.class)
    public void testInvalidJsonInvalidProduct() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(422, ""));
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.SMS);
        this.client.transaction(redactRequest);
        this.client.transaction(redactRequest.getId(), redactRequest.getProduct());
    }

    @Test(expected = NexmoBadRequestException.class)
    public void testRateLimit() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(429, ""));
        RedactRequest redactRequest = new RedactRequest("test-id", RedactRequest.Product.SMS);
        this.client.transaction(redactRequest);
        this.client.transaction(redactRequest.getId(), redactRequest.getProduct());
    }

}
