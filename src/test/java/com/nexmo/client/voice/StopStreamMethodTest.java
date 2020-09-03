/*
 * Copyright (c) 2020 Vonage
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
package com.nexmo.client.voice;

import com.nexmo.client.HttpConfig;
import com.nexmo.client.HttpWrapper;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
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

public class StopStreamMethodTest {
    private StopStreamMethod method;
    String httpMethod;

    @Before
    public void setUp() throws Exception {
        this.method = new StopStreamMethod(new HttpWrapper());
        httpMethod = "DELETE";
    }

    @Test
    public void makeRequestTest() throws UnsupportedEncodingException {
        RequestBuilder builder = method.makeRequest("63f61863-4a51-4f6b-86e1-46edebcf9356");

        assertEquals(httpMethod, builder.getMethod());
        assertTrue(builder.getUri().toString().contains("63f61863-4a51-4f6b-86e1-46edebcf9356"));
        assertTrue(builder.getUri().toString().contains("stream"));
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
        StopStreamMethod method = new StopStreamMethod(wrapper);

        RequestBuilder builder = method.makeRequest(uuid);

        assertEquals(httpMethod, builder.getMethod());
        assertEquals(expectedUri, builder.build().getURI().toString());
    }
}
