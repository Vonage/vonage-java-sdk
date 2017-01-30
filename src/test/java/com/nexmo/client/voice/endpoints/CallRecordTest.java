/*
 * Copyright (c) 2011-2017 Nexmo Inc
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

package com.nexmo.client.voice.endpoints;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.voice.CallDirection;
import com.nexmo.client.voice.CallEndpoint;
import com.nexmo.client.voice.CallRecord;
import com.nexmo.client.voice.CallStatus;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class CallRecordTest {
    @Test
    public void testJson() throws Exception {
        String json = "{\n" +
                "  \"uuid\": \"93137ee3-580e-45f7-a61a-e0b5716000ef\",\n" +
                "  \"status\": \"completed\",\n" +
                "  \"direction\": \"outbound\",\n" +
                "  \"rate\": \"0.02400000\",\n" +
                "  \"price\": \"0.00280000\",\n" +
                "  \"duration\": \"7\",\n" +
                "  \"network\": \"23410\",\n" +
                "  \"conversation_uuid\": \"aa17bd11-c895-4225-840d-30dc38c31e50\",\n" +
                "  \"start_time\": \"2017-01-13T13:55:02.000Z\",\n" +
                "  \"end_time\": \"2017-01-13T13:55:09.000Z\",\n" +
                "  \"to\": {\n" +
                "    \"type\": \"phone\",\n" +
                "    \"number\": \"447700900104\"\n" +
                "  },\n" +
                "  \"from\": {\n" +
                "    \"type\": \"phone\",\n" +
                "    \"number\": \"447700900105\"\n" +
                "  },\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"/v1/calls/93137ee3-580e-45f7-a61a-e0b5716000ef\"\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
        CallRecord record = new ObjectMapper().readValue(json, CallRecord.class);

        assertEquals("93137ee3-580e-45f7-a61a-e0b5716000ef", record.getUuid());
        assertEquals(CallStatus.COMPLETED, record.getStatus());
        assertEquals(CallDirection.OUTBOUND, record.getDirection());
        assertEquals("0.02400000", record.getRate());
        assertEquals("0.00280000", record.getPrice());
        assertEquals(7, (long)record.getDuration());
        assertEquals("23410", record.getNetwork());
        // 2017-01-13T13:55:02.000Z
        Calendar expectedStart = new GregorianCalendar(2017, Calendar.JANUARY, 13 ,13, 55, 2);
        expectedStart.getTime();
        expectedStart.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar actualStart = Calendar.getInstance();
        actualStart.setTimeZone(TimeZone.getTimeZone("UTC"));
        actualStart.setTime(record.getStartTime());

        assertEquals(
                //getTime() prints out a date in the default timezone. In my case EST
                //but we want everything to be in UTC time
                //can't set the default to timezone to UTC, that fixes the symptom, not the cause
                expectedStart,

                //but why does getStartTime() print out in EST? Jackson should use UTC as default
                //it prints out in EST because the test is calling toString() on it?
                //focus on this first
                actualStart);
        // 2017-01-13T13:55:09.000Z
        Calendar expectedEnd = new GregorianCalendar(2017, Calendar.JANUARY, 13 ,13, 55, 9);
        expectedEnd.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar actualEnd = Calendar.getInstance();
        actualEnd.setTimeZone(TimeZone.getTimeZone("UTC"));
        actualEnd.setTime(record.getEndTime());

        assertEquals(
                expectedEnd,
                actualEnd);
        assertEquals(new CallEndpoint("447700900104"), record.getTo());
        assertEquals(new CallEndpoint("447700900105"), record.getFrom());


    }

    @Test
    public void testToString() throws Exception {
        CallRecord record = new CallRecord(new CallEndpoint("447700900104"), new CallEndpoint("447700900105"));
        record.setUuid("93137ee3-580e-45f7-a61a-e0b5716000ef");
        record.setStatus(CallStatus.COMPLETED);
        assertEquals(
                "<CallRecord ID: 93137ee3-580e-45f7-a61a-e0b5716000ef, From: 447700900105, To: 447700900104, Status: completed>",
                record.toString());
    }
}
