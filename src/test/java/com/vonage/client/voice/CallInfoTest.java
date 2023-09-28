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
package com.vonage.client.voice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import static org.junit.jupiter.api.Assertions.*;

public class CallInfoTest {
    final String jsonWithPlaceholder = "{\n" +
            "  \"uuid\": \"93137ee3-580e-45f7-a61a-e0b5716000ef\",\n" +
            "  \"status\": \"PLACEHOLDER\",\n" +
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
        CallInfo record = new ObjectMapper().readValue(json, CallInfo.class);

        assertEquals("93137ee3-580e-45f7-a61a-e0b5716000ef", record.getUuid());
        assertEquals(CallStatus.COMPLETED, record.getStatus());
        assertEquals("outbound", record.getDirection().toString());
        assertEquals("0.02400000", record.getRate());
        assertEquals("0.00280000", record.getPrice());
        assertEquals(7, (long) record.getDuration());
        assertEquals("23410", record.getNetwork());
        // 2017-01-13T13:55:02.000Z
        Calendar expectedStart = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        expectedStart.set(2017, Calendar.JANUARY, 13, 13, 55, 2);
        expectedStart.set(Calendar.MILLISECOND, 0);
        assertEquals(expectedStart.getTime(), record.getStartTime());
        // 2017-01-13T13:55:09.000Z
        Calendar expectedEnd = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        expectedEnd.set(2017, Calendar.JANUARY, 13, 13, 55, 9);
        expectedEnd.set(Calendar.MILLISECOND, 0);
        assertEquals(expectedEnd.getTime(), record.getEndTime());
        assertEquals(new PhoneEndpoint("447700900104"), record.getTo());
        assertEquals(new PhoneEndpoint("447700900105"), record.getFrom());
    }

    @Test
    public void testDeserializeUnknownEnumsFallbackToUnknown() throws Exception {
        String json = "{\n" +
                "  \"uuid\": \"93137ee3-580e-45f7-a61a-e0b5716000ef\",\n" +
                "  \"status\": \"test-unknown-call-status\",\n" +
                "  \"direction\": \"test-unknown-direction\",\n" +
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
        CallInfo record = new ObjectMapper().readValue(json, CallInfo.class);

        assertEquals(CallStatus.UNKNOWN, record.getStatus());
        assertEquals(CallDirection.UNKNOWN, record.getDirection());
    }


    @Test
    public void testStatusStarted() throws Exception {
        testStatus("started", CallStatus.STARTED);
    }

    @Test
    public void testStatusRinging() throws Exception {
        testStatus("ringing", CallStatus.RINGING);
    }

    @Test
    public void testStatusAnswered() throws Exception {
        testStatus("answered", CallStatus.ANSWERED);
    }

    @Test
    public void testStatusMachine() throws Exception {
        testStatus("machine", CallStatus.MACHINE);
    }

    @Test
    public void testStatusCompleted() throws Exception {
        testStatus("completed", CallStatus.COMPLETED);
    }

    @Test
    public void testStatusTimeout() throws Exception {
        testStatus("timeout", CallStatus.TIMEOUT);
    }

    @Test
    public void testStatusFailed() throws Exception {
        testStatus("failed", CallStatus.FAILED);
    }

    @Test
    public void testStatusRejected() throws Exception {
        testStatus("rejected", CallStatus.REJECTED);
    }

    @Test
    public void testStatusBusy() throws Exception {
        testStatus("busy", CallStatus.BUSY);
    }

    @Test
    public void testStatusCancelled() throws Exception {
        testStatus("cancelled", CallStatus.CANCELLED);
    }

    public void testStatus(String value, CallStatus expectedValue) throws Exception {
        CallInfo record = new ObjectMapper().readValue(jsonWithPlaceholder.replaceFirst("PLACEHOLDER", value),
                CallInfo.class);
        assertEquals(record.getStatus(), expectedValue);
    }

    @Test
    public void testToString() throws Exception {
        CallInfo record = new CallInfo(new PhoneEndpoint("447700900104"), new PhoneEndpoint("447700900105"));
        record.setUuid("93137ee3-580e-45f7-a61a-e0b5716000ef");
        record.setStatus(CallStatus.COMPLETED);
        assertEquals("<CallInfo ID: 93137ee3-580e-45f7-a61a-e0b5716000ef, From: 447700900105, To: 447700900104, Status: completed>", record.toString());
    }
}
