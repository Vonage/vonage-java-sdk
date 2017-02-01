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
import com.nexmo.client.voice.CallDirection;
import com.nexmo.client.voice.CallModifier;
import com.nexmo.client.voice.CallInfo;
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



public class ModifyCallMethodTest {
    private static final Log LOG = LogFactory.getLog(ModifyCallMethodTest.class);

    @Test
    public void makeRequest() throws Exception {
        HttpWrapper httpWrapper = new HttpWrapper(null);
        ModifyCallMethod methodUnderTest = new ModifyCallMethod(httpWrapper);

        RequestBuilder request = methodUnderTest.makeRequest(
                new CallModifier("abc-123", "hangup")
        );

        assertEquals("PUT", request.getMethod());
        assertEquals("application/json", request.getFirstHeader("Content-Type").getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(request.getEntity().getContent(), JsonNode.class);
        LOG.info(request.getEntity().getContent());
        assertEquals("hangup", node.get("action").asText());
    }

    @Test
    public void parseResponse() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(null);
        ModifyCallMethod methodUnderTest = new ModifyCallMethod(wrapper);

        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );

        String json = "{\"uuid\": \"63f61863-4a51-4f6b-86e1-46edebcf9356\"," +
                "\"conversation_uuid\": \"63f61863-4a51-4f6b-86e1-46edebio0123\"," +
                "\"to\": {\"type\": \"phone\",\"number\": \"441632960960\"}," +
                "\"from\": {\"type\": \"phone\",\"number\": \"441632960961\"}," +
                "\"status\": \"completed\",\"direction\": \"outbound\",\"rate\": \"0.39\"," +
                "\"price\": \"23.40\",\"network\": \"65512\"}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        CallInfo callInfo = methodUnderTest.parseResponse(stubResponse);
        assertEquals("63f61863-4a51-4f6b-86e1-46edebcf9356", callInfo.getUuid());
        assertEquals("63f61863-4a51-4f6b-86e1-46edebio0123", callInfo.getConversationUuid());
        assertEquals(CallStatus.COMPLETED, callInfo.getStatus());
        assertEquals(CallDirection.OUTBOUND, callInfo.getDirection());
        assertEquals("65512", callInfo.getNetwork());
        assertEquals("phone", callInfo.getFrom().getType());
        assertEquals("441632960961", callInfo.getFrom().getNumber());
        assertEquals("441632960960", callInfo.getTo().getNumber());
        assertEquals("phone", callInfo.getTo().getType());
    }
}