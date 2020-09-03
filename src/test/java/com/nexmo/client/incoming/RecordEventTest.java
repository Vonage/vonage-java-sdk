/*
 * Copyright (c) 2020 Vonage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.incoming;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

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
        assertEquals("https://api.nexmo.com/media/download?id=aaaaaaaa-bbbb-cccc-dddd-0123456789ab",
                recordEvent.getUrl()
        );
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
