/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
import com.nexmo.client.TestUtils;
import com.nexmo.client.auth.SignatureAuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ControlEndpointTest {
    private ControlEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new ControlEndpoint(new HttpWrapper());
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = this.endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{TokenAuthMethod.class}, auths);
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest(new ControlRequest("request-id",
                VerifyControlCommand.CANCEL
        ));
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/verify/control/json", builder.build().getURI().toString());

        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("request-id", params.get("request_id"));
        assertEquals("cancel", params.get("cmd"));
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(200,
                "{\n" + "  \"status\":\"0\",\n" + "  \"command\":\"cancel\"\n" + "}"
        );
        ControlResponse response = this.endpoint.parseResponse(stub);
        assertEquals("0", response.getStatus());
        assertEquals(VerifyControlCommand.CANCEL, response.getCommand());
    }

    @Test
    public void testParseErrorResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(200,
                "{\n" + "    \"error_text\": \"Missing username\",\n" + "    \"status\": \"2\"\n" + "}"
        );
        try {
            ControlResponse response = this.endpoint.parseResponse(stub);
            fail("Parsing an error response should throw an exception.");
        } catch (VerifyException exc) {
            assertEquals("2", exc.getStatus());
            assertEquals("Missing username", exc.getErrorText());
        }

    }

    @Test
    public void testDefaultUri() throws Exception {
        ControlRequest request = new ControlRequest("request-id", VerifyControlCommand.CANCEL);

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/verify/control/json", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        ControlEndpoint endpoint = new ControlEndpoint(wrapper);
        ControlRequest request = new ControlRequest("request-id", VerifyControlCommand.CANCEL);

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/verify/control/json", builder.build().getURI().toString());
    }
}
