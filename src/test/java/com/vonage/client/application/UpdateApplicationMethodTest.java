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
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UpdateApplicationMethodTest extends AppBasicAuthTest {
    private UpdateApplicationMethod method;

    @Before
    public void setUp() {
        method = new UpdateApplicationMethod(new HttpWrapper());
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = method.makeRequest(
                Application.fromJson("{ \"id\": \"78d335fa323d01149c3dd6f0d48968cf\" }")
        );

        assertEquals("PUT", builder.getMethod());
        assertEquals("application/json", builder.getFirstHeader("Content-Type").getValue());
    }

    @Test
    public void testDefaultUri() throws Exception {
        RequestBuilder builder = method.makeRequest(
                Application.fromJson("{ \"id\": \"78d335fa323d01149c3dd6f0d48968cf\" }")
        );

        assertEquals("PUT", builder.getMethod());
        assertEquals("https://api.nexmo.com/v2/applications/78d335fa323d01149c3dd6f0d48968cf", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        UpdateApplicationMethod method = new UpdateApplicationMethod(wrapper);

        RequestBuilder builder = method.makeRequest(
                Application.fromJson("{ \"id\": \"78d335fa323d01149c3dd6f0d48968cf\" }")
        );

        assertEquals("PUT", builder.getMethod());
        assertEquals("https://example.com/v2/applications/78d335fa323d01149c3dd6f0d48968cf", builder.build().getURI().toString());
    }
}
