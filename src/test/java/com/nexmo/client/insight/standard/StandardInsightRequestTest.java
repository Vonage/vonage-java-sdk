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

package com.nexmo.client.insight.standard;

import org.junit.Test;

import static org.junit.Assert.*;

public class StandardInsightRequestTest {
    @Test
    public void testConstructor1() throws Exception {
        StandardInsightRequest request = new StandardInsightRequest("12345");
        assertEquals(request.getNumber(), "12345");
        assertNull(request.getCountry());
        assertNull(request.getCnam());
    }

    @Test
    public void testConstructor2() throws Exception {
        StandardInsightRequest request = new StandardInsightRequest("12345", "GB");
        assertEquals(request.getNumber(), "12345");
        assertEquals(request.getCountry(), "GB");
        assertNull(request.getCnam());
    }

    @Test
    public void testConstructor3() throws Exception {
        StandardInsightRequest request = new StandardInsightRequest("12345", "GB", true);
        assertEquals(request.getNumber(), "12345");
        assertEquals(request.getCountry(), "GB");
        assertTrue(request.getCnam());
    }

    @Test
    public void testEquals() throws Exception {
        assertFalse(new StandardInsightRequest("1234").equals(null));
        assertFalse(new StandardInsightRequest("1234").equals(new Object()));
        StandardInsightRequest sir = new StandardInsightRequest("1234");
        assertTrue(
                "An object should be equal to itself",
                sir.equals(sir));
        {
            StandardInsightRequest si1 = new StandardInsightRequest("1234", "GB", true);
            StandardInsightRequest si2 = new StandardInsightRequest("1234", "GB", true);
            assertTrue(
                    "StandardInsightRequests created with the same values should be equal.",
                    si1.equals(si2));
        }

        assertFalse(
                "Different number should result in non-equals",
                new StandardInsightRequest("1234").equals(new StandardInsightRequest("7891")));

        {
            StandardInsightRequest si1 = new StandardInsightRequest("1234", "GB");
            StandardInsightRequest si2 = new StandardInsightRequest("1234", "US");
            assertFalse(
                    "Different country values should result in non-equals",
                    si1.equals(si2));
        }

        {
            StandardInsightRequest si1 = new StandardInsightRequest("1234", null, true);
            StandardInsightRequest si2 = new StandardInsightRequest("1234", null, false);
            assertFalse(
                    "Different cnam values should result in non-equals",
                    si1.equals(si2));
        }
    }

}
