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
import com.vonage.client.voice.CallDirection;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class CallEventTest extends AbstractEventTest {

    @Test
    public void testParseEmptyEvent() {
        for (var tuple : List.of(
                new ParseEventTuple(RtcAnsweredEvent.class, EventType.RTC_ANSWERED, "rtc:answered"),
                new ParseEventTuple(RtcRingingEvent.class, EventType.RTC_RINGING, "rtc:ringing"),
                new ParseEventTuple(SipAnsweredEvent.class, EventType.SIP_ANSWERED, "sip:answered"),
                new ParseEventTuple(SipRingingEvent.class, EventType.SIP_RINGING, "sip:ringing")
        )) {
            var event = (AbstractCallEvent) parseEvent(
                    tuple.eventTypeEnum(), tuple.clazz(), "{\"type\":\""+tuple.eventTypeStr()+"\"}"
            );
            assertNull(event.getDirection());
        }
    }

    @Test
    public void testFromJson() {
        final String json = "{\"body\":{\"direction\":\"inbound\"}}";
        for (var clazz : List.of(
                RtcAnsweredEvent.class,
                RtcRingingEvent.class,
                SipAnsweredEvent.class,
                SipRingingEvent.class
        )) {
            var event = Jsonable.fromJson(json, clazz);
            assertNotNull(event);
            assertEquals(clazz, event.getClass());
            assertEquals(CallDirection.INBOUND, event.getDirection());
        }
    }
}
