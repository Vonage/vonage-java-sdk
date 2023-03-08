/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.numbers;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageBadRequestException;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;


public class CancelNumberEndpointTest {
    CancelNumberEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        endpoint = new CancelNumberEndpoint(new HttpWrapper());
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
