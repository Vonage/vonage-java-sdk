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
package com.vonage.client.voice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import static com.vonage.client.TestUtils.test429;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class DtmfEndpointTest {
    private DtmfEndpoint endpoint;
    private final String uuid = UUID.randomUUID().toString();

    @Before
    public void setUp() throws Exception {
        endpoint = new DtmfEndpoint(new HttpWrapper());
    }

    @Test
    public void makeRequest() throws Exception {
        HttpWrapper httpWrapper = new HttpWrapper();
        DtmfEndpoint methodUnderTest = new DtmfEndpoint(httpWrapper);

        RequestBuilder request = methodUnderTest.makeRequest(new DtmfRequestWrapper(uuid, new DtmfPayload("867")));

        assertEquals("PUT", request.getMethod());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), request.getFirstHeader("Content-Type").getValue());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), request.getFirstHeader("Accept").getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(request.getEntity().getContent(), JsonNode.class);
        assertEquals("867", node.get("digits").asText());
    }

    @Test
    public void parseResponse() throws Exception {
        HttpWrapper wrapper = new HttpWrapper();
        DtmfEndpoint methodUnderTest = new DtmfEndpoint(wrapper);

        HttpResponse stubResponse = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("1.1", 1, 1),
                200,
                "OK"
        ));

        String json = "{\"message\": \"DTMF sent\",\"uuid\": \""+uuid+"\"}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        DtmfResponse response = methodUnderTest.parseResponse(stubResponse);
        assertEquals("DTMF sent", response.getMessage());
        assertEquals(uuid, response.getUuid());
    }

    @Test
    public void testRequestThrottleResponse() throws Exception {
        test429(new DtmfEndpoint(null));
    }

    @Test
    public void testCustomUri() throws Exception {
        String expectedUri = "https://example.com/v1/calls/"+uuid+"/dtmf";
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        endpoint = new DtmfEndpoint(wrapper);
        RequestBuilder builder = endpoint.makeRequest(new DtmfRequestWrapper(uuid, new DtmfPayload("1")));
        assertEquals("PUT", builder.getMethod());
        assertEquals(expectedUri, builder.build().getURI().toString());
    }
}
