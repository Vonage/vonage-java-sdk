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
package com.nexmo.client.verify;

import com.nexmo.client.HttpConfig;
import com.nexmo.client.HttpWrapper;
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
