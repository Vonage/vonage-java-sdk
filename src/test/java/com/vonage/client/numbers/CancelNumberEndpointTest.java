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
package com.vonage.client.numbers;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.TestUtils;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;


public class CancelNumberEndpointTest {
    CancelNumberEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new CancelNumberEndpoint(new HttpWrapper());
    }

    @Test
    public void makeRequest() throws Exception {
        RequestBuilder request = endpoint.makeRequest(new CancelNumberRequest("AA", "447700900000"));

        assertEquals("POST", request.getMethod());
        Map<String, String> params = TestUtils.makeParameterMap(request.getParameters());
        assertEquals("AA", params.get("country"));
        assertEquals("447700900000", params.get("msisdn"));
    }

    @Test
    public void parseResponse() throws Exception {
        String json = "{\n" + "  \"error-code\":\"200\",\n" + "  \"error-code-label\":\"success\"\n" + "}";

        endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, json));
    }

    @Test
    public void parseBadRequestResponse() throws Exception {
        String json = "{\n" + "    \"error_title\": \"Bad Request\",\n" + "    \"invalid_parameters\": {\n"
                + "        \"country\": \"Is required.\"\n" + "    },\n" + "    \"type\": \"BAD_REQUEST\"\n" + "}";
        try {
            endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, json));
            fail("A 400 response should raise a VonageBadRequestException");
        } catch (VonageBadRequestException e) {
            // This is expected
        }
    }

    @Test
    public void parseMethodFailedResponse() throws Exception {
        String json = "{\n" + "    \"error_title\": \"Bad Request\",\n" + "    \"invalid_parameters\": {\n"
                + "        \"country\": \"Is required.\"\n" + "    },\n" + "    \"type\": \"BAD_REQUEST\"\n" + "}";
        try {
            endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, json));
            fail("A 500 response should raise a VonageBadRequestException");
        } catch (VonageBadRequestException e) {
            // This is expected
        }
    }

    @Test
    public void testDefaultUri() throws Exception {
        CancelNumberRequest request = new CancelNumberRequest("AA", "447700900000");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://rest.nexmo.com/number/cancel", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        CancelNumberEndpoint endpoint = new CancelNumberEndpoint(wrapper);
        CancelNumberRequest request = new CancelNumberRequest("AA", "447700900000");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/number/cancel", builder.build().getURI().toString());
    }
}
