/*
 *   Copyright 2020 Vonage
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
