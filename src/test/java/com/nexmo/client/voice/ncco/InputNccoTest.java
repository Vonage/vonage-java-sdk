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

public class InputNccoTest {
    @Test
    public void testToJSON() throws Exception {
        assertEquals("{\"action\":\"input\"}", new InputNcco().toJson());
    }

    @Test
    public void testJSON() throws Exception {
        String json;
        {
            InputNcco ncco = new InputNcco();
            ncco.setEventUrl("https://api.example.com/event");
            ncco.setEventMethod("GET");
            ncco.setMaxDigits(4);
            ncco.setSubmitOnHash(true);
            ncco.setTimeOut(5);

            json = ncco.toJson();
        }

        InputNcco ncco = new ObjectMapper().readValue(json, InputNcco.class);
        assertArrayEquals(new String[]{"https://api.example.com/event"}, ncco.getEventUrl());
        assertEquals("GET", ncco.getEventMethod());
        assertEquals(4, (int)ncco.getMaxDigits());
        assertEquals(true, ncco.getSubmitOnHash());
        assertEquals(5, (int)ncco.getTimeOut());
    }
}