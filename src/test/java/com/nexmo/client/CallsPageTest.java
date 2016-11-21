package com.nexmo.client;
/*
 * Copyright (c) 2011-2016 Nexmo Inc
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.nexmo.client.voice.CallsPage;
import org.junit.Before;
import org.junit.Test;

public class CallsPageTest {
    CallsPage page;

    @Before
    public void setUp() {
        page = CallsPage.fromJson("{\n" +
                "  \"count\": 100,\n" +
                "  \"page_size\": 10,\n" +
                "  \"record_index\": 20,\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"/calls?page_size=10&record_index=20&order=asc\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"_embedded\": {\n" +
                "    \"calls\": [\n" +
                "      {\n" +
                "        \"_links\": {\n" +
                "          \"self\": {\n" +
                "            \"href\": \"/calls/63f61863-4a51-4f6b-86e1-46edebcf9356\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"uuid\": \"63f61863-4a51-4f6b-86e1-46edebcf9356\",\n" +
                "        \"conversation_uuid\": \"63f61863-4a51-4f6b-86e1-46edebio0123\",\n" +
                "        \"to\": [{\n" +
                "          \"type\": \"phone\",\n" +
                "          \"number\": \"441632960960\"\n" +
                "        }],\n" +
                "        \"from\": {\n" +
                "          \"type\": \"phone\",\n" +
                "          \"number\": \"441632960961\"\n" +
                "        },\n" +
                "        \"status\": \"completed\",\n" +
                "        \"direction\": \"outbound\",\n" +
                "        \"rate\": \"0.39\",\n" +
                "        \"price\": \"23.40\",\n" +
                "        \"duration\": \"60\",\n" +
                "        \"start_time\": \"2015-02-04T22:45:00Z\",\n" +
                "        \"end_time\": \"2015-02-04T23:45:00Z\",\n" +
                "        \"network\": \"65512\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void testBasics() {
        assertEquals("/calls?page_size=10&record_index=20&order=asc", page.getLinks().getSelf().getHref());
        assertEquals("441632960960", page.getEmbedded().getCalls()[0].getTo()[0].getNumber());
    }

    @Test
    public void testIterable() throws Exception {
        assertEquals("441632960960", page.iterator().next().getTo()[0].getNumber());
    }
}
