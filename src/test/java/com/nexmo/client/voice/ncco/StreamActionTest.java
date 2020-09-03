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
package com.nexmo.client.voice.ncco;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class StreamActionTest {
    @Test
    public void testBuilderMultipleInstances() {
        StreamAction.Builder builder = StreamAction.builder("http://example.com");
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFields() {
        StreamAction stream = StreamAction.builder("http://example.com")
                .streamUrl("http://example.org")
                .level(0.33f)
                .bargeIn(true)
                .loop(3)
                .build();

        String expectedJson = "[{\"streamUrl\":[\"http://example.org\"],\"level\":0.33,\"bargeIn\":true,\"loop\":3,\"action\":\"stream\"}]";
        assertEquals(expectedJson, new Ncco(stream).toJson());
    }

    @Test
    public void testGetAction() {
        StreamAction stream = StreamAction.builder("http://example.com").build();
        assertEquals("stream", stream.getAction());
    }

    @Test
    public void testStreamUrlField() {
        StreamAction stream = StreamAction.builder("http://example.com").streamUrl("http://example.org").build();

        String expectedJson = "[{\"streamUrl\":[\"http://example.org\"],\"action\":\"stream\"}]";
        assertEquals(expectedJson, new Ncco(stream).toJson());
    }

    @Test
    public void testLevelField() {
        StreamAction stream = StreamAction.builder("http://example.com").level(-0.35f).build();

        String expectedJson = "[{\"streamUrl\":[\"http://example.com\"],\"level\":-0.35,\"action\":\"stream\"}]";
        assertEquals(expectedJson, new Ncco(stream).toJson());
    }

    @Test
    public void testBargeInField() {
        StreamAction stream = StreamAction.builder("http://example.com").bargeIn(true).build();

        String expectedJson = "[{\"streamUrl\":[\"http://example.com\"],\"bargeIn\":true,\"action\":\"stream\"}]";
        assertEquals(expectedJson, new Ncco(stream).toJson());
    }

    @Test
    public void testLoopField() {
        StreamAction stream = StreamAction.builder("http://example.com").loop(3).build();

        String expectedJson = "[{\"streamUrl\":[\"http://example.com\"],\"loop\":3,\"action\":\"stream\"}]";
        assertEquals(expectedJson, new Ncco(stream).toJson());
    }
}
