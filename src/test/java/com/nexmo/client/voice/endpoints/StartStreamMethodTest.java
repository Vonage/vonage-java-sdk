/*
 * Copyright (c) 2011-2018 Nexmo Inc
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

import com.nexmo.client.TestUtils;
import com.nexmo.client.voice.StreamRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StartStreamMethodTest {
    private StartStreamMethod endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new StartStreamMethod(null);
    }

    @Test
    public void makeRequest() throws Exception {
        StreamRequest request = new StreamRequest("stream-uuid", "https://mp3.example.com/mystream.mp3", 0);
        RequestBuilder builder = this.endpoint.makeRequest(request);

        assertEquals("PUT", builder.build().getMethod());
        assertEquals("https://api.nexmo.com/v1/calls/stream-uuid/stream", builder.build().getURI().toString());

        // TODO: Test JSON representation here.
    }

    @Test
    public void customBaseUrl() throws Exception {
        this.endpoint.setBaseUrl("https://api.example.com/");

        StreamRequest request = new StreamRequest("stream-uuid", "https://mp3.example.com/mystream.mp3", 0);
        RequestBuilder builder = this.endpoint.makeRequest(request);

        assertEquals("PUT", builder.build().getMethod());
        assertEquals("https://api.example.com/v1/calls/stream-uuid/stream", builder.build().getURI().toString());

        // TODO: Test JSON representation here.
    }

    @Test
    @Ignore
    public void parseResponse() throws Exception {
        // TODO: Test response parsing here.
        fail();
    }
}