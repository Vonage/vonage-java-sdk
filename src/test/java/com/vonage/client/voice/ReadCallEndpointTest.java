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

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;

import static com.vonage.client.TestUtils.test429;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ReadCallEndpointTest {
    private ReadCallEndpoint method;

    @Before
    public void setUp() throws Exception {
        method = new ReadCallEndpoint(new HttpWrapper());
    }

    @Test
    public void getAcceptableAuthMethods() throws Exception {
        assertArrayEquals(new Class<?>[]{JWTAuthMethod.class}, method.getAcceptableAuthMethods());
    }

    @Test
    public void makeRequest() throws Exception {
        RequestBuilder request = method.makeRequest("abcd-efgh");
        assertEquals("https://api.nexmo.com/v1/calls/abcd-efgh", request.getUri().toString());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), request.getFirstHeader("Accept").getValue());
    }

    @Test
    public void parseResponse() throws Exception {
        HttpResponse stubResponse = TestUtils.makeJsonHttpResponse(200,
                "      {\n" + "        \"uuid\": \"93137ee3-580e-45f7-a61a-e0b5716000ef\",\n"
                        + "        \"status\": \"completed\",\n" + "        \"direction\": \"outbound\",\n"
                        + "        \"rate\": \"0.02400000\",\n" + "        \"price\": \"0.00280000\",\n"
                        + "        \"duration\": \"7\",\n" + "        \"network\": \"23410\",\n"
                        + "        \"conversation_uuid\": \"aa17bd11-c895-4225-840d-30dc38c31e50\",\n"
                        + "        \"start_time\": \"2017-01-13T13:55:02.000Z\",\n"
                        + "        \"end_time\": \"2017-01-13T13:55:09.000Z\",\n" + "        \"to\": {\n"
                        + "          \"type\": \"phone\",\n" + "          \"number\": \"447700900104\"\n"
                        + "        },\n" + "        \"from\": {\n" + "          \"type\": \"phone\",\n"
                        + "          \"number\": \"447700900105\"\n" + "        },\n" + "        \"_links\": {\n"
                        + "          \"self\": {\n"
                        + "            \"href\": \"/v1/calls/93137ee3-580e-45f7-a61a-e0b5716000ef\"\n" + "          }\n"
                        + "        }\n" + "      }\n"
        );
        CallInfo record = method.parseResponse(stubResponse);
        assertEquals("93137ee3-580e-45f7-a61a-e0b5716000ef", record.getUuid());
    }

    @Test
    public void testRequestThrottleResponse() throws Exception {
        test429(new ReadCallEndpoint(null));
    }

    @Test
    public void testDefaultUri() throws Exception {
        RequestBuilder builder = method.makeRequest("call-id");
        assertEquals("GET", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls/call-id", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        ReadCallEndpoint method = new ReadCallEndpoint(wrapper);

        RequestBuilder builder = method.makeRequest("call-id");
        assertEquals("GET", builder.getMethod());
        assertEquals("https://example.com/v1/calls/call-id", builder.build().getURI().toString());
    }
}
