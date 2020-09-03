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
package com.nexmo.client.sms;


import org.junit.Test;

import static org.junit.Assert.*;

public class HexUtilTest {
    @Test
    public void testBytesToHex() {
        String result = HexUtil.bytesToHex(new byte[]{0x00, 0x10, 0x7f, 0x70, -1});
        assertEquals("00107F70FF", result);
    }

    @Test
    public void testBytesToHexWithSeparator() {
        String result = HexUtil.bytesToHex(new byte[]{0x00, 0x10, 0x7f, 0x70, -1}, ",");
        assertEquals(",00,10,7F,70,FF", result);
    }

    @Test
    public void testBytesToHexNull() {
        assertEquals("", HexUtil.bytesToHex(null));
    }

    @Test
    public void testHexToBytes() throws Exception {
        assertArrayEquals(new byte[]{0x00, 0x10, 0x7f, 0x70, -1}, HexUtil.hexToBytes("00107F70FF"));
    }

    @Test
    public void testHexToBytesNull() throws Exception {
        assertNull(null, HexUtil.hexToBytes(null));
    }
}
