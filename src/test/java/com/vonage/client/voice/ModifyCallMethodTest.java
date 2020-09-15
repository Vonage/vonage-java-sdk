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
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ModifyCallMethodTest {
    private static final Log LOG = LogFactory.getLog(ModifyCallMethodTest.class);

    private ModifyCallMethod method;

    @Before
    public void setUp() throws Exception {
        method = new ModifyCallMethod(new HttpWrapper());
    }

    @Test
    public void makeRequest() throws Exception {
        RequestBuilder request = method.makeRequest(
                new CallModifier("abc-123", ModifyCallAction.HANGUP)
        );

        assertEquals("PUT", request.getMethod());
        assertEquals("application/json", request.getFirstHeader("Content-Type").getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(request.getEntity().getContent(), JsonNode.class);
        assertEquals("hangup", node.get("action").asText());
    }

    @Test
    public void earmuffRequest() throws Exception {
        RequestBuilder request = method.makeRequest(
                new CallModifier("abc-123", ModifyCallAction.EARMUFF)
        );

        assertEquals("PUT", request.getMethod());
        assertEquals("application/json", request.getFirstHeader("Content-Type").getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(request.getEntity().getContent(), JsonNode.class);
        assertEquals("earmuff", node.get("action").asText());
    }

    @Test
    public void unearmuffRequest() throws Exception {
        RequestBuilder request = method.makeRequest(
                new CallModifier("abc-123", ModifyCallAction.UNEARMUFF)
        );

        assertEquals("PUT", request.getMethod());
        assertEquals("application/json", request.getFirstHeader("Content-Type").getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(request.getEntity().getContent(), JsonNode.class);
        assertEquals("unearmuff", node.get("action").asText());
    }

    @Test
    public void muteRequest() throws Exception {
        RequestBuilder request = method.makeRequest(
                new CallModifier("abc-123", ModifyCallAction.MUTE)
        );

        assertEquals("PUT", request.getMethod());
        assertEquals("application/json", request.getFirstHeader("Content-Type").getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(request.getEntity().getContent(), JsonNode.class);
        assertEquals("mute", node.get("action").asText());
    }

    @Test
    public void unmuteRequest() throws Exception {
        RequestBuilder request = method.makeRequest(
                new CallModifier("abc-123", ModifyCallAction.UNMUTE)
        );

        assertEquals("PUT", request.getMethod());
        assertEquals("application/json", request.getFirstHeader("Content-Type").getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(request.getEntity().getContent(), JsonNode.class);
        assertEquals("unmute", node.get("action").asText());
    }

    @Test
    public void parseResponse() throws Exception {
        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );

        String json = "{\"message\":\"Received\"}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        ModifyCallResponse response = method.parseResponse(stubResponse);
        assertEquals("Received", response.getMessage());
    }

    @Test
    public void parseNullResponse() throws Exception {
        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 204, "OK")
        );

        ModifyCallResponse response = method.parseResponse(stubResponse);
        assertNull(response);
    }

    @Test
    public void testCustomUri() throws Exception {
        String expectedUri = "https://example.com/v1/calls/ssf61863-4a51-ef6b-11e1-w6edebcf93bA";

        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        method = new ModifyCallMethod(wrapper);

        RequestBuilder builder = method.makeRequest(
                new CallModifier("ssf61863-4a51-ef6b-11e1-w6edebcf93bA", ModifyCallAction.HANGUP)
        );
        assertEquals("PUT", builder.getMethod());
        assertEquals(expectedUri, builder.build().getURI().toString());
    }
}
