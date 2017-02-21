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
/* Copyright (c) 2011-2017 Nexmo Inc
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
import com.nexmo.client.voice.Call;
import com.nexmo.client.voice.CallDirection;
import com.nexmo.client.voice.CallEvent;
import com.nexmo.client.voice.CallStatus;
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

public class CreateCallMethodTest {
    private static final Log LOG = LogFactory.getLog(CreateCallMethodTest.class);

    @Test
    public void testMakeRequest() throws Exception {
        CreateCallMethod methodUnderTest = new CreateCallMethod(null);

        // Execute test call:
        RequestBuilder request = methodUnderTest.makeRequest(
                new Call("447700900903", "447700900904", "https://example.com/answer"));

        assertEquals("POST", request.getMethod());
        assertEquals("application/json", request.getFirstHeader("Content-Type").getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(request.getEntity().getContent(), JsonNode.class);
        LOG.info(request.getEntity().getContent());
        assertEquals("447700900903", node.get("to").get(0).get("number").asText());
        assertEquals("447700900904", node.get("from").get("number").asText());
        assertEquals("https://example.com/answer", node.get("answer_url").get(0).asText());
    }

    @Test
    public void testCustomUri() throws Exception {
        CreateCallMethod methodUnderTest = new CreateCallMethod(null);
        methodUnderTest.setUri("https://api.example.com/calls");
        RequestBuilder request = methodUnderTest.makeRequest(
                new Call("447700900903", "447700900904", "https://example.com/answer"));
        assertEquals("https://api.example.com/calls", request.getUri().toString());
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpWrapper wrapper = new HttpWrapper();
        CreateCallMethod methodUnderTest = new CreateCallMethod(wrapper);

        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );

        String json = " {\"uuid\":\"93137ee3-580e-45f7-a61a-e0b5716000ea\",\"status\":\"started\",\"direction\":\"outbound\",\"conversation_uuid\":\"aa17bd11-c895-4225-840d-30dc78c31e50\"}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        // Execute test call:
        CallEvent callEvent = methodUnderTest.parseResponse(stubResponse);
        assertEquals("93137ee3-580e-45f7-a61a-e0b5716000ea", callEvent.getUuid());
        assertEquals("aa17bd11-c895-4225-840d-30dc78c31e50", callEvent.getConversationUuid());
        assertEquals(CallStatus.STARTED, callEvent.getStatus());
        assertEquals(CallDirection.OUTBOUND, callEvent.getDirection());
    }
}
