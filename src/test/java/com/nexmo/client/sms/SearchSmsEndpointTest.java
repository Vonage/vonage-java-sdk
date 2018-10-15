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
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

public class SearchSmsEndpointTest {
    private SmsSearchEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new SmsSearchEndpoint(new HttpWrapper());
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = this.endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{TokenAuthMethod.class}, auths);
    }

    @Test
    public void testMakeSingleIdRequest() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest(new SmsIdSearchRequest("one-id"));
        assertEquals("GET", builder.getMethod());
        assertThat(builder.build().getURI().toString(), startsWith("https://rest.nexmo.com/search/messages?"));

        Map<String, List<String>> params = TestUtils.makeFullParameterMap(builder.getParameters());
        assertThat(params.size(), equalTo(1));
        List<String> ids = params.get("ids");
        assertNotNull(ids);
        assertEquals(1, ids.size());
        assertEquals("one-id", ids.get(0));
    }

    @Test
    public void testMakeMultipleIdRequest() throws Exception {
        SmsIdSearchRequest request = new SmsIdSearchRequest("one-id");
        request.addId("two-id");
        RequestBuilder builder = this.endpoint.makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertThat(builder.build().getURI().toString(), startsWith("https://rest.nexmo.com/search/messages?"));

        Map<String, List<String>> params = TestUtils.makeFullParameterMap(builder.getParameters());
        assertThat(params.size(), equalTo(1));
        List<String> ids = params.get("ids");
        assertNotNull(ids);
        assertEquals(2, ids.size());
        assertEquals("one-id", ids.get(0));
        assertEquals("two-id", ids.get(1));
    }

    @Test
    public void testMakeDateRequest() throws Exception {
        SmsDateSearchRequest request = new SmsDateSearchRequest(new GregorianCalendar(
                2017,
                GregorianCalendar.SEPTEMBER,
                22
        ).getTime(), "447700900510");
        RequestBuilder builder = this.endpoint.makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertThat(builder.build().getURI().toString(), startsWith("https://rest.nexmo.com/search/messages?"));

        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertThat(params.size(), equalTo(2));
        assertThat(params.get("date"), equalTo("2017-09-22"));
        assertThat(params.get("to"), equalTo("447700900510"));
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "  \"count\": 2,\n" + "  \"items\": [\n" + "    {\n" + "      \"message-id\": \"00A0B0C0\",\n"
                        + "      \"account-id\": \"key\",\n" + "      \"network\": \"20810\",\n"
                        + "      \"from\": \"MyApp\",\n" + "      \"to\": \"123456890\",\n"
                        + "      \"body\": \"hello world\",\n" + "      \"price\": \"0.04500000\",\n"
                        + "      \"date-received\": \"2011-11-25 16:03:00\",\n"
                        + "      \"final-status\": \"DELIVRD\",\n" + "      \"date-closed\": \"2011-11-25 16:03:00\",\n"
                        + "      \"latency\": 11151,\n" + "      \"type\": \"MT\"\n" + "    },\n" + "    {\n"
                        + "      \"message-id\": \"00A0B0C1\",\n" + "      \"account-id\": \"key\",\n"
                        + "      \"network\": \"20810\",\n" + "      \"from\": \"MyApp\",\n"
                        + "      \"to\": \"123456891\",\n" + "      \"body\": \"foo bar\",\n"
                        + "      \"price\": \"0.04500000\",\n" + "      \"date-received\": \"2011-11-25 17:03:00\",\n"
                        + "      \"final-status\": \"DELIVRD\",\n" + "      \"date-closed\": \"2011-11-25 18:03:00\",\n"
                        + "      \"latency\": 14151,\n" + "      \"type\": \"MT\"\n" + "    }\n" + "  ]\n" + "}\n"
        );
        SearchSmsResponse response = this.endpoint.parseResponse(stub);

        assertThat(response.getCount(), equalTo(2));

        SmsDetails details = response.getItems()[0];
        assertEquals("00A0B0C0", details.getMessageId());
        assertEquals("key", details.getAccountId());
        assertEquals("20810", details.getNetwork());
        assertEquals("MyApp", details.getFrom());
        assertEquals("123456890", details.getTo());
        assertEquals("hello world", details.getBody());
        assertEquals("0.04500000", details.getPrice());
        assertEquals(new GregorianCalendar(2011, Calendar.NOVEMBER, 25, 16, 03, 00).getTime(),
                details.getDateReceived()
        );
        assertEquals("DELIVRD", details.getFinalStatus());
        assertEquals(new GregorianCalendar(2011, Calendar.NOVEMBER, 25, 16, 03, 00).getTime(), details.getDateClosed());
        assertEquals(11151, details.getLatency().longValue());
        assertEquals("MT", details.getType());
    }

    @Test
    public void testDefaultUri() throws Exception {
        SmsIdSearchRequest request = new SmsIdSearchRequest("one-id");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://rest.nexmo.com/search/messages?ids=one-id", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(new HttpConfig.Builder().baseUri("https://example.com").build());
        SmsSearchEndpoint endpoint = new SmsSearchEndpoint(wrapper);
        SmsIdSearchRequest request = new SmsIdSearchRequest("one-id");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://example.com/search/messages?ids=one-id", builder.build().getURI().toString());
    }
}
