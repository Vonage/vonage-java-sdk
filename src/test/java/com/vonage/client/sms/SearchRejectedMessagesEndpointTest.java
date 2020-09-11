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
package com.vonage.client.sms;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

public class SearchRejectedMessagesEndpointTest {
    private SearchRejectedMessagesEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new SearchRejectedMessagesEndpoint(new HttpWrapper());
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = this.endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{TokenAuthMethod.class}, auths);
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest(new SearchRejectedMessagesRequest(new GregorianCalendar(2017,
                Calendar.OCTOBER,
                22
        ).getTime(), "447700900737"));
        // TODO: Check method and URL are correct:
        assertEquals("GET", builder.getMethod());
        assertThat(builder.build().getURI().toString(), startsWith("https://rest.nexmo.com/search/rejections?"));

        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals(2, params.size());
        assertEquals("2017-10-22", params.get("date"));
        assertEquals("447700900737", params.get("to"));
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(200,
                "{\n" + "  \"count\": 1,\n" + "  \"items\": [\n" + "    {\n" + "      \"account-id\": \"key\",\n"
                        + "      \"from\": \"MyApp\",\n" + "      \"to\": \"123456890\",\n"
                        + "      \"date-received\": \"2012-05-02 16:03:00\",\n" + "      \"error-code\": 9,\n"
                        + "      \"error-code-label\": \"partner quota exceeded -- Your pre-pay account does not have sufficient credit to process this message\"\n"
                        + "    }\n" + "  ]\n" + "}\n"
        );
        SearchRejectedMessagesResponse response = this.endpoint.parseResponse(stub);
        assertEquals(1, response.getCount());
        assertNotNull(response.getItems());
        assertEquals(1, response.getItems().length);
        RejectedMessage m = response.getItems()[0];
        assertEquals("key", m.getAccountId());
        assertEquals("MyApp", m.getFrom());
        assertEquals("123456890", m.getTo());
        assertEquals(new GregorianCalendar(2012, Calendar.MAY, 2, 16, 3, 0).getTime(), m.getDateReceived());
        assertEquals(9, m.getErrorCode().longValue());
        assertEquals(
                "partner quota exceeded -- Your pre-pay account does not have sufficient credit to process this message",
                m.getErrorCodeLabel()
        );
    }

    @Test
    public void testDefaultUri() throws Exception {
        SearchRejectedMessagesRequest request = new SearchRejectedMessagesRequest(new GregorianCalendar(2017,
                Calendar.OCTOBER,
                22
        ).getTime(), "447700900737");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://rest.nexmo.com/search/rejections?date=2017-10-22&to=447700900737",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        SearchRejectedMessagesEndpoint endpoint = new SearchRejectedMessagesEndpoint(wrapper);
        SearchRejectedMessagesRequest request = new SearchRejectedMessagesRequest(new GregorianCalendar(2017,
                Calendar.OCTOBER,
                22
        ).getTime(), "447700900737");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://example.com/search/rejections?date=2017-10-22&to=447700900737", builder.build().getURI().toString());
    }
}
