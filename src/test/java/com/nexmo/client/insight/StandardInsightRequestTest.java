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

package com.nexmo.client.insight;

import org.junit.Test;

import static org.junit.Assert.*;

public class StandardInsightRequestTest {
    @Test
    public void testWithNumber() {
        StandardInsightRequest request = StandardInsightRequest.withNumber("12345");
        assertEquals(request.getNumber(), "12345");
        assertNull(request.getCountry());
    }

    @Test
    public void testWithNumberAndCountry() {
        StandardInsightRequest request = StandardInsightRequest.withNumberAndCountry("12345", "GB");
        assertEquals(request.getNumber(), "12345");
        assertEquals(request.getCountry(), "GB");
    }

    @Test
    public void testBuildWithAllFields() {
        StandardInsightRequest request = StandardInsightRequest.builder("12345").country("GB").cnam(true).build();
        assertEquals(request.getNumber(), "12345");
        assertEquals(request.getCountry(), "GB");
        assertTrue(request.getCnam());

        request = StandardInsightRequest.builder("12345").number("98765").country("GB").cnam(true).build();
        assertEquals(request.getNumber(), "98765");
        assertEquals(request.getCountry(), "GB");
        assertTrue(request.getCnam());
    }
}
