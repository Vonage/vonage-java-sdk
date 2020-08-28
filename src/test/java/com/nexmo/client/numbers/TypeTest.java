/*
 * Copyright (c) 2011-2019 Vonage Inc
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
package com.nexmo.client.numbers;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeTest {
    @Test
    public void testFromString() {
        assertEquals(Type.LANDLINE, Type.fromString("landline"));
        assertEquals(Type.MOBILE_LVN, Type.fromString("mobile-lvn"));
        assertEquals(Type.LANDLINE_TOLL_FREE, Type.fromString("landline-toll-free"));
        assertEquals(Type.UNKNOWN, Type.fromString("test unknown"));
    }

    @Test
    public void testName() {
        assertEquals("landline", Type.LANDLINE.getType());
        assertEquals("mobile-lvn", Type.MOBILE_LVN.getType());
        assertEquals("landline-toll-free", Type.LANDLINE_TOLL_FREE.getType());
        assertEquals("unknown", Type.UNKNOWN.getType());
    }
}
