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
import com.vonage.client.voice.ncco.RecordingFormat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;

public class AudioRecordDoneEventTest extends AbstractEventTest {

    @Test
    public void testParseEmptyEvent() {
        var event = parseEvent(EventType.AUDIO_RECORD_DONE, AudioRecordDoneEvent.class, "{\"type\":\"audio:record:done\"}");
        assertNull(event.getEventId());
        assertNull(event.getDestinationUrl());
        assertNull(event.getFormat());
        assertNull(event.getRecordingId());
        assertNull(event.getStartTime());
        assertNull(event.getEndTime());
        assertNull(event.getSize());
    }

    @Test
    public void testFromJsonAllFields() {
        final Integer eventId = 123, size = 4096;
        URI destinationUrl = URI.create("https://example.com/recordings/deposit");
        final Instant startTime = Instant.parse("2021-02-03T04:56:28Z"),
                endTime = Instant.parse("2021-02-03T05:17:40Z");
        final String
                recordingId = UUID.randomUUID().toString().replace("-", ""),
                json = "{\"body\":{\"event_id\":" + eventId + ",\"recording_id\":\"" + recordingId +
                "\",\"destination_url\":\"" + destinationUrl + "\",\"format\":\"mp3\",\"start_time\":\"" +
                startTime + "\",\"end_time\":\"" + endTime + "\",\"size\":" + size + "}}";


        var event = Jsonable.fromJson(json, AudioRecordDoneEvent.class);
        testJsonableBaseObject(event);
        assertEquals(eventId, event.getEventId());
        assertEquals(recordingId, event.getRecordingId());
        assertEquals(destinationUrl, event.getDestinationUrl());
        assertEquals(RecordingFormat.MP3, event.getFormat());
        assertEquals(startTime, event.getStartTime());
        assertEquals(endTime, event.getEndTime());
        assertEquals(size, event.getSize());
    }
}
