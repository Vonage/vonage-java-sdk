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
package com.vonage.client.verify;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CheckMethodTest extends MethodTest<CheckMethod> {
    @Before
    public void setUp() {
        method = new CheckMethod(new HttpWrapper());
    }

    @Test
    public void testConstructVerifyParamsWithoutIp() throws Exception {
        CheckRequest checkRequest = new CheckRequest("request-id", "code");
        RequestBuilder request = method.makeRequest(checkRequest);
        List<NameValuePair> params = request.getParameters();

        assertContainsParam(params, "request_id", "request-id");
        assertContainsParam(params, "code", "code");
        assertParamMissing(params, "ip_address");
    }

    @Test
    public void testConstructVerifyParamsWithIp() throws Exception {
        CheckRequest checkRequest = new CheckRequest("request-id", "code", "ip-address");
        RequestBuilder request = method.makeRequest(checkRequest);
        List<NameValuePair> params = request.getParameters();

        assertContainsParam(params, "request_id", "request-id");
        assertContainsParam(params, "code", "code");
        assertContainsParam(params, "ip_address", "ip-address");
    }

    @Test
    public void testDefaultUri() throws Exception {
        CheckRequest request = new CheckRequest("request-id", "code");
        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/verify/check/json", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        CheckMethod method = new CheckMethod(wrapper);
        CheckRequest request = new CheckRequest("request-id", "code");

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/verify/check/json", builder.build().getURI().toString());
    }
}
