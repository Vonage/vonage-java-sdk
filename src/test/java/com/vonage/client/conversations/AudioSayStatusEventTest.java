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
package com.vonage.client.conversations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import java.util.Map;

public class AudioSayStatusEventTest extends AbstractEventTest {

    <E extends AudioSayStatusEvent, B extends AudioSayStatusEvent.Builder<E, B>> void testStatusEvent(
            B builder, EventType type) {

        var event = testBaseEvent(type,
                builder.sayId(randomIdStr),
                Map.of("say_id", randomId)
        );
        assertEquals(randomId, event.getSayId());
    }

    <E extends AudioSayStatusEvent> void testStatusEvent(
            EventType eventType, String eventTypeStr, Class<E> eventClass) {

        E event = parseEvent(eventType, eventClass, STR."""
            {
                "id": \{randomEventId},
                "type": "audio:say:\{eventTypeStr}",
                "body": {
                   "say_id": "\{randomIdStr}"
                },
                "_links": {}
            }
            """
        );
        assertEquals(randomId, event.getSayId());
        assertEquals(randomEventId, event.getId());
    }

    @Test
    public void testAudioSayStopEvent() {
        testStatusEvent(AudioSayStopEvent.builder(), EventType.AUDIO_SAY_STOP);
        testStatusEvent(EventType.AUDIO_SAY_STOP, "stop", AudioSayStopEvent.class);
    }

    @Test
    public void testAudioSayDoneEvent() {
        testStatusEvent(EventType.AUDIO_SAY_DONE, "done", AudioSayDoneEvent.class);
    }
}
