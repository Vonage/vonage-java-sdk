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
package com.nexmo.client.numbers;

import com.nexmo.client.NexmoBadRequestException;
import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.TestUtils;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Test;

import java.util.Map;

import static com.nexmo.client.TestUtils.test429;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;


public class CancelNumberEndpointTest {
    @Test
    public void makeRequest() throws Exception {
        CancelNumberEndpoint methodUnderTest = new CancelNumberEndpoint(null);

        RequestBuilder request = methodUnderTest.makeRequest(new CancelNumberRequest("AA", "447700900000"));

        assertEquals("POST", request.getMethod());
        assertEquals(request.getUri().toString(), "https://rest.nexmo.com/number/cancel");
        Map<String, String> params = TestUtils.makeParameterMap(request.getParameters());
        assertEquals("AA", params.get("country"));
        assertEquals("447700900000", params.get("msisdn"));
    }

    @Test
    public void customBaseUrl() throws Exception {
        CancelNumberEndpoint methodUnderTest = new CancelNumberEndpoint(null);
        methodUnderTest.setBaseUrl("https://rest.example.com/");

        RequestBuilder request = methodUnderTest.makeRequest(new CancelNumberRequest("AA", "447700900000"));

        assertEquals("POST", request.getMethod());
        assertEquals(request.getUri().toString(), "https://rest.example.com/number/cancel");
        Map<String, String> params = TestUtils.makeParameterMap(request.getParameters());
        assertEquals("AA", params.get("country"));
        assertEquals("447700900000", params.get("msisdn"));
    }

    @Test
    public void parseResponse() throws Exception {
        CancelNumberEndpoint methodUnderTest = new CancelNumberEndpoint(null);
        String json = "{\n" +
                "  \"error-code\":\"200\",\n" +
                "  \"error-code-label\":\"success\"\n" +
                "}";

        CancelNumberResponse response = methodUnderTest.parseResponse(TestUtils.makeJsonHttpResponse(200, json));
        assertEquals("200", response.getErrorCode());
        assertEquals("success", response.getErrorCodeLabel());
    }

    @Test
    public void parseBadRequestResponse() throws Exception {
        CancelNumberEndpoint methodUnderTest = new CancelNumberEndpoint(null);

        String json = "{\n" +
                "    \"error_title\": \"Bad Request\",\n" +
                "    \"invalid_parameters\": {\n" +
                "        \"country\": \"Is required.\"\n" +
                "    },\n" +
                "    \"type\": \"BAD_REQUEST\"\n" +
                "}";
        try {
            methodUnderTest.parseResponse(TestUtils.makeJsonHttpResponse(400, json));
            fail("A 400 response should raise a NexmoBadRequestException");
        } catch (NexmoBadRequestException e) {
            // This is expected
        }
    }

    @Test
    public void parseMethodFailedResponse() throws Exception {
        CancelNumberEndpoint methodUnderTest = new CancelNumberEndpoint(null);
        String json = "{\n" +
                "    \"error_title\": \"Bad Request\",\n" +
                "    \"invalid_parameters\": {\n" +
                "        \"country\": \"Is required.\"\n" +
                "    },\n" +
                "    \"type\": \"BAD_REQUEST\"\n" +
                "}";
        try {
            methodUnderTest.parseResponse(TestUtils.makeJsonHttpResponse(500, json));
            fail("A 500 response should raise a NexmoMethodFailedException");
        } catch (NexmoMethodFailedException e) {
            // This is expected
        }
    }

    @Test
    public void testRequestThrottleResponse() throws Exception {
        test429(new CancelNumberEndpoint(null));
    }
}
