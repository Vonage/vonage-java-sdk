/*
 *   Copyright 2020 Vonage
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
package com.vonage.client.account;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
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
