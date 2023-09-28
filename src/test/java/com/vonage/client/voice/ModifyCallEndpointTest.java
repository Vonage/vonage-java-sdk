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
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ModifyCallEndpointTest {
    private ModifyCallEndpoint method;
    private final String uuid = UUID.randomUUID().toString();

    @BeforeEach
    public void setUp() throws Exception {
        method = new ModifyCallEndpoint(new HttpWrapper());
    }

    @Test
    public void makeRequest() throws Exception {
        for (ModifyCallAction action : ModifyCallAction.values()) {
            RequestBuilder request = method.makeRequest(new ModifyCallRequestWrapper(
                    uuid, new ModifyCallPayload(action)
            ));

            assertEquals("PUT", request.getMethod());
            assertEquals(ContentType.APPLICATION_JSON.getMimeType(), request.getFirstHeader("Content-Type").getValue());
            assertEquals(ContentType.APPLICATION_JSON.getMimeType(), request.getFirstHeader("Accept").getValue());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readValue(request.getEntity().getContent(), JsonNode.class);
            assertEquals(action.name().toLowerCase(), node.get("action").asText());
        }
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
        String expectedUri = "https://example.com/v1/calls/"+uuid;

        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        method = new ModifyCallEndpoint(wrapper);

        RequestBuilder builder = method.makeRequest(new ModifyCallRequestWrapper(
                uuid, new ModifyCallPayload(ModifyCallAction.HANGUP)
        ));
        assertEquals("PUT", builder.getMethod());
        assertEquals(expectedUri, builder.build().getURI().toString());
    }
}
