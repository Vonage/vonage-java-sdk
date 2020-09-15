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

public class CallEventTest {
    @Test
    public void testDeserializeCallEvent() {
        String json = "{\n" + "    \"conversation_uuid\": \"CON-4bf66420-d6cb-46e0-9ba9-f7eede9a7301\",\n"
                + "    \"direction\": \"inbound\",\n" + "    \"from\": \"447700900000\",\n"
                + "    \"status\": \"started\",\n" + "    \"timestamp\": \"2018-08-14T11:07:01.284Z\",\n"
                + "    \"to\": \"447700900001\",\n" + "    \"uuid\": \"688fd94bd0e1f59c36a4cbd36312fc28\"\n" + "}";

        CallEvent callEvent = CallEvent.fromJson(json);
        assertEquals("CON-4bf66420-d6cb-46e0-9ba9-f7eede9a7301", callEvent.getConversationUuid());
        assertEquals(CallDirection.INBOUND, callEvent.getDirection());
        assertEquals("447700900000", callEvent.getFrom());
        assertEquals(CallStatus.STARTED, callEvent.getStatus());
        assertEquals("447700900001", callEvent.getTo());
        assertEquals("688fd94bd0e1f59c36a4cbd36312fc28", callEvent.getUuid());

        Calendar calendar = new GregorianCalendar(2018, Calendar.AUGUST, 14, 11, 7, 1);
        calendar.set(Calendar.MILLISECOND, 284);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        assertEquals(calendar.getTime(), callEvent.getTimestamp());

    }
}
