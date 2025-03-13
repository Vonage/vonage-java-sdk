/*
 *   Copyright 2025 Vonage
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

import com.vonage.client.numbers.UpdateNumberRequest.CallbackType;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class EnumTests {

    @Test
    public void testTypeFromString() {
        assertEquals(Type.LANDLINE, Type.fromString("landline"));
        assertEquals(Type.MOBILE_LVN, Type.fromString("mobile-lvn"));
        assertEquals(Type.LANDLINE_TOLL_FREE, Type.fromString("landline-toll-free"));
        assertEquals(Type.UNKNOWN, Type.fromString("test unknown"));
    }

    @Test
    public void testTypeName() {
        assertEquals("landline", Type.LANDLINE.toString());
        assertEquals("mobile-lvn", Type.MOBILE_LVN.toString());
        assertEquals("landline-toll-free", Type.LANDLINE_TOLL_FREE.toString());
        assertEquals("unknown", Type.UNKNOWN.toString());
        assertNull(Type.fromString(null));
    }

    @Test
    public void testSearchPatternValues() {
        assertEquals(0, SearchPattern.STARTS_WITH.getValue());
        assertEquals(1, SearchPattern.ANYWHERE.getValue());
        assertEquals(2, SearchPattern.ENDS_WITH.getValue());
    }

    @Test
    public void testFeatureFromString() {
        assertEquals(Feature.SMS, Feature.fromString("sms"));
        assertEquals(Feature.MMS, Feature.fromString("Mms"));
        assertEquals(Feature.VOICE, Feature.fromString("vOiCE"));
        assertNull(Feature.fromString(null));
    }

    @Test
    public void testCallbackTypeFromString() {
        assertEquals(CallbackType.SIP, CallbackType.fromString("sip"));
        assertEquals(CallbackType.APP, CallbackType.fromString("App"));
        assertEquals(CallbackType.TEL, CallbackType.fromString("tEL"));
        assertNull(CallbackType.fromString(null));
    }
}
