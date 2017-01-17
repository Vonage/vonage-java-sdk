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

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.voice.CallRecordPage;
import com.nexmo.client.voice.CallsFilter;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ListCallsMethodTest {
    ListCallsMethod method;

    @Before
    public void setUp() throws Exception {
        method = new ListCallsMethod(null);
    }

    @Test
    public void getAcceptableAuthMethods() throws Exception {
        assertArrayEquals(
                new Class[]{JWTAuthMethod.class},
                method.getAcceptableAuthMethods());
    }

    @Test
    public void makeRequestWithNoFilter() throws Exception {
        HttpUriRequest request = method.makeRequest(null);
        assertEquals("GET", request.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls", request.getURI().toString());
    }

    @Test
    public void makeRequestWithFilter() throws Exception {
        CallsFilter callsFilter = new CallsFilter();
        callsFilter.setPageSize(3);
        HttpUriRequest request = method.makeRequest(callsFilter);
        assertEquals("GET", request.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls?page_size=3", request.getURI().toString());
    }

    @Test
    public void parseResponse() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(null);
        CreateCallMethod methodUnderTest = new CreateCallMethod(wrapper);

        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );

        // TODO: The following deep inspection may be better implemented under CallEventTest
        String json = "{\n" +
                "  \"page_size\": 10,\n" +
                "  \"record_index\": 0,\n" +
                "  \"count\": 2,\n" +
                "  \"_embedded\": {\n" +
                "    \"calls\": [\n" +
                "      {\n" +
                "        \"uuid\": \"93137ee3-580e-45f7-a61a-e0b5716000ef\",\n" +
                "        \"status\": \"completed\",\n" +
                "        \"direction\": \"outbound\",\n" +
                "        \"rate\": \"0.02400000\",\n" +
                "        \"price\": \"0.00280000\",\n" +
                "        \"duration\": \"7\",\n" +
                "        \"network\": \"23410\",\n" +
                "        \"conversation_uuid\": \"aa17bd11-c895-4225-840d-30dc38c31e50\",\n" +
                "        \"start_time\": \"2017-01-13T13:55:02.000Z\",\n" +
                "        \"end_time\": \"2017-01-13T13:55:09.000Z\",\n" +
                "        \"to\": {\n" +
                "          \"type\": \"phone\",\n" +
                "          \"number\": \"447700900104\"\n" +
                "        },\n" +
                "        \"from\": {\n" +
                "          \"type\": \"phone\",\n" +
                "          \"number\": \"447700900105\"\n" +
                "        },\n" +
                "        \"_links\": {\n" +
                "          \"self\": {\n" +
                "            \"href\": \"/v1/calls/93137ee3-580e-45f7-a61a-e0b5716000ef\"\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"uuid\": \"105e02df-940a-466c-b28b-51ae015a9166\",\n" +
                "        \"status\": \"completed\",\n" +
                "        \"direction\": \"outbound\",\n" +
                "        \"rate\": \"0.02400000\",\n" +
                "        \"price\": \"0.00280000\",\n" +
                "        \"duration\": \"7\",\n" +
                "        \"network\": \"23410\",\n" +
                "        \"conversation_uuid\": \"1467b438-f5a8-4937-9a65-e1f946a2f664\",\n" +
                "        \"start_time\": \"2017-01-11T15:03:46.000Z\",\n" +
                "        \"end_time\": \"2017-01-11T15:03:53.000Z\",\n" +
                "        \"to\": {\n" +
                "          \"type\": \"phone\",\n" +
                "          \"number\": \"447700900104\"\n" +
                "        },\n" +
                "        \"from\": {\n" +
                "          \"type\": \"phone\",\n" +
                "          \"number\": \"447700900105\"\n" +
                "        },\n" +
                "        \"_links\": {\n" +
                "          \"self\": {\n" +
                "            \"href\": \"/v1/calls/105e02df-940a-466c-b28b-51ae015a9166\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"/v1/calls?page_size=10&record_index=0\"\n" +
                "    },\n" +
                "    \"first\": {\n" +
                "      \"href\": \"/v1/calls?page_size=10\"\n" +
                "    },\n" +
                "    \"last\": {\n" +
                "      \"href\": \"/v1/calls?page_size=10\"\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        CallRecordPage page = method.parseResponse(stubResponse);
        assertEquals(2, page.getCount());
        assertEquals(2, page.getEmbedded().getCallRecords().length);
        assertEquals("/v1/calls?page_size=10", page.getLinks().getFirst().getHref());
        assertEquals("/v1/calls?page_size=10", page.getLinks().getLast().getHref());

    }

}