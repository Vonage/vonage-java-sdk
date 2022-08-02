/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.numbers;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import static com.vonage.client.TestUtils.test429;
import static org.junit.Assert.assertEquals;


public class ListNumbersEndpointTest {
    private ListNumbersEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        endpoint = new ListNumbersEndpoint(new HttpWrapper());
    }

    @Test
    public void makeRequest() throws Exception {
        ListNumbersFilter filter = new ListNumbersFilter();
        filter.setIndex(10);
        filter.setSize(20);
        filter.setPattern("234");
        filter.setSearchPattern(SearchPattern.STARTS_WITH);
        RequestBuilder request = endpoint.makeRequest(filter);

        assertEquals("GET", request.getMethod());
        Map<String, String> params = TestUtils.makeParameterMap(request.getParameters());
        assertEquals("234", params.get("pattern"));
        assertEquals("0", params.get("search_pattern"));
        assertEquals("10", params.get("index"));
        assertEquals("20", params.get("size"));
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), request.getFirstHeader("Accept").getValue());
    }

    @Test
    public void parseResponse() throws Exception {
        HttpResponse stubResponse = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("1.1", 1, 1),
                200,
                "OK"
        ));

        String json = "{\n" + "  \"count\": 1,\n" + "  \"numbers\": [\n" + "    {\n" + "      \"country\": \"GB\",\n"
                + "      \"msisdn\": \"447700900000\",\n" + "      \"moHttpUrl\": \"https://example.com/mo\",\n"
                + "      \"type\": \"mobile-lvn\",\n" + "      \"features\": [\n" + "        \"VOICE\",\n"
                + "        \"SMS\"\n" + "      ],\n" + "      \"voiceCallbackType\": \"app\",\n"
                + "      \"voiceCallbackValue\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" + "    }\n" + "  ]\n"
                + "}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        ListNumbersResponse response = endpoint.parseResponse(stubResponse);
        assertEquals(1, response.getCount());
    }

    @Test
    public void testRequestThrottleResponse() throws Exception {
        test429(new ListNumbersEndpoint(null));
    }

    @Test
    public void testDefaultUri() throws Exception {
        ListNumbersFilter filter = new ListNumbersFilter();
        filter.setIndex(10);
        filter.setSize(20);
        filter.setPattern("234");
        filter.setSearchPattern(SearchPattern.STARTS_WITH);

        RequestBuilder builder = endpoint.makeRequest(filter);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://rest.nexmo.com/account/numbers?index=10&size=20&pattern=234&search_pattern=0",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        ListNumbersEndpoint endpoint = new ListNumbersEndpoint(wrapper);
        ListNumbersFilter filter = new ListNumbersFilter();
        filter.setIndex(10);
        filter.setSize(20);
        filter.setPattern("234");
        filter.setSearchPattern(SearchPattern.STARTS_WITH);

        RequestBuilder builder = endpoint.makeRequest(filter);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "https://example.com/account/numbers?index=10&size=20&pattern=234&search_pattern=0",
                builder.build().getURI().toString()
        );
    }
}
