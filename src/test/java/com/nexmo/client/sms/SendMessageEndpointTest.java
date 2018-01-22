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
import com.nexmo.client.TestUtils;
import com.nexmo.client.auth.AuthCollection;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.messages.BinaryMessage;
import com.nexmo.client.sms.messages.Message;
import com.nexmo.client.sms.messages.TextMessage;
import com.nexmo.client.sms.messages.WapPushMessage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.xml.parsers.ParserConfigurationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SendMessageEndpointTest {
    private SendMessageEndpoint endpoint;

    @Before
    public void setUp() throws ParserConfigurationException {
        endpoint = new SendMessageEndpoint(null);
    }

    @Test
    public void testConstructParamsText() throws Exception {
        Message message = new TextMessage("TestSender", "not-a-number", "Test");
        List<NameValuePair> params = endpoint.makeRequest(message).getParameters();

        assertContainsParam(params, "from", "TestSender");
        assertContainsParam(params, "to", "not-a-number");
        assertContainsParam(params, "type", "text");
        assertMissingParam(params, "status-report-req");
        assertContainsParam(params, "text", "Test");
    }

    @Test
    public void testStatusReportRequiredSetter() throws Exception {
        Message message = new TextMessage("TestSender", "not-a-number", "Test");
        message.setStatusReportRequired(true);
        List<NameValuePair> params = endpoint.makeRequest(message).getParameters();

        assertContainsParam(params, "from", "TestSender");
        assertContainsParam(params, "to", "not-a-number");
        assertContainsParam(params, "type", "text");
        assertContainsParam(params, "status-report-req", "1");
        assertContainsParam(params, "text", "Test");
    }

    @Test
    public void testConstructParamsUnicode() throws Exception {
        Message message = new TextMessage("TestSender", "not-a-number", "Test", true);

        RequestBuilder requestBuilder = endpoint.makeRequest(message);
        List<NameValuePair> params = requestBuilder.getParameters();

        assertContainsParam(params, "from", "TestSender");
        assertContainsParam(params, "to", "not-a-number");
        assertContainsParam(params, "type", "unicode");
        assertMissingParam(params, "status-report-req");
        assertContainsParam(params, "text", "Test");
    }

    @Test
    public void testConstructParamsBinary() throws Exception {
        Message message = new BinaryMessage("TestSender", "not-a-number", "abc".getBytes(), "def".getBytes());
        List<NameValuePair> params = endpoint.makeRequest(message).getParameters();

        assertContainsParam(params, "from", "TestSender");
        assertContainsParam(params, "to", "not-a-number");
        assertContainsParam(params, "type", "binary");
        assertMissingParam(params, "status-report-req");
        assertContainsParam(params, "udh", "646566");
        assertContainsParam(params, "body", "616263");
    }

    @Test
    public void testConstructParamsWapPush() throws Exception {
        Message message = new WapPushMessage("TestSender", "not-a-number", "http://the-url", "A Title");
        List<NameValuePair> params = endpoint.makeRequest(message).getParameters();

        assertContainsParam(params, "from", "TestSender");
        assertContainsParam(params, "to", "not-a-number");
        assertContainsParam(params, "type", "wappush");
        assertMissingParam(params, "status-report-req");
        assertContainsParam(params, "url", "http://the-url");
        assertContainsParam(params, "title", "A Title");
    }

    @Test
    public void testConstructParamsValidityPeriodTTL() throws Exception {
        Message message = new BinaryMessage("TestSender", "not-a-number", "abc".getBytes(), "def".getBytes());
        message.setTimeToLive(50L);
        List<NameValuePair> params = endpoint.makeRequest(message).getParameters();

        assertContainsParam(params, "ttl", "50");
    }

    @Test
    public void testParseResponse() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, "{\n" +
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
        assertEquals(rs.getMessageCount(), 2);
        assertEquals(rs.getMessages().size(), 2);

        SmsSubmissionResponseMessage r = rs.getMessages().iterator().next();
        assertEquals(r.getTo(), "not-a-number");
    }

    @Test
    public void testParseResponseInvalidStatus() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200,"{\n" +
                "  \"message-count\":2,\n" +
                "  \"messages\":[\n" +
                "    {\n" +
                "      \"to\":\"not-a-number\",\n" +
                "      \"message-id\":\"message-id-1\",\n" +
                "      \"status\":\"this-should-be-a-number\",\n" +
                "      \"remaining-balance\":\"26.43133450\",\n" +
                "      \"message-price\":\"0.03330000\",\n" +
                "      \"network\":\"12345\",\n" +
                "      \"client-ref\":\"abcde\"\n" +
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
        assertEquals(MessageStatus.STATUS_INTERNAL_ERROR, rs.getMessages().iterator().next().getStatus());
    }

    @Test
    public void testParseResponseStatusMissing() throws Exception {
        try {
            endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200,"{\n" +
                    "  \"message-count\":1,\n" +
                    "  \"messages\":[\n" +
                    "    {\n" +
                    "      \"to\":\"not-a-number\",\n" +
                    "      \"message-id\":\"message-id-1\",\n" +
                    "      \"remaining-balance\":\"26.43133450\",\n" +
                    "      \"message-price\":\"0.03330000\",\n" +
                    "      \"network\":\"12345\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}"));
            fail("A missing status should result in a NexmoResponseParseException being thrown");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseResponseMissingValues() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200,"{\n" +
                "  \"message-count\":1,\n" +
                "  \"messages\":[\n" +
                "    {\n" +
                "      \"to\":\"\",\n" +
                "      \"message-id\":\"\",\n" +
                "      \"status\":\"\",\n" +
                "      \"remaining-balance\":\"\",\n" +
                "      \"message-price\":\"\",\n" +
                "      \"network\":\"\",\n" +
                "      \"error-text\": \"\",\n" +
                "      \"client-ref\": \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"));

        SmsSubmissionResponseMessage r = rs.getMessages().iterator().next();
        assertNull(r.getId());
        assertNull(r.getTo());
        assertEquals(MessageStatus.STATUS_INTERNAL_ERROR, r.getStatus());
        assertNull(r.getErrorText());
        assertNull(r.getClientRef());
        assertNull(r.getRemainingBalance());
        assertNull(r.getMessagePrice());
        assertNull(r.getNetwork());
    }

    @Test
    public void testParseResponseError() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200,"{\n" +
                "  \"message-count\":1,\n" +
                "  \"messages\":[\n" +
                "    {\n" +
                "    \"status\":\"6\",\n" +
                "    \"error-text\": \"The message was invalid\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"));
        assertEquals(MessageStatus.STATUS_INVALID_MESSAGE, rs.getMessages().iterator().next().getStatus());
    }

    @Test
    public void testParseResponseUnexpectedNode() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200,"{\n" +
                "  \"message-count\":1,\n" +
                "  \"messages\":[\n" +
                "    {\n" +
                "      \"to\":\"not-a-number\",\n" +
                "      \"message-id\":\"\",\n" +
                "      \"status\":\"0\",\n" +
                "      \"remaining-balance\":\"26.43133450\",\n" +
                "      \"message-price\":\"0.0330000\",\n" +
                "      \"network\":\"12345\",\n" +
                "      \"WHAT-IS-THIS\":\"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"));
        assertEquals(MessageStatus.STATUS_OK, rs.getMessages().iterator().next().getStatus());
    }

    @Test
    public void testParseResponseInvalidRemainingBalance() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200,"{\n" +
                "  \"message-count\":1,\n" +
                "  \"messages\":[\n" +
                "    {\n" +
                "      \"to\":\"not-a-number\",\n" +
                "      \"message-id\":\"\",\n" +
                "      \"status\":\"0\",\n" +
                "      \"remaining-balance\":\"not-a-number\",\n" +
                "      \"message-price\":\"0.0330000\",\n" +
                "      \"network\":\"12345\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"));

        SmsSubmissionResponseMessage r = rs.getMessages().iterator().next();
        assertNull(r.getRemainingBalance());
        assertEquals(new BigDecimal("0.0330000"), r.getMessagePrice());
    }

    @Test
    public void testParseResponseInvalidMessagePriceBalance() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200,"{\n" +
                "  \"message-count\":1,\n" +
                "  \"messages\":[\n" +
                "    {\n" +
                "      \"to\":\"not-a-number\",\n" +
                "      \"message-id\":\"\",\n" +
                "      \"status\":\"0\",\n" +
                "      \"remaining-balance\":\"26.43133450\",\n" +
                "      \"message-price\":\"not-a-number\",\n" +
                "      \"network\":\"12345\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"));

        SmsSubmissionResponseMessage r = rs.getMessages().iterator().next();
        assertEquals(new BigDecimal("26.43133450"), r.getRemainingBalance());
        assertNull(r.getMessagePrice());
    }

    @Test
    public void testParseResponseStatusThrottled() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200,"{\n" +
                "  \"message-count\":1,\n" +
                "  \"messages\":[\n" +
                "    {\n" +
                "      \"status\":\"1\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"));
        SmsSubmissionResponseMessage r = rs.getMessages().iterator().next();
        assertEquals(MessageStatus.STATUS_THROTTLED, r.getStatus());
        assertTrue(r.getTemporaryError());
    }

    @Test
    public void testParseResponseStatusInternalError() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, "{\n" +
                "  \"message-count\":1,\n" +
                "  \"messages\":[\n" +
                "    {\n" +
                "      \"status\":\"5\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"));
        SmsSubmissionResponseMessage r = rs.getMessages().iterator().next();
        assertEquals(MessageStatus.STATUS_INTERNAL_ERROR, r.getStatus());
        assertTrue(r.getTemporaryError());
    }

    @Test
    public void testParseResponseStatusTooManyBinds() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, "{\n" +
                "  \"message-count\":1,\n" +
                "  \"messages\":[\n" +
                "    {\n" +
                "      \"status\":\"10\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"));
        SmsSubmissionResponseMessage r = rs.getMessages().iterator().next();
        assertEquals(MessageStatus.STATUS_TOO_MANY_BINDS, r.getStatus());
        assertTrue(r.getTemporaryError());
    }

    private static void assertContainsParam(List<NameValuePair> params, String key, String value) {
        NameValuePair item = new BasicNameValuePair(key, value);
        assertTrue(
                "" + params + " should contain " + item,
                params.contains(item)
        );
    }

    private static void assertMissingParam(List<NameValuePair> params, String key) {
        for (NameValuePair pair : params) {
            if (pair.getName().equals(key)) {
                fail("" + params + " should not contain " + key);
            }
        }
    }

    @Test
    public void testEntityEncoding() throws Exception {
        HttpWrapper wrapper = mock(HttpWrapper.class);
        HttpClient client = mock(HttpClient.class);
        AuthCollection authCollection = mock(AuthCollection.class);
        AuthMethod tokenAuth = new TokenAuthMethod("abcd", "def");

        when(wrapper.getAuthCollection()).thenReturn(authCollection);
        @SuppressWarnings("unchecked")
        Set<Class> anySet = any(Set.class);
        when(authCollection.getAcceptableAuthMethod(anySet)).thenReturn(tokenAuth);
        when(wrapper.getHttpClient()).thenReturn(client);
        when(client.execute(any(HttpUriRequest.class))).thenReturn(
                TestUtils.makeJsonHttpResponse(200, "{\n" +
                        "  \"message-count\":1,\n" +
                        "  \"messages\":[\n" +
                        "    {\n" +
                        "      \"status\":\"10\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
        );

        ArgumentCaptor<HttpUriRequest> argument = ArgumentCaptor.forClass(HttpUriRequest.class);

        Message message = new TextMessage("TestSender", "not-a-number", "Test", true);
        endpoint = new SendMessageEndpoint(wrapper);
        endpoint.execute(message);

        verify(client).execute(argument.capture());
        if (argument.getValue() instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) argument.getValue()).getEntity();
            assertNotNull(entity);
            assertEquals("application/x-www-form-urlencoded; charset=UTF-8",
                    entity.getContentType().getValue());
        }
    }
}
