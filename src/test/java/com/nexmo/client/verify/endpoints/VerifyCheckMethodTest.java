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

package com.nexmo.client.verify.endpoints;

import com.nexmo.client.verify.CheckRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.nexmo.client.TestUtils.assertContainsParam;
import static com.nexmo.client.TestUtils.assertParamMissing;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class VerifyCheckMethodTest {
    private VerifyCheckMethod endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new VerifyCheckMethod(null);
    }

    @Test
    public void testMakeRequest() throws Exception {
        final String expectedUrl = "https://api.nexmo.com/verify/check/xml";

        RequestBuilder request = this.endpoint.makeRequest(new CheckRequest("a-request-id", "1234"));

        assertEquals("POST", request.build().getMethod());
        assertEquals(expectedUrl, request.build().getURI().toString());

        List<NameValuePair> params = request.getParameters();
        assertContainsParam(params, "request_id", "a-request-id");
        assertContainsParam(params, "code", "1234");
        assertParamMissing(params, "ip_address");
    }

    @Test
    public void testMakeRequestWithIpAddress() throws Exception {
        final String expectedUrl = "https://api.nexmo.com/verify/check/xml";

        RequestBuilder request = this.endpoint.makeRequest(
                new CheckRequest("a-request-id", "1234", "123.123.123.123"));

        assertEquals("POST", request.build().getMethod());
        assertEquals(expectedUrl, request.build().getURI().toString());

        List<NameValuePair> params = request.getParameters();
        assertContainsParam(params, "request_id", "a-request-id");
        assertContainsParam(params, "code", "1234");
        assertContainsParam(params, "ip_address", "123.123.123.123");
    }


    @Test
    public void testCustomBaseUrl() throws Exception {
        final String expectedUrl = "https://api.example.com/verify/check/xml";

        this.endpoint.setBaseUrl("https://api.example.com/");
        RequestBuilder request = this.endpoint.makeRequest(new CheckRequest("a-request-id", "1234"));

        assertEquals("POST", request.build().getMethod());
        assertEquals(expectedUrl, request.build().getURI().toString());
    }

    @Test
    public void testMissingRequestIdThrows() throws Exception {
        try {
            this.endpoint.makeRequest(new CheckRequest(null, "1234"));
            fail("null requestId should throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // This is expected
        }
    }

    @Test
    public void testMissingCodeThrows() throws Exception {
        try {
            this.endpoint.makeRequest(new CheckRequest("a-request-id", null));
            fail("null code should throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // This is expected
        }
    }
}