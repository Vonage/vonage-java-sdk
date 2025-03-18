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
import java.util.Map;

public class AudioRtcEventTest extends AbstractEventTest {

    <E extends AudioRtcEvent, B extends AudioRtcEvent.Builder<E, B>> void testRtcEvent(
            B builder, EventType type) {

        var event = testBaseEvent(type,
                builder.rtcId(randomIdStr),
                Map.of("rtc_id", randomId)
        );
        assertEquals(randomId, event.getRtcId());
    }

    @Test
    public void testRtcEvents() {
        testRtcEvent(AudioMuteOffEvent.builder(), EventType.AUDIO_MUTE_OFF);
        testRtcEvent(AudioMuteOnEvent.builder(), EventType.AUDIO_MUTE_ON);
        testRtcEvent(AudioEarmuffOffEvent.builder(), EventType.AUDIO_EARMUFF_OFF);
        testRtcEvent(AudioEarmuffOnEvent.builder(), EventType.AUDIO_EARMUFF_ON);
    }

    @Test
    public void testParseEmptyRtcEvents() {
        for (var tuple : new ParseEventTuple[] {
                new ParseEventTuple(AudioMuteOffEvent.class, EventType.AUDIO_MUTE_OFF, "audio:mute:off"),
                new ParseEventTuple(AudioMuteOnEvent.class, EventType.AUDIO_MUTE_ON, "audio:mute:on"),
                new ParseEventTuple(AudioEarmuffOffEvent.class, EventType.AUDIO_EARMUFF_OFF, "audio:earmuff:off"),
                new ParseEventTuple(AudioEarmuffOnEvent.class, EventType.AUDIO_EARMUFF_ON, "audio:earmuff:on")
        }) {
            var event = (AudioRtcEvent) parseEvent(
                    tuple.eventTypeEnum(), tuple.clazz(), "{\"body\":{\"rtc_id\":\""+randomIdStr+"\"},\"type\":\""+tuple.eventTypeStr()+"\"}"
            );
            assertEquals(randomId, event.getRtcId());
        }
    }

    @Test
    public void testFromEmptyJson() {
        final String json = "{}";
        for (var clazz : List.of(
                AudioMuteOffEvent.class,
                AudioMuteOnEvent.class,
                AudioEarmuffOffEvent.class,
                AudioEarmuffOnEvent.class
        )) {
            var event = Jsonable.fromJson(json, clazz);
            assertNotNull(event);
            assertNull(event.getRtcId());
        }
    }
}
