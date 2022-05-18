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
/* Copyright 2020 Vonage
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

public class CreateCallEndpointTest {
    private static final Log LOG = LogFactory.getLog(CreateCallEndpointTest.class);
    private CreateCallEndpoint method;

    @Before
    public void setUp() throws Exception {
        method = new CreateCallEndpoint(new HttpWrapper());
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder request = method.makeRequest(new Call("447700900903",
                "447700900904",
                "https://example.com/answer"
        ));

        assertEquals("POST", request.getMethod());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), request.getFirstHeader("Content-Type").getValue());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), request.getFirstHeader("Accept").getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(request.getEntity().getContent(), JsonNode.class);
        LOG.info(request.getEntity().getContent());
        assertEquals("447700900903", node.get("to").get(0).get("number").asText());
        assertEquals("447700900904", node.get("from").get("number").asText());
        assertEquals("https://example.com/answer", node.get("answer_url").get(0).asText());
    }

    @Test
    public void testParseResponse() throws Exception {

        HttpResponse stubResponse = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("1.1", 1, 1),
                200,
                "OK"
        ));

        String json = " {\"uuid\":\"93137ee3-580e-45f7-a61a-e0b5716000ea\",\"status\":\"started\",\"direction\":\"outbound\",\"conversation_uuid\":\"aa17bd11-c895-4225-840d-30dc78c31e50\"}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        // Execute test call:
        CallEvent callEvent = method.parseResponse(stubResponse);
        assertEquals("93137ee3-580e-45f7-a61a-e0b5716000ea", callEvent.getUuid());
        assertEquals("aa17bd11-c895-4225-840d-30dc78c31e50", callEvent.getConversationUuid());
        assertEquals(CallStatus.STARTED, callEvent.getStatus());
        assertEquals(CallDirection.OUTBOUND, callEvent.getDirection());
    }

    @Test
    public void testRequestThrottleResponse() throws Exception {
        test429(new CreateCallEndpoint(null));
    }

    @Test
    public void testDefaultUri() throws Exception {
        Call request = new Call("447700900903", "447700900904", "https://example.com/answer");

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        CreateCallEndpoint method = new CreateCallEndpoint(wrapper);
        Call request = new Call("447700900903", "447700900904", "https://example.com/answer");

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/v1/calls", builder.build().getURI().toString());
    }
}
