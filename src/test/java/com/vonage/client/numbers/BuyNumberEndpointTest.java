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
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class BuyNumberEndpointTest {
    private BuyNumberEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        endpoint = new BuyNumberEndpoint(new HttpWrapper());
    }

    @Test
    public void makeRequest() throws Exception {
        RequestBuilder request = endpoint.makeRequest(new BuyNumberRequest("AA", "447700900000"));

        assertEquals("POST", request.getMethod());
        Map<String, String> params = TestUtils.makeParameterMap(request.getParameters());
        assertEquals("AA", params.get("country"));
        assertEquals("447700900000", params.get("msisdn"));
    }

    @Test
    public void parseResponse() throws Exception {
        HttpResponse stubResponse = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("1.1", 1, 1),
                200,
                "OK"
        ));

        String json = "{\n" + "  \"error-code\":\"200\",\n" + "  \"error-code-label\":\"success\"\n" + "}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        endpoint.parseResponse(stubResponse);
    }

    @Test
    public void parseBadRequestResponse() throws Exception {
        HttpResponse stubResponse = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("1.1", 1, 1),
                400,
                "OK"
        ));

        String json = "{\n" + "    \"error_title\": \"Bad Request\",\n" + "    \"invalid_parameters\": {\n"
                + "        \"country\": \"Is required.\"\n" + "    },\n" + "    \"type\": \"BAD_REQUEST\"\n" + "}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        try {
            endpoint.parseResponse(stubResponse);
            fail("A 400 response should raise a VonageBadRequestException");
        } catch (VonageBadRequestException e) {
            // This is expected
        }
    }

    @Test
    public void testDefaultUri() throws Exception {
        BuyNumberRequest request = new BuyNumberRequest("AA", "447700900000");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://rest.nexmo.com/number/buy",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        BuyNumberEndpoint endpoint = new BuyNumberEndpoint(wrapper);
        BuyNumberRequest request = new BuyNumberRequest("AA", "447700900000");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/number/buy", builder.build().getURI().toString());
    }
}
