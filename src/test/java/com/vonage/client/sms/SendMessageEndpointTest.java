/*
 * Copyright (c) 2020 Vonage
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
package com.vonage.client.sms;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.AuthCollection;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.sms.messages.BinaryMessage;
import com.vonage.client.sms.messages.Message;
import com.vonage.client.sms.messages.TextMessage;
import com.vonage.client.sms.messages.WapPushMessage;
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
        endpoint = new SendMessageEndpoint(new HttpWrapper());
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
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "  \"message-count\":2,\n" + "  \"messages\":[\n" + "    {\n"
                        + "      \"to\":\"not-a-number\",\n" + "      \"message-id\":\"message-id-1\",\n"
                        + "      \"status\":\"0\",\n" + "      \"remaining-balance\":\"26.43133450\",\n"
                        + "      \"message-price\":\"0.03330000\",\n" + "      \"network\":\"12345\",\n"
                        + "      \"client-ref\": \"first ref\"\n" + "    },\n" + "    {\n"
                        + "      \"to\":\"not-a-number-2\",\n" + "      \"message-id\":\"message-id-2\",\n"
                        + "      \"status\":\"0\",\n" + "      \"remaining-balance\":\"27.43133450\",\n"
                        + "      \"message-price\":\"0.03430000\",\n" + "      \"network\":\"98765\",\n"
                        + "      \"client-ref\": \"second ref\"\n" + "    }\n" + "  ]\n" + "}"
        ));
        assertEquals(rs.getMessageCount(), 2);
        assertEquals(rs.getMessages().size(), 2);

        SmsSubmissionResponseMessage firstMessage = rs.getMessages().get(0);
        SmsSubmissionResponseMessage secondMessage = rs.getMessages().get(1);

        assertEquals("not-a-number", firstMessage.getTo());
        assertEquals("message-id-1", firstMessage.getId());
        assertEquals(MessageStatus.OK, firstMessage.getStatus());
        assertEquals(new BigDecimal("26.43133450"), firstMessage.getRemainingBalance());
        assertEquals(new BigDecimal("0.03330000"), firstMessage.getMessagePrice());
        assertEquals("12345", firstMessage.getNetwork());
        assertEquals("first ref", firstMessage.getClientRef());

        assertEquals("not-a-number-2", secondMessage.getTo());
        assertEquals("message-id-2", secondMessage.getId());
        assertEquals(MessageStatus.OK, secondMessage.getStatus());
        assertEquals(new BigDecimal("27.43133450"), secondMessage.getRemainingBalance());
        assertEquals(new BigDecimal("0.03430000"), secondMessage.getMessagePrice());
        assertEquals("98765", secondMessage.getNetwork());
        assertEquals("second ref", secondMessage.getClientRef());
    }

    @Test
    public void testParseResponseInvalidStatus() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "  \"message-count\":2,\n" + "  \"messages\":[\n" + "    {\n"
                        + "      \"to\":\"not-a-number\",\n" + "      \"message-id\":\"message-id-1\",\n"
                        + "      \"status\":\"12345\",\n" + "      \"remaining-balance\":\"26.43133450\",\n"
                        + "      \"message-price\":\"0.03330000\",\n" + "      \"network\":\"12345\",\n"
                        + "      \"client-ref\":\"abcde\"\n" + "    },\n" + "    {\n"
                        + "      \"to\":\"not-a-number\",\n" + "      \"message-id\":\"message-id-2\",\n"
                        + "      \"status\":\"0\",\n" + "      \"remaining-balance\":\"26.43133450\",\n"
                        + "      \"message-price\":\"0.03330000\",\n" + "      \"network\":\"12345\"\n" + "    }\n"
                        + "  ]\n" + "}"
        ));
        assertEquals(MessageStatus.UNKNOWN, rs.getMessages().get(0).getStatus());
    }

    @Test
    public void testParseResponseError() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "  \"message-count\":1,\n" + "  \"messages\":[\n" + "    {\n" + "    \"status\":\"6\",\n"
                        + "    \"error-text\": \"The message was invalid\"\n" + "    }\n" + "  ]\n" + "}"
        ));
        assertEquals(MessageStatus.INVALID_MESSAGE, rs.getMessages().get(0).getStatus());
    }

    @Test
    public void testParseResponseUnexpectedNode() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "  \"message-count\":1,\n" + "  \"messages\":[\n" + "    {\n"
                        + "      \"to\":\"not-a-number\",\n" + "      \"message-id\":\"\",\n"
                        + "      \"status\":\"0\",\n" + "      \"remaining-balance\":\"26.43133450\",\n"
                        + "      \"message-price\":\"0.0330000\",\n" + "      \"network\":\"12345\",\n"
                        + "      \"WHAT-IS-THIS\":\"\"\n" + "    }\n" + "  ]\n" + "}"
        ));
        assertEquals(MessageStatus.OK, rs.getMessages().get(0).getStatus());
    }

    @Test
    public void testParseResponseStatusThrottled() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "  \"message-count\":1,\n" + "  \"messages\":[\n" + "    {\n" + "      \"status\":\"1\"\n"
                        + "    }\n" + "  ]\n" + "}"
        ));
        SmsSubmissionResponseMessage r = rs.getMessages().get(0);
        assertEquals(MessageStatus.THROTTLED, r.getStatus());
        assertTrue(r.isTemporaryError());
    }

    @Test
    public void testParseResponseStatusInternalError() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "  \"message-count\":1,\n" + "  \"messages\":[\n" + "    {\n" + "      \"status\":\"5\"\n"
                        + "    }\n" + "  ]\n" + "}"
        ));
        SmsSubmissionResponseMessage r = rs.getMessages().get(0);
        assertEquals(MessageStatus.INTERNAL_ERROR, r.getStatus());
        assertTrue(r.isTemporaryError());
    }

    @Test
    public void testParseResponseStatusTooManyBinds() throws Exception {
        SmsSubmissionResponse rs = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "  \"message-count\":1,\n" + "  \"messages\":[\n" + "    {\n" + "      \"status\":\"10\"\n"
                        + "    }\n" + "  ]\n" + "}"
        ));
        SmsSubmissionResponseMessage r = rs.getMessages().get(0);
        assertEquals(MessageStatus.TOO_MANY_BINDS, r.getStatus());
        assertTrue(r.isTemporaryError());
    }

    private static void assertContainsParam(List<NameValuePair> params, String key, String value) {
        NameValuePair item = new BasicNameValuePair(key, value);
        assertTrue("" + params + " should contain " + item, params.contains(item));
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
        HttpConfig httpConfig = HttpConfig.defaultConfig();
        HttpWrapper wrapper = mock(HttpWrapper.class);
        HttpClient client = mock(HttpClient.class);
        AuthCollection authCollection = mock(AuthCollection.class);
        AuthMethod tokenAuth = new TokenAuthMethod("abcd", "def");

        when(wrapper.getAuthCollection()).thenReturn(authCollection);
        @SuppressWarnings("unchecked") Set<Class> anySet = any(Set.class);
        when(authCollection.getAcceptableAuthMethod(anySet)).thenReturn(tokenAuth);
        when(wrapper.getHttpClient()).thenReturn(client);
        when(wrapper.getHttpConfig()).thenReturn(httpConfig);
        when(client.execute(any(HttpUriRequest.class))).thenReturn(TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "  \"message-count\":1,\n" + "  \"messages\":[\n" + "    {\n" + "      \"status\":\"10\"\n"
                        + "    }\n" + "  ]\n" + "}"
        ));

        ArgumentCaptor<HttpUriRequest> argument = ArgumentCaptor.forClass(HttpUriRequest.class);

        Message message = new TextMessage("TestSender", "not-a-number", "Test", true);
        endpoint = new SendMessageEndpoint(wrapper);
        endpoint.execute(message);

        verify(client).execute(argument.capture());
        if (argument.getValue() instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) argument.getValue()).getEntity();
            assertNotNull(entity);
            assertEquals("application/x-www-form-urlencoded; charset=UTF-8", entity.getContentType().getValue());
        }
    }
}
