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
