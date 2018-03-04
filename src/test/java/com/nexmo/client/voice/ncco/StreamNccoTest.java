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
package com.nexmo.client.voice.ncco;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class StreamNccoTest {
    @Test
    public void testToJson() throws Exception {
        assertEquals(
                "{\"action\":\"stream\",\"streamUrl\":[\"https://api.example.com/stream\"]}",
                new StreamNcco("https://api.example.com/stream").toJson());
    }

    @Test
    public void testJson() throws Exception {
        String json;
        {
            StreamNcco ncco = new StreamNcco("https://api.example.com/stream");
            ncco.setStreamUrl("https://api.example.com/stream2");
            ncco.setLevel(0.5f);
            ncco.setBargeIn(true);
            ncco.setLoop(3);

            json = ncco.toJson();
        }

        StreamNcco ncco = new ObjectMapper().readValue(json, StreamNcco.class);
        assertEquals("https://api.example.com/stream2", ncco.getStreamUrl());
        assertEquals(0.5f, (float)ncco.getLevel(), 0.001f);
        assertEquals(true, ncco.getBargeIn());
        assertEquals(3, (int)ncco.getLoop());
    }
}