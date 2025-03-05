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
package com.vonage.client.conversations;

import com.vonage.client.Jsonable;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class SipMachineEventTest extends AbstractEventTest {

    @Test
    public void testParseEmptyEvent() {
        for (var tuple : List.of(
                new ParseEventTuple(SipMachineEvent.class, EventType.SIP_MACHINE, "sip:machine"),
                new ParseEventTuple(SipAmdMachineEvent.class, EventType.SIP_AMD_MACHINE, "sip:amd_machine")
        )) {
            var event = (AbstractSipMachineEvent) parseEvent(
                    tuple.eventTypeEnum(), tuple.clazz(), "{\"type\":\""+tuple.eventTypeStr()+"\"}"
            );
            assertNull(event.getBodyType());
            assertNull(event.getConfidence());
        }
    }

    @Test
    public void testFromJson() {
        final String json = "{\"body\":{\"type\":\"string\",\"confidence\":15}}";
        for (var clazz : List.of(
                SipMachineEvent.class,
                SipAmdMachineEvent.class
        )) {
            var event = Jsonable.fromJson(json, clazz);
            assertNotNull(event);
            assertEquals(clazz, event.getClass());
            assertNotNull(event.getBodyType());
            assertEquals(15, event.getConfidence());
        }
    }
}
