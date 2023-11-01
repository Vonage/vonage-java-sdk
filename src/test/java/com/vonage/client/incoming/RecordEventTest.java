/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.incoming;

import org.junit.jupiter.api.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import static org.junit.jupiter.api.Assertions.*;

public class RecordEventTest {
    @Test
    public void testDeserializeRecordEvent() {
        String json = "{\n" + "  \"start_time\": \"2020-01-01T12:00:00Z\",\n"
                + "  \"recording_url\": \"https://api.nexmo.com/media/download?id=aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n"
                + "  \"size\": 12345,\n" + "  \"recording_uuid\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n"
                + "  \"end_time\": \"2020-01-01T12:01:00Z\",\n"
                + "  \"conversation_uuid\": \"bbbbbbbb-cccc-dddd-eeee-0123456789ab\",\n"
                + "  \"timestamp\": \"2020-01-01T14:00:00.000Z\"\n" + "}";

        RecordEvent recordEvent = RecordEvent.fromJson(json);
        assertEquals("https://api.nexmo.com/media/download?id=aaaaaaaa-bbbb-cccc-dddd-0123456789ab", recordEvent.getUrl());
        assertEquals(12345, recordEvent.getSize());
        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", recordEvent.getUuid());
        assertEquals("bbbbbbbb-cccc-dddd-eeee-0123456789ab", recordEvent.getConversationUuid());

        Calendar startTime = new GregorianCalendar(2020, Calendar.JANUARY, 1, 12, 0, 0);
        startTime.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals(startTime.getTime(), recordEvent.getStartTime());

        Calendar endTime = new GregorianCalendar(2020, Calendar.JANUARY, 1, 12, 1, 0);
        endTime.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals(endTime.getTime(), recordEvent.getEndTime());

        Calendar timestamp = new GregorianCalendar(2020, Calendar.JANUARY, 1, 14, 0, 0);
        timestamp.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals(timestamp.getTime(), recordEvent.getTimestamp());
    }
}
