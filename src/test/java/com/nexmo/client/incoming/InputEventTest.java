/*
 * Copyright (c) 2011-2018 Nexmo Inc
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
import static org.junit.Assert.assertTrue;

public class InputEventTest {
    @Test
    public void testDeserializeInputEvent() {
        String json = "{\n" + "  \"uuid\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n"
                + "  \"conversation_uuid\": \"bbbbbbbb-cccc-dddd-eeee-0123456789ab\",\n" + "  \"timed_out\": true,\n"
                + "  \"dtmf\": \"1234\",\n" + "  \"timestamp\": \"2020-01-01T14:00:00.000Z\"\n" + "}";

        InputEvent inputEvent = InputEvent.fromJson(json);
        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", inputEvent.getUuid());
        assertEquals("bbbbbbbb-cccc-dddd-eeee-0123456789ab", inputEvent.getConversationUuid());
        assertTrue(inputEvent.isTimedOut());
        assertEquals("1234", inputEvent.getDtmf());

        Calendar calendar = new GregorianCalendar(2020, Calendar.JANUARY, 1, 14, 0, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        assertEquals(calendar.getTime(), inputEvent.getTimestamp());
    }
}
