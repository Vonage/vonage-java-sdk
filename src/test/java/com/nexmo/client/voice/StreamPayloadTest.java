package com.nexmo.client.voice;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
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
public class StreamPayloadTest {
    private StreamPayload payload;

    @Before
    public void setUp() throws Exception {
        payload = new StreamPayload("https://nexmo-community.github.io/ncco-examples/assets/voice_api_audio_streaming.mp3", 1);
    }

    @Test
    public void getStreamUrl() throws Exception {
        assertArrayEquals(new String[]{"https://nexmo-community.github.io/ncco-examples/assets/voice_api_audio_streaming.mp3"}, payload.getStreamUrl());
    }

    @Test
    public void getLoop() throws Exception {
        assertEquals(1, payload.getLoop());
    }

    @Test
    public void toJson() throws Exception {
        String jsonString = "{\"loop\":1,\"stream_url\":[\"https://nexmo-community.github.io/ncco-examples/assets/voice_api_audio_streaming.mp3\"]}";
        assertEquals(jsonString, payload.toJson());
    }

}
