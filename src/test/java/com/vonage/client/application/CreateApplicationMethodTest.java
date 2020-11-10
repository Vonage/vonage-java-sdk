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
package com.vonage.client.application;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateApplicationMethodTest extends AppBasicAuthTest {
    private CreateApplicationMethod method;

    @Before
    public void setUp() {
        method = new CreateApplicationMethod(new HttpWrapper());
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = method.makeRequest(Application.builder().build());

        assertEquals("POST", builder.getMethod());
        assertEquals("application/json", builder.getFirstHeader("Content-Type").getValue());
    }

    @Test
    public void testDefaultUri() throws Exception {
        RequestBuilder builder = method.makeRequest(Application.builder().build());

        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/v2/applications", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        CreateApplicationMethod method = new CreateApplicationMethod(wrapper);

        RequestBuilder builder = method.makeRequest(Application.builder().build());

        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/v2/applications", builder.build().getURI().toString());
    }
}
