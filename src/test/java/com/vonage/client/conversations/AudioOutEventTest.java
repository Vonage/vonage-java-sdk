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

import com.vonage.client.OrderedJsonMap;
import static com.vonage.client.OrderedJsonMap.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.net.URI;
import java.util.List;

public class AudioOutEventTest extends AbstractEventTest {

    <E extends AudioOutEvent<?>, B extends AudioOutEvent.Builder<E, B>> E testAudioOutEvent(
            EventType type, B builder, OrderedJsonMap bodyFields) {

        boolean queue = true;
        double level = 0.35;
        int loop = 6;

        var event = testBaseEvent(type,
                builder.loop(loop).level(level).queue(queue),
                new OrderedJsonMap(
                        entry("queue", queue),
                        entry("level", level),
                        entry("loop", loop)
                ).addAll(bodyFields)
        );
        assertEquals(loop, event.getLoop());
        assertEquals(level, event.getLevel());
        assertEquals(queue, event.getQueue());
        return event;
    }

    @Test
    public void testAudioPlayEventAllFields() {
        String streamUrl = "ftp://example.com/path/to/audio.mp3";
        var event = testAudioOutEvent(EventType.AUDIO_PLAY,
                AudioPlayEvent.builder().streamUrl(streamUrl),
                new OrderedJsonMap(entry("stream_url", List.of(streamUrl)))
        );
        assertEquals(URI.create(streamUrl), event.getStreamUrl());
        assertNull(event.getPlayId());
    }

    @Test
    public void testAudioSayEventAllFields() {

    }
}
