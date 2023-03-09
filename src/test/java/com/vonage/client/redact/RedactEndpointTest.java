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

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RedactEndpointTest {

    private RedactEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        endpoint = new RedactEndpoint(new HttpWrapper());
    }

    @Test
    public void testConstructParamsWithoutType() throws Exception {
        RedactRequest request = new RedactRequest("test-id", RedactRequest.Product.VOICE);

        RequestBuilder builder = endpoint.makeRequest(request);
        HttpEntity entity = builder.getEntity();

        assertEquals(entity.getContentType().getValue(), ContentType.APPLICATION_JSON.toString());
        assertEquals("application/json", builder.getFirstHeader("Content-Type").getValue());
        assertNotNull(entity.getContent());
    }

    @Test
    public void testConstructParamsWithType() throws Exception {
        RedactRequest request = new RedactRequest("test-id", RedactRequest.Product.SMS);
        request.setType(RedactRequest.Type.INBOUND);

        RequestBuilder builder = endpoint.makeRequest(request);
        HttpEntity entity = builder.getEntity();

        assertEquals(entity.getContentType().getValue(), ContentType.APPLICATION_JSON.toString());
        assertNotNull(entity.getContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructParamsWithMissingId() throws Exception {
        RedactRequest request = new RedactRequest(null, RedactRequest.Product.SMS);

        endpoint.makeRequest(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructParamsWithMissingProduct() throws Exception {
        RedactRequest request = new RedactRequest("test-id", null);

        endpoint.makeRequest(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructParamsWithMissingTypeAndSmsProduct() throws Exception {
        RedactRequest request = new RedactRequest("test-id", RedactRequest.Product.SMS);

        endpoint.makeRequest(request);
    }

    @Test
    public void testDefaultUri() throws Exception {
        RedactRequest request = new RedactRequest("test-id", RedactRequest.Product.VOICE);

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/redact/transaction", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        RedactEndpoint method = new RedactEndpoint(wrapper);
        RedactRequest request = new RedactRequest("test-id", RedactRequest.Product.VOICE);

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/v1/redact/transaction", builder.build().getURI().toString());
    }
}
