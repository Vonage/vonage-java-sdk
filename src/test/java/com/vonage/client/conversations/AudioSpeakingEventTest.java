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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.*;
import java.util.List;

public class AudioSpeakingEventTest extends AbstractEventTest {

    @Test
    public void testParseEmptyEvent() {
        for (var tuple : List.of(
                new ParseEventTuple(AudioSpeakingOnEvent.class, EventType.AUDIO_SPEAKING_ON, "audio:speaking:on"),
                new ParseEventTuple(AudioSpeakingOffEvent.class, EventType.AUDIO_SPEAKING_OFF, "audio:speaking:off")
        )) {
            var event = (AbstractAudioSpeakingEvent) parseEvent(
                    tuple.eventTypeEnum(), tuple.clazz(), "{\"body\":{\"channel\":{}},\"type\":\""+tuple.eventTypeStr()+"\"}"
            );
            var channel = event.getChannel();
            assertNotNull(channel);
        }
    }
}
