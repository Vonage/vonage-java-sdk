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
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.UUID;

public class RtcAnswerEventTest extends AbstractEventTest {

    @Test
    public void testParseEmptyEvent() {
        var event = parseEvent(EventType.RTC_ANSWER, RtcAnswerEvent.class, "{\"type\":\"rtc:answer\"}");
        assertNull(event.getAnswer());
        assertNull(event.getRtcId());
        assertNull(event.getSessionDestination());
        assertNull(event.getIsFromMb());
    }

    @Test
    public void testFromJsonAllFields() {
        boolean isFromMb = true;
        final String answer = "Hello", rtcId = UUID.randomUUID().toString(), sessionDestination = "127.0.0.1",
                json ="{\"body\":{\"answer\":\"" + answer + "\",\"rtc_id\":\"" + rtcId +
                        "\",\"session_destination\":\"" + sessionDestination + "\",\"isFromMb\":" + isFromMb + "}}";

        var event = Jsonable.fromJson(json, RtcAnswerEvent.class);
        testJsonableBaseObject(event);
        assertEquals(answer, event.getAnswer());
        assertEquals(rtcId, event.getRtcId());
        assertEquals(sessionDestination, event.getSessionDestination());
        assertEquals(isFromMb, event.getIsFromMb());
    }
}
