/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.numbers;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

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
