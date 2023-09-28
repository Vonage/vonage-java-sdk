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

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

public class StartStreamEndpointTest {
    private final String uuid = UUID.randomUUID().toString();
    private StartStreamEndpoint method;

    @BeforeEach
    public void setUp() throws Exception {
        method = new StartStreamEndpoint(new HttpWrapper());
    }

    @Test
    public void testDefaultUri() throws Exception {
        StreamRequestWrapper request = new StreamRequestWrapper(uuid, new StreamPayload("stream-url", 0, 0d));
        RequestBuilder builder = method.makeRequest(request);
        assertEquals("PUT", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls/"+uuid+"/stream", builder.build().getURI().toString());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        StartStreamEndpoint method = new StartStreamEndpoint(wrapper);
        StreamRequestWrapper request = new StreamRequestWrapper(uuid, new StreamPayload("stream-url", 0, 0d));
        RequestBuilder builder = method.makeRequest(request);
        assertEquals("PUT", builder.getMethod());
        assertEquals("https://example.com/v1/calls/"+uuid+"/stream", builder.build().getURI().toString());
    }
}
