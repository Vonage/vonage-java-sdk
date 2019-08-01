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


import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.messages.Message;
import com.nexmo.client.sms.messages.TextMessage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SmsClientTest {
    private HttpWrapper wrapper;
    private SmsClient client;

    @Before
    public void setUp() throws ParserConfigurationException {
        wrapper = new HttpWrapper(new TokenAuthMethod("not-an-api-key", "secret"));
        client = new SmsClient(wrapper);
    }

    private HttpClient stubHttpClient(int statusCode, String content) throws Exception {
        HttpClient result = mock(HttpClient.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        return result;
    }

    @Test
    public void testSubmitMessage() throws Exception {
        this.wrapper.setHttpClient(this.stubHttpClient(
                200,
                "{\n" +
                        "  \"message-count\":2,\n" +
                        "  \"messages\":[\n" +
                        "    {\n" +
                        "      \"to\":\"not-a-number\",\n" +
                        "      \"message-id\":\"message-id-1\",\n" +
                        "      \"status\":\"0\",\n" +
                        "      \"remaining-balance\":\"26.43133450\",\n" +
                        "      \"message-price\":\"0.03330000\",\n" +
                        "      \"network\":\"12345\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"to\":\"not-a-number\",\n" +
                        "      \"message-id\":\"message-id-2\",\n" +
                        "      \"status\":\"0\",\n" +
                        "      \"remaining-balance\":\"26.43133450\",\n" +
                        "      \"message-price\":\"0.03330000\",\n" +
                        "      \"network\":\"12345\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"));

        Message message = new TextMessage("TestSender", "not-a-number", "Test");

        SmsSubmissionResponse r = client.submitMessage(message);
        assertEquals(r.getMessageCount(), 2);
        assertEquals(r.getMessages().size(), 2);
    }

    @Test
    public void testSubmitMessageHttpError() throws Exception {
        this.wrapper.setHttpClient(this.stubHttpClient(500, ""));

        Message message = new TextMessage("TestSender", "not-a-number", "Test");
        try {
            client.submitMessage(message);
            fail("A NexmoResponseParseException should be thrown if an HTTP 500 response is received.");
        } catch (NexmoResponseParseException nrp) {
            // This is expected
        }
    }

    @Test
    public void testSearchMessagesId() throws Exception {
        this.wrapper.setHttpClient(this.stubHttpClient(200, "{\n" +
                "  \"count\": 2,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"message-id\": \"00A0B0C0\",\n" +
                "      \"account-id\": \"key\",\n" +
                "      \"network\": \"20810\",\n" +
                "      \"from\": \"MyApp\",\n" +
                "      \"to\": \"123456890\",\n" +
                "      \"body\": \"hello world\",\n" +
                "      \"price\": \"0.04500000\",\n" +
                "      \"date-received\": \"2011-11-25 16:03:00\",\n" +
                "      \"final-status\": \"DELIVRD\",\n" +
                "      \"date-closed\": \"2011-11-25 16:03:00\",\n" +
                "      \"latency\": 11151,\n" +
                "      \"type\": \"MT\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"message-id\": \"00A0B0C1\",\n" +
                "      \"account-id\": \"key\",\n" +
                "      \"network\": \"20810\",\n" +
                "      \"from\": \"MyApp\",\n" +
                "      \"to\": \"123456891\",\n" +
                "      \"body\": \"foo bar\",\n" +
                "      \"price\": \"0.04500000\",\n" +
                "      \"date-received\": \"2011-11-25 17:03:00\",\n" +
                "      \"final-status\": \"DELIVRD\",\n" +
                "      \"date-closed\": \"2011-11-25 18:03:00\",\n" +
                "      \"latency\": 14151,\n" +
                "      \"type\": \"MT\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n"));

            SearchSmsResponse response = client.searchMessages("an-id");
            assertThat(response.getCount(), CoreMatchers.equalTo(2));
    }

    @Test
    public void testSearchMessagesDate() throws Exception {
        this.wrapper.setHttpClient(this.stubHttpClient(200, "{\n" +
                "  \"count\": 2,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"message-id\": \"00A0B0C0\",\n" +
                "      \"account-id\": \"key\",\n" +
                "      \"network\": \"20810\",\n" +
                "      \"from\": \"MyApp\",\n" +
                "      \"to\": \"123456890\",\n" +
                "      \"body\": \"hello world\",\n" +
                "      \"price\": \"0.04500000\",\n" +
                "      \"date-received\": \"2011-11-25 16:03:00\",\n" +
                "      \"final-status\": \"DELIVRD\",\n" +
                "      \"date-closed\": \"2011-11-25 16:03:00\",\n" +
                "      \"latency\": 11151,\n" +
                "      \"type\": \"MT\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"message-id\": \"00A0B0C1\",\n" +
                "      \"account-id\": \"key\",\n" +
                "      \"network\": \"20810\",\n" +
                "      \"from\": \"MyApp\",\n" +
                "      \"to\": \"123456891\",\n" +
                "      \"body\": \"foo bar\",\n" +
                "      \"price\": \"0.04500000\",\n" +
                "      \"date-received\": \"2011-11-25 17:03:00\",\n" +
                "      \"final-status\": \"DELIVRD\",\n" +
                "      \"date-closed\": \"2011-11-25 18:03:00\",\n" +
                "      \"latency\": 14151,\n" +
                "      \"type\": \"MT\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n"));

        SearchSmsResponse response = client.searchMessages(
                new GregorianCalendar(2017, Calendar.OCTOBER, 22).getTime(),
                "447700900983"
        );
        assertThat(response.getCount(), CoreMatchers.equalTo(2));
    }

    @Test
    public void testSearchRejected() throws Exception {
        this.wrapper.setHttpClient(this.stubHttpClient(200, "{\n" +
                "  \"count\": 1,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"account-id\": \"key\",\n" +
                "      \"from\": \"MyApp\",\n" +
                "      \"to\": \"123456890\",\n" +
                "      \"date-received\": \"2012-05-02 16:03:00\",\n" +
                "      \"error-code\": 9,\n" +
                "      \"error-code-label\": \"partner quota exceeded -- Your pre-pay account does not have sufficient credit to process this message\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n"));

        SearchRejectedMessagesResponse response = client.searchRejectedMessages(
                new GregorianCalendar(2017, Calendar.OCTOBER, 22).getTime(),
                "447700900983"
        );
        assertThat(response.getCount(), CoreMatchers.equalTo(1));
    }

    @Test
    public void testSearchSingleMessagesId() throws Exception {
        this.wrapper.setHttpClient(this.stubHttpClient(200, "{\n" +
                "      \"message-id\": \"00A0B0C0\",\n" +
                "      \"account-id\": \"key\",\n" +
                "      \"network\": \"20810\",\n" +
                "      \"from\": \"MyApp\",\n" +
                "      \"to\": \"123456890\",\n" +
                "      \"body\": \"hello world\",\n" +
                "      \"price\": \"0.04500000\",\n" +
                "      \"date-received\": \"2011-11-25 16:03:00\",\n" +
                "      \"final-status\": \"DELIVRD\",\n" +
                "      \"date-closed\": \"2011-11-25 16:03:00\",\n" +
                "      \"latency\": 11151,\n" +
                "      \"type\": \"MT\"\n" +
                "    }"));

        SmsSingleSearchResponse response = client.getSms("an-id");
        assertEquals("00A0B0C0", response.getMessageId());
        assertEquals("key", response.getAccountId());
        assertEquals("20810", response.getNetwork());
        assertEquals("MyApp", response.getFrom());
        assertEquals("123456890", response.getTo());
        assertEquals("hello world", response.getBody());
        assertEquals("0.04500000", response.getPrice());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2011-11-25 16:03:00"), response.getDateReceived());
        assertEquals("DELIVRD", response.getFinalStatus());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2011-11-25 16:03:00"), response.getDateClosed());
        assertEquals(Integer.valueOf(11151), response.getLatency());
        assertEquals("MT", response.getType());
    }
}
