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
package com.vonage.client.voice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.vonage.client.TestUtils.test429;
import static org.junit.Assert.assertEquals;


public class DtmfEndpointTest {
    private static final Log LOG = LogFactory.getLog(DtmfEndpointTest.class);

    private DtmfEndpoint method;
    @Before
    public void setUp() throws Exception {
        method = new DtmfEndpoint(new HttpWrapper());
    }

    @Test
    public void makeRequest() throws Exception {
        HttpWrapper httpWrapper = new HttpWrapper();
        DtmfEndpoint methodUnderTest = new DtmfEndpoint(httpWrapper);

        RequestBuilder request = methodUnderTest.makeRequest(new DtmfRequest("abc-123", "867"));

        assertEquals("PUT", request.getMethod());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), request.getFirstHeader("Content-Type").getValue());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), request.getFirstHeader("Accept").getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(request.getEntity().getContent(), JsonNode.class);
        LOG.info(request.getEntity().getContent());
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

        String json = "{\"message\": \"DTMF sent\",\"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        DtmfResponse response = methodUnderTest.parseResponse(stubResponse);
        assertEquals("DTMF sent", response.getMessage());
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
    }

    @Test
    public void testRequestThrottleResponse() throws Exception {
        test429(new DtmfEndpoint(null));
    }

    @Test
    public void testCustomUri() throws Exception {
        String expectedUri = "https://example.com/v1/calls/ssf61863-4a51-ef6b-11e1-w6edebcf93bb/dtmf";

        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        method = new DtmfEndpoint(wrapper);

        RequestBuilder builder = method.makeRequest(new DtmfRequest("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", "1"));
        assertEquals("PUT", builder.getMethod());
        assertEquals(expectedUri, builder.build().getURI().toString());
    }
}
