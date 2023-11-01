/*
 *   Copyright 2023 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.sms;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

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
        assertNull(HexUtil.hexToBytes(null));
    }
}
