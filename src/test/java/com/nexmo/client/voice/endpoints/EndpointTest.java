package com.nexmo.client.voice.endpoints;/*
 * Copyright (c) 2011-2016 Nexmo Inc
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

import com.nexmo.client.voice.Endpoint;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class EndpointTest {
    @Before
    public void setUp() {}

    @Test
    public void testConstructor() throws Exception {
        Endpoint e = new Endpoint("number", "dtmf");
        assertEquals("number", e.getNumber());
        assertEquals("dtmf", e.getDtmfAnswer());
    }

    @Test
    public void testSetNumber() throws Exception {
        Endpoint e = new Endpoint("number", "dtmf");
        e.setNumber("1234");
        assertEquals("1234", e.getNumber());
    }

    @Test
    public void testSetDtmf() throws Exception {
        Endpoint e = new Endpoint("number", "dtmf");
        e.setDtmfAnswer("#123");
        assertEquals("#123", e.getDtmfAnswer());
    }
}
