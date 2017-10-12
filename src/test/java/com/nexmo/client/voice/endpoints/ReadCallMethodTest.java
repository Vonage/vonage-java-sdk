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

import com.nexmo.client.TestUtils;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.voice.CallInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import static com.nexmo.client.TestUtils.test429;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ReadCallMethodTest {
    private ReadCallMethod method;

    @Before
    public void setUp() throws Exception {
        this.method = new ReadCallMethod(null);
    }

    @Test
    public void getAcceptableAuthMethods() throws Exception {
        assertArrayEquals(new Class[]{JWTAuthMethod.class}, this.method.getAcceptableAuthMethods());
    }

    @Test
    public void makeRequest() throws Exception {
        RequestBuilder request = method.makeRequest("abcd-efgh");
        assertEquals("https://api.nexmo.com/v1/calls/abcd-efgh", request.getUri().toString());
    }

    @Test
    public void parseResponse() throws Exception {
        HttpResponse stubResponse = TestUtils.makeJsonHttpResponse(200,
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
                        "      }\n");
        CallInfo record = method.parseResponse(stubResponse);
        assertEquals("93137ee3-580e-45f7-a61a-e0b5716000ef", record.getUuid());
    }

    @Test
    public void testBaseUri() throws Exception {
        method.setBaseUri("http://api.example.com/");
        assertEquals("http://api.example.com/", method.getBaseUri());
    }


    @Test
    public void testRequestThrottleResponse() throws Exception {
        test429(new ReadCallMethod(null));
    }
}
