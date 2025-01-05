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

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import java.util.Map;

public class AudioPlayStatusEventTest extends AbstractEventTest {

    <E extends AudioPlayStatusEvent, B extends AudioPlayStatusEvent.Builder<E, B>> void testStatusEvent(
            B builder, EventType type) {

        var event = testBaseEvent(type,
                builder.playId(randomIdStr),
                Map.of("play_id", randomId)
        );
        assertEquals(randomId, event.getPlayId());
    }

    <E extends AudioPlayStatusEvent> void testStatusEvent(
            EventType eventType, String eventTypeStr, Class<E> eventClass) {

        E event = parseEvent(eventType, eventClass, "{\n" +
            "  \"id\": " + randomEventId + ",\n" +
            "  \"type\": \"audio:play:" + eventTypeStr + "\",\n" +
            "  \"body\": {\n" +
            "    \"play_id\": \"" + randomIdStr + "\"\n" +
            "  },\n" +
            "  \"_links\": {}\n" +
            "}"
        );
        assertEquals(randomId, event.getPlayId());
        assertEquals(randomEventId, event.getId());
    }

    @Test
    public void testAudioPlayStopEvent() {
        testStatusEvent(AudioPlayStopEvent.builder(), EventType.AUDIO_PLAY_STOP);
        testStatusEvent(EventType.AUDIO_PLAY_STOP, "stop", AudioPlayStopEvent.class);
    }

    @Test
    public void testAudioPlayDoneEvent() {
        testStatusEvent(EventType.AUDIO_PLAY_DONE, "done", AudioPlayDoneEvent.class);
    }
}
