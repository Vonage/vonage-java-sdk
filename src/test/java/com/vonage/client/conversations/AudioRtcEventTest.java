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
}
