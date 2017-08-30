package com.nexmo.client.voice.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.voice.TalkRequest;
import com.nexmo.client.voice.TalkResponse;
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
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

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
public class StartTalkMethodTest {
    private static final Log LOG = LogFactory.getLog(StartTalkMethodTest.class);

    @Test
    public void makeRequest() throws Exception {
        StartTalkMethod methodUnderTest = new StartTalkMethod(null);

        // Execute test call:
        RequestBuilder request = methodUnderTest.makeRequest(new TalkRequest("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", "Hello World!", 0));

        assertEquals("PUT", request.getMethod());
        assertEquals("application/json", request.getFirstHeader("Content-Type").getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(request.getEntity().getContent(), JsonNode.class);
        LOG.info(request.getEntity().getContent());
        assertEquals("Hello World!", node.get("text").asText());
        assertEquals("0", node.get("loop").asText());
    }

    @Test
    public void testPassUriInConstructor() throws Exception {
        StartTalkMethod methodUnderTest = new StartTalkMethod(null, "https://example.com");
        assertEquals("https://example.com/v1/calls", methodUnderTest.getUri());
    }

    @Test
    public void testSetUri() throws Exception {
        StartTalkMethod methodUnderTest = new StartTalkMethod(null);
        assertEquals("https://api.nexmo.com/v1/calls", methodUnderTest.getUri());
        methodUnderTest.setUri("https://example.com");
        assertEquals("https://example.com/v1/calls", methodUnderTest.getUri());
    }

    @Test
    public void parseResponse() throws Exception {
        StartTalkMethod methodUnderTest = new StartTalkMethod(null);

        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );

        String json = "{\"message\":\"Talk started\",\"uuid\":\"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        // Execute test call:
        TalkResponse talkResponse = methodUnderTest.parseResponse(stubResponse);
        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", talkResponse.getUuid());
        assertEquals("Talk started", talkResponse.getMessage());
    }
}
