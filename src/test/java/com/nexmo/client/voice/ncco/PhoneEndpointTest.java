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
package com.nexmo.client.voice.ncco;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhoneEndpointTest {
    @Test
    public void testOnAnswerWithUrlNoRingback() {
        PhoneEndpoint endpoint = PhoneEndpoint.builder("15554441234")
                .number("15554441235")
                .dtmfAnswer("1234")
                .onAnswer("http://example.com")
                .build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441235\",\"dtmfAnswer\":\"1234\",\"onAnswer\":{\"url\":\"http://example.com\"},\"type\":\"phone\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }

    @Test
    public void testAllFields() {
        PhoneEndpoint endpoint = PhoneEndpoint.builder("15554441234")
                .number("15554441235")
                .dtmfAnswer("1234")
                .onAnswer("http://example.com", "https://example.com/ringback.mp3")
                .build();
        ConnectAction connect = ConnectAction.builder(endpoint).build();

        String expectedJson = "[{\"endpoint\":[{\"number\":\"15554441235\",\"dtmfAnswer\":\"1234\",\"onAnswer\":{\"url\":\"http://example.com\",\"ringback\":\"https://example.com/ringback.mp3\"},\"type\":\"phone\"}],\"action\":\"connect\"}]";
        assertEquals(expectedJson, new Ncco(connect).toJson());
    }
}
