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


import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.logging.LoggingUtils;
import com.vonage.client.sms.messages.Message;
import com.vonage.client.sms.messages.TextMessage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoggingUtils.class)
public class SmsClientTest {
    private HttpWrapper wrapper;
    private SmsClient client;

    @Before
    public void setUp() throws ParserConfigurationException {
        wrapper = new HttpWrapper(new TokenAuthMethod("not-an-api-key", "secret"));
        client = new SmsClient(wrapper);
    }

    private HttpClient stubHttpClient(int statusCode, String content) throws Exception {
        //Log log = mock(Log.class);
        mockStatic(LoggingUtils.class);
        HttpClient result = mock(HttpClient.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(LoggingUtils.logResponse(any(HttpResponse.class))).thenReturn("response logged");
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        return result;
    }

    @Test
    public void testSubmitMessage() throws Exception {
        wapper.setHttpClient(stubHttpClient(
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
        wapper.setHttpClient(stubHttpClient(500, ""));

        Message message = new TextMessage("TestSender", "not-a-number", "Test");
        try {
            client.submitMessage(message);
            fail("A VonageResponseParseException should be thrown if an HTTP 500 response is received.");
        } catch (VonageResponseParseException nrp) {
            // This is expected
        }
    }

    @Test
    public void testSearchMessagesId() throws Exception {
        wapper.setHttpClient(stubHttpClient(200, "{\n" +
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
        wapper.setHttpClient(stubHttpClient(200, "{\n" +
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
        wapper.setHttpClient(stubHttpClient(200, "{\n" +
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
        wapper.setHttpClient(stubHttpClient(200, "{\n" +
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
