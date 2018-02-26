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
import com.nexmo.client.auth.JWTAuthMethod;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class StopTalkMethodTest {
    private StopTalkMethod endpoint;

    @Before
    public void setUp() {
        this.endpoint = new StopTalkMethod(null);
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = this.endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{JWTAuthMethod.class}, auths);
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest("stop-talk-uuid");
        assertEquals("DELETE", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls/stop-talk-uuid/talk", builder.build().getURI().toString());
    }

    @Test
    public void testCustomBaseUrl() throws Exception {
        this.endpoint.setBaseUrl("https://api.example.com/");
        RequestBuilder builder = this.endpoint.makeRequest("stop-talk-uuid");
        assertEquals("DELETE", builder.getMethod());
        assertEquals("https://api.example.com/v1/calls/stop-talk-uuid/talk", builder.build().getURI().toString());
    }
}