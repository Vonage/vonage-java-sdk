package com.nexmo.client.voice.endpoints;

import com.nexmo.client.voice.StreamResponse;
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
public class StopStreamMethodTest {
    private static final Log LOG = LogFactory.getLog(StopStreamMethodTest.class);

    @Test
    public void makeRequest() throws Exception {
        StopStreamMethod methodUnderTest = new StopStreamMethod(null);

        // Execute test call:
        RequestBuilder request = methodUnderTest.makeRequest("aaaaaaaa-bbbb-cccc-dddd-0123456789ab");

        assertEquals("DELETE", request.getMethod());
        assertEquals("application/json", request.getFirstHeader("Content-Type").getValue());
    }

    @Test
    public void testBaseUri() throws Exception {
        StopStreamMethod methodUnderTest = new StopStreamMethod(null, "https://example.com");
        assertEquals("https://example.com/v1/calls", methodUnderTest.getUri());
    }

    @Test
    public void parseResponse() throws Exception {
        StopStreamMethod methodUnderTest = new StopStreamMethod(null);

        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );

        String json = "{\"message\":\"Stream stopped\",\"uuid\":\"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        // Execute test call:
        StreamResponse streamResponse = methodUnderTest.parseResponse(stubResponse);
        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", streamResponse.getUuid());
        assertEquals("Stream stopped", streamResponse.getMessage());
    }

}
