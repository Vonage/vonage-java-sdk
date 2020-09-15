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
package com.vonage.client.voice;

import com.vonage.client.VonageUnexpectedException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CallInfoPageTest {
    private CallInfoPage page;

    @Before
    public void setUp() {
        page = CallInfoPage.fromJson("{\n" +
                "  \"page_size\": 10,\n" +
                "  \"record_index\": 0,\n" +
                "  \"count\": 1,\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"/v1/calls?page_size=10&record_index=20&order=asc\"\n" +
                "    },\n" +
                "    \"first\": {\n" +
                "      \"href\": \"/v1/calls?page_size=10\"\n" +
                "    },\n" +
                "    \"last\": {\n" +
                "      \"href\": \"/v1/calls?page_size=10\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"_embedded\": {\n" +
                "    \"calls\": [\n" +
                "      {\n" +
                "        \"uuid\": \"1452dad1-b27b-4e71-a90f-b18af2656949\",\n" +
                "        \"status\": \"completed\",\n" +
                "        \"direction\": \"outbound\",\n" +
                "        \"rate\": \"0.02400000\",\n" +
                "        \"price\": \"0.00280000\",\n" +
                "        \"duration\": \"7\",\n" +
                "        \"network\": \"23410\",\n" +
                "        \"conversation_uuid\": \"879067dc-0370-44af-98bf-b6d11beb4229\",\n" +
                "        \"start_time\": \"2016-12-09T13:30:46.000Z\",\n" +
                "        \"end_time\": \"2016-12-09T13:30:53.000Z\",\n" +
                "        \"to\": {\n" +
                "          \"type\": \"phone\",\n" +
                "          \"number\": \"447700900549\"\n" +
                "        },\n" +
                "        \"from\": {\n" +
                "          \"type\": \"phone\",\n" +
                "          \"number\": \"447700900236\"\n" +
                "        },\n" +
                "        \"_links\": {\n" +
                "          \"self\": {\n" +
                "            \"href\": \"/v1/calls/1452dad1-b27b-4e71-a90f-b18af2656949\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}\n");
    }

    @Test
    public void testFailedUnmarshal() throws Exception {
        try {
            CallInfoPage.fromJson("Notvalidjson");
            fail("Parsing invalid JSON should raise a VonageUnexpectedException");
        } catch (VonageUnexpectedException nue) {
            // This is expected.
        }

    }

    @Test
    public void testBasics() {
        assertEquals("/v1/calls?page_size=10&record_index=20&order=asc", page.getLinks().getSelf().getHref());
        assertEquals("447700900549", page.getEmbedded().getCallInfos()[0].getTo().toLog());
        assertEquals(10, page.getPageSize());
        assertEquals(0, page.getRecordIndex());
    }

    @Test
    public void testIterable() throws Exception {
        assertEquals("447700900549", page.iterator().next().getTo().toLog());
    }
}