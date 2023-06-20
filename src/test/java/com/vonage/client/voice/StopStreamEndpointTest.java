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
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StopStreamEndpointTest {
    private StopStreamEndpoint method;
    String httpMethod;

    @Before
    public void setUp() throws Exception {
        method = new StopStreamEndpoint(new HttpWrapper());
        httpMethod = "DELETE";
    }

    @Test
    public void makeRequestTest() throws UnsupportedEncodingException {
        RequestBuilder builder = method.makeRequest("63f61863-4a51-4f6b-86e1-46edebcf9356");

        assertEquals(httpMethod, builder.getMethod());
        assertTrue(builder.getUri().toString().contains("63f61863-4a51-4f6b-86e1-46edebcf9356"));
        assertTrue(builder.getUri().toString().contains("stream"));
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
    }

    @Test
    public void parseResponseTest() throws IOException {
        String expectedUuid = "63f61863-4a51-4f6b-86e1-46edebcf9356";
        String expectedMessage = "Stream stopped";

        HttpResponse stubResponse =
                new BasicHttpResponse(
                        new BasicStatusLine(
                                new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );
        String json = "{\"message\":\"Stream stopped\", \"uuid\":\"63f61863-4a51-4f6b-86e1-46edebcf9356\" }";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        StreamResponse response = method.parseResponse(stubResponse);

        assertEquals(expectedMessage, response.getMessage());
        assertEquals(expectedUuid, response.getUuid());

    }

    @Test
    public void testDefaultUri() throws Exception {
        String expectedUri = "https://api.nexmo.com/v1/calls/63f61863-4a51-4f6b-86e1-46edebcf9356/stream";

        RequestBuilder builder = method.makeRequest("63f61863-4a51-4f6b-86e1-46edebcf9356");

        assertEquals(httpMethod, builder.getMethod());
        assertEquals(expectedUri, builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        String uuid = "63f61863-4a51-4f6b-86e1-46edebcf9356";
        String expectedUri = "https://example.com/v1/calls/" + uuid + "/stream";
        String httpMethod = "DELETE";

        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        StopStreamEndpoint method = new StopStreamEndpoint(wrapper);

        RequestBuilder builder = method.makeRequest(uuid);

        assertEquals(httpMethod, builder.getMethod());
        assertEquals(expectedUri, builder.build().getURI().toString());
    }
}
