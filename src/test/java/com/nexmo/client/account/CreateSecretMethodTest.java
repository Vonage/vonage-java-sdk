/*
 * Copyright (c) 2020 Vonage
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
package com.nexmo.client.account;

import com.nexmo.client.HttpConfig;
import com.nexmo.client.HttpWrapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateSecretMethodTest {
    private CreateSecretMethod method;

    @Before
    public void setUp() {
        this.method = new CreateSecretMethod(new HttpWrapper());
    }

    @Test
    public void testConstructParams() throws Exception {
        CreateSecretRequest request = new CreateSecretRequest("account-id", "secret");

        RequestBuilder builder = method.makeRequest(request);
        HttpEntity entity = builder.getEntity();

        assertEquals(entity.getContentType().getValue(), ContentType.APPLICATION_JSON.toString());
        assertNotNull(entity.getContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructParamsWithMissingApiKey() throws Exception {
        CreateSecretRequest request = new CreateSecretRequest(null, "secret");

        method.makeRequest(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructParamsWithMissingSecret() throws Exception {
        CreateSecretRequest request = new CreateSecretRequest("account-id", null);

        method.makeRequest(request);
    }

    @Test
    public void testDefaultUri() throws Exception {
        CreateSecretRequest request = new CreateSecretRequest("account-id", "secret");

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/accounts/account-id/secrets", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        CreateSecretMethod method = new CreateSecretMethod(wrapper);
        CreateSecretRequest request = new CreateSecretRequest("account-id", "secret");

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/accounts/account-id/secrets", builder.build().getURI().toString());
    }
}
