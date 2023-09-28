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

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static com.vonage.client.TestUtils.test429;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class ListCallsEndpointTest {
    private static final Log LOG = LogFactory.getLog(ListCallsEndpointTest.class);

    private ListCallsEndpoint method;

    @BeforeEach
    public void setUp() throws Exception {
        method = new ListCallsEndpoint(new HttpWrapper());
    }

    @Test
    public void getAcceptableAuthMethods() throws Exception {
        assertArrayEquals(new Class<?>[]{JWTAuthMethod.class}, method.getAcceptableAuthMethods());
    }

    @Test
    public void makeRequestWithNoFilter() throws Exception {
        RequestBuilder request = method.makeRequest(null);
        assertEquals("GET", request.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls", request.getUri().toString());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), request.getFirstHeader("Accept").getValue());
    }

    @Test
    public void makeRequestWithFilter() throws Exception {
        CallsFilter callsFilter = CallsFilter.builder().pageSize(3).build();
        RequestBuilder request = method.makeRequest(callsFilter);
        assertEquals("GET", request.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls?page_size=3", request.getUri().toString());
    }

    @Test
    public void parseResponse() throws Exception {
        HttpResponse stubResponse = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("1.1", 1, 1),
                200,
                "OK"
        ));

        String json = "{\n" + "  \"page_size\": 10,\n" + "  \"record_index\": 0,\n" + "  \"count\": 2,\n"
                + "  \"_embedded\": {\n" + "    \"calls\": [\n" + "      {\n"
                + "        \"uuid\": \"93137ee3-580e-45f7-a61a-e0b5716000ef\",\n"
                + "        \"status\": \"completed\",\n" + "        \"direction\": \"outbound\",\n"
                + "        \"rate\": \"0.02400000\",\n" + "        \"price\": \"0.00280000\",\n"
                + "        \"duration\": \"7\",\n" + "        \"network\": \"23410\",\n"
                + "        \"conversation_uuid\": \"aa17bd11-c895-4225-840d-30dc38c31e50\",\n"
                + "        \"start_time\": \"2017-01-13T13:55:02.000Z\",\n"
                + "        \"end_time\": \"2017-01-13T13:55:09.000Z\",\n" + "        \"to\": {\n"
                + "          \"type\": \"phone\",\n" + "          \"number\": \"447700900104\"\n" + "        },\n"
                + "        \"from\": {\n" + "          \"type\": \"phone\",\n"
                + "          \"number\": \"447700900105\"\n" + "        },\n" + "        \"_links\": {\n"
                + "          \"self\": {\n"
                + "            \"href\": \"/v1/calls/93137ee3-580e-45f7-a61a-e0b5716000ef\"\n" + "          }\n"
                + "        }\n" + "      },\n" + "      {\n"
                + "        \"uuid\": \"105e02df-940a-466c-b28b-51ae015a9166\",\n"
                + "        \"status\": \"completed\",\n" + "        \"direction\": \"outbound\",\n"
                + "        \"rate\": \"0.02400000\",\n" + "        \"price\": \"0.00280000\",\n"
                + "        \"duration\": \"7\",\n" + "        \"network\": \"23410\",\n"
                + "        \"conversation_uuid\": \"1467b438-f5a8-4937-9a65-e1f946a2f664\",\n"
                + "        \"start_time\": \"2017-01-11T15:03:46.000Z\",\n"
                + "        \"end_time\": \"2017-01-11T15:03:53.000Z\",\n" + "        \"to\": {\n"
                + "          \"type\": \"phone\",\n" + "          \"number\": \"447700900104\"\n" + "        },\n"
                + "        \"from\": {\n" + "          \"type\": \"phone\",\n"
                + "          \"number\": \"447700900105\"\n" + "        },\n" + "        \"_links\": {\n"
                + "          \"self\": {\n"
                + "            \"href\": \"/v1/calls/105e02df-940a-466c-b28b-51ae015a9166\"\n" + "          }\n"
                + "        }\n" + "      }\n" + "    ]\n" + "  },\n" + "  \"_links\": {\n" + "    \"self\": {\n"
                + "      \"href\": \"/v1/calls?page_size=10&record_index=0\"\n" + "    },\n" + "    \"first\": {\n"
                + "      \"href\": \"/v1/calls?page_size=10\"\n" + "    },\n" + "    \"last\": {\n"
                + "      \"href\": \"/v1/calls?page_size=10\"\n" + "    }\n" + "  }\n" + "}\n";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        CallInfoPage page = method.parseResponse(stubResponse);
        assertEquals(2, page.getCount());
        assertEquals(2, page.getEmbedded().getCallInfos().length);
        assertEquals("/v1/calls?page_size=10", page.getLinks().getFirst().getHref());
        assertEquals("/v1/calls?page_size=10", page.getLinks().getLast().getHref());
    }

    @Test
    public void testBadUriThrowsException() throws Exception {
        ListCallsEndpoint method = new ListCallsEndpoint(new HttpWrapper(HttpConfig.builder()
                .baseUri(":this::///isnota_uri")
                .build()));

        try {
            CallsFilter filter = CallsFilter.builder().pageSize(30).build();
            RequestBuilder request = method.makeRequest(filter);
            // Anything past here only executes if our assertion is incorrect:
            LOG.error("SnsRequest URI: " + request.getUri());
            fail("Making a request with a bad URI should throw a VonageUnexpectedException");
        } catch (VonageUnexpectedException nue) {
            // This is expected
        }
    }

    @Test
    public void testRequestThrottleResponse() throws Exception {
        test429(new ListCallsEndpoint(null));
    }

    @Test
    public void testDefaultUri() throws Exception {
        CallsFilter filter = CallsFilter.builder().pageSize(3).build();

        RequestBuilder builder = method.makeRequest(filter);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls?page_size=3", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        ListCallsEndpoint method = new ListCallsEndpoint(wrapper);
        CallsFilter filter = CallsFilter.builder().pageSize(3).build();

        RequestBuilder builder = method.makeRequest(filter);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://example.com/v1/calls?page_size=3", builder.build().getURI().toString());
    }
}
