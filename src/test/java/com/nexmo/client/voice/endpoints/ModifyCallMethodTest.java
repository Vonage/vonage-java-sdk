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
package com.nexmo.client.voice.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.voice.CallModifier;
import com.nexmo.client.voice.ModifyCallAction;
import com.nexmo.client.voice.ModifyCallResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class ModifyCallMethodTest {
    private static final Log LOG = LogFactory.getLog(ModifyCallMethodTest.class);

    @Test
    public void makeRequest() throws Exception {
        HttpWrapper httpWrapper = new HttpWrapper();
        ModifyCallMethod methodUnderTest = new ModifyCallMethod(httpWrapper);

        RequestBuilder request = methodUnderTest.makeRequest(
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
        HttpWrapper httpWrapper = new HttpWrapper();
        ModifyCallMethod methodUnderTest = new ModifyCallMethod(httpWrapper);

        RequestBuilder request = methodUnderTest.makeRequest(
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
        HttpWrapper httpWrapper = new HttpWrapper();
        ModifyCallMethod methodUnderTest = new ModifyCallMethod(httpWrapper);

        RequestBuilder request = methodUnderTest.makeRequest(
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
        HttpWrapper httpWrapper = new HttpWrapper();
        ModifyCallMethod methodUnderTest = new ModifyCallMethod(httpWrapper);

        RequestBuilder request = methodUnderTest.makeRequest(
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
        HttpWrapper httpWrapper = new HttpWrapper();
        ModifyCallMethod methodUnderTest = new ModifyCallMethod(httpWrapper);

        RequestBuilder request = methodUnderTest.makeRequest(
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
        HttpWrapper wrapper = new HttpWrapper();
        ModifyCallMethod methodUnderTest = new ModifyCallMethod(wrapper);

        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );

        String json = "{\"message\":\"Received\"}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        ModifyCallResponse response = methodUnderTest.parseResponse(stubResponse);
        assertEquals("Received", response.getMessage());
    }

    @Test
    public void testSetUri() throws Exception {
        ModifyCallMethod methodUnderTest = new ModifyCallMethod(null);
        methodUnderTest.setUri("https://example.com/dummy/");
        RequestBuilder req = methodUnderTest.makeRequest(
                new CallModifier("uuid-1234", ModifyCallAction.HANGUP)
        );
        assertEquals(new URI("https://example.com/dummy/uuid-1234"), req.getUri());
    }
}
