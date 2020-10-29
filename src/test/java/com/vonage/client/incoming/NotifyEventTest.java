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

import com.vonage.client.VonageUnexpectedException;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class NotifyEventTest {

    @Test
    public void testDeserializeFromJson_success() {
        String json = "{\n"
                + "  \"payload\": {\"key2\":23,\"key1\":\"value1\"},\n"
                + "  \"conversation_uuid\": \"bbbbbbbb-cccc-dddd-eeee-0123456789ab\",\n"
                + "  \"timestamp\": \"2020-10-01T12:00:00.000Z\"\n" + "}";

        NotifyEvent notifyEvent = NotifyEvent.fromJson(json);

        assertEquals("bbbbbbbb-cccc-dddd-eeee-0123456789ab", notifyEvent.getConversationUuid());

        Map<String, Object> testPayload = new HashMap<>();
        testPayload.put("key1", "value1");
        testPayload.put("key2", 23);
        assertEquals(testPayload, notifyEvent.getPayload());

        Calendar timestamp = new GregorianCalendar(2020, Calendar.OCTOBER, 1, 12, 0, 0);
        timestamp.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals(timestamp.getTime(), notifyEvent.getTimestamp());
        System.out.println(notifyEvent.toString());
    }

    @Test
    public void testDeserializeFromJson_fail() {
        String json = "test json";
        try {
            NotifyEvent.fromJson(json);
        } catch (VonageUnexpectedException e) {
            assertEquals("Failed to convert NotifyEvent from json.", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        String notifyEventString = "NotifyEvent{conversationUuid=\'bbbbbbbb-cccc-dddd-eeee-0123456789ab\',"
                + " timestamp=null, payload=null}";
        NotifyEvent notifyEvent = new NotifyEvent();
        notifyEvent.setConversationUuid("bbbbbbbb-cccc-dddd-eeee-0123456789ab");
        assertEquals(notifyEventString, notifyEvent.toString());
    }
}
