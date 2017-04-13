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
package com.nexmo.client.insight.advanced;

import org.junit.Test;

import static org.junit.Assert.*;

public class AdvancedInsightRequestTest {
    @Test
    public void testConstructor1() throws Exception {
        AdvancedInsightRequest request = new AdvancedInsightRequest("12345");
        assertEquals(request.getNumber(), "12345");
        assertNull(request.getCountry());
    }

    @Test
    public void testConstructor2() throws Exception {
        AdvancedInsightRequest request = new AdvancedInsightRequest("12345", "GB");
        assertEquals(request.getNumber(), "12345");
        assertEquals(request.getCountry(), "GB");
    }

    @Test
    public void testConstructor3() throws Exception {
        AdvancedInsightRequest request = new AdvancedInsightRequest("12345", "GB", "123.123.123.123");
        assertEquals(request.getNumber(), "12345");
        assertEquals(request.getCountry(), "GB");
        assertEquals(request.getIpAddress(), "123.123.123.123");
    }

    @Test
    public void testEquals() throws Exception {
        assertFalse(new AdvancedInsightRequest("1234").equals(null));
        assertFalse(new AdvancedInsightRequest("1234").equals(new Object()));
        AdvancedInsightRequest bir = new AdvancedInsightRequest("1234");
        assertTrue(bir.equals(bir));
        assertTrue(new AdvancedInsightRequest("1234").equals(new AdvancedInsightRequest("1234")));
        assertFalse(new AdvancedInsightRequest("1234").equals(new AdvancedInsightRequest("7890")));
        assertFalse(new AdvancedInsightRequest("1234", "GB", "123.123.123.123").equals(
                new AdvancedInsightRequest("1234", "GB", "123.123.123.124")));

        {
            AdvancedInsightRequest req1 = new AdvancedInsightRequest(
                    "1234", "GB", "123.123.123.123", true);
            AdvancedInsightRequest req2 = new AdvancedInsightRequest(
                    "1234", "GB", "123.123.123.123", false);
            assertFalse(
                    "Differing cnam values should result in non-equals",
                    req1.equals(req2)
            );
        }
    }
}
