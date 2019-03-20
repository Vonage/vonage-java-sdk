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
package com.nexmo.client.sms;

import com.nexmo.client.HttpConfig;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.TestUtils;
import com.nexmo.client.auth.TokenAuthMethod;
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
