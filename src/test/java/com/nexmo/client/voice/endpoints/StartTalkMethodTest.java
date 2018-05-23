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

import com.nexmo.client.voice.TalkRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StartTalkMethodTest {
    StartTalkMethod endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new StartTalkMethod(null);
    }

    @Test
    public void makeRequest() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest(new TalkRequest("talk-id", "some text"));
        assertEquals("PUT", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls/talk-id/talk", builder.build().getURI().toString());
    }

    @Test
    public void customBaseUrl() throws Exception {
        this.endpoint.setBaseUrl("https://api.example.com/");
        RequestBuilder builder = this.endpoint.makeRequest(new TalkRequest("talk-id", "some text"));
        assertEquals("PUT", builder.getMethod());
        assertEquals("https://api.example.com/v1/calls/talk-id/talk", builder.build().getURI().toString());
    }
}