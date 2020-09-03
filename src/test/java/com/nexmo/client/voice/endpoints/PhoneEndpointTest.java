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
package com.nexmo.client.voice.endpoints;

import com.nexmo.client.voice.PhoneEndpoint;

import org.junit.Test;

import static org.junit.Assert.*;


public class PhoneEndpointTest {
    @Test
    public void testConstructor() throws Exception {
        PhoneEndpoint e = new PhoneEndpoint("number", "dtmf");
        assertEquals("number", e.getNumber());
        assertEquals("dtmf", e.getDtmfAnswer());
    }

    @Test
    public void testSetNumber() throws Exception {
        PhoneEndpoint e = new PhoneEndpoint("number", "dtmf");
        e.setNumber("1234");
        assertEquals("1234", e.getNumber());
    }

    @Test
    public void testSetDtmf() throws Exception {
        PhoneEndpoint e = new PhoneEndpoint("number", "dtmf");
        e.setDtmfAnswer("#123");
        assertEquals("#123", e.getDtmfAnswer());
    }

    @Test
    public void testComparison() throws Exception {

        PhoneEndpoint e1 = new PhoneEndpoint("number");
        PhoneEndpoint e2 = new PhoneEndpoint("number");
        assertTrue("Endpoints with the same values should be equal", e1.equals(e2));
        assertTrue("Endpoints with the same values should have the same hash", e1.hashCode() == e2.hashCode());

        e1 = new PhoneEndpoint("number");
        e2 = new PhoneEndpoint("number1");
        assertFalse("Endpoints with different numbers should not be equal",  e1.equals(e2));
        assertFalse("Endpoints with different numbers should have different hashes", e1.hashCode() == e2.hashCode());

        e1 = new PhoneEndpoint("number", "dtmf");
        e2 = new PhoneEndpoint("number", "dtmf1");
        assertFalse("Endpoints with different dtmfAnswers should not be equal",  e1.equals(e2));

        e1 = new PhoneEndpoint("number", "dtmf");
        e2 = null;
        assertFalse("An instance is not equal to null",  e1.equals(e2));

        e1 = new PhoneEndpoint("number", "dtmf");
        Object o = new Object();
        assertFalse("An instance is not equal to a different type.",  e1.equals(o));
    }
}