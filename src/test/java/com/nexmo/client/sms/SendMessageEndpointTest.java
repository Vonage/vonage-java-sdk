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
    public void testConstructBaseUri() throws Exception {
        SendMessageEndpoint methodUnderTest = new SendMessageEndpoint(null, "https://example.com");
        assertEquals("https://example.com/sms/xml", methodUnderTest.getUri());
    }

    @Test
    public void testSetUri() throws Exception {
        SendMessageEndpoint methodUnderTest = new SendMessageEndpoint(null);
        assertEquals("https://rest.nexmo.com/sms/xml", methodUnderTest.getUri());
        methodUnderTest.setUri("https://example.com");
        assertEquals("https://example.com/sms/xml", methodUnderTest.getUri());
    }

    @Test
    public void testParseResponse() throws NexmoResponseParseException {
        SmsSubmissionResult[] rs = endpoint.parseResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "<mt-submission-response>\n" +
                "    <messages count='2'>\n" +
                "        <message>\n" +
                "            <to>not-a-number</to>\n" +
                "            <messageId>message-id-1</messageId>\n" +
                "            <status>0</status>\n" +
                "            <remainingBalance>26.43133450</remainingBalance>\n" +
                "            <messagePrice>0.03330000</messagePrice>\n" +
                "            <network>12345</network>\n" +
                "        </message>\n" +
                "        <message>\n" +
                "            <to>not-a-number</to>\n" +
                "            <messageId>message-id-2</messageId>\n" +
                "            <status>0</status>\n" +
                "            <remainingBalance>26.39803450</remainingBalance>\n" +
                "            <messagePrice>0.03330000</messagePrice>\n" +
                "            <network>12345</network>\n" +
                "        </message>\n" +
                "    </messages>\n" +
                "</mt-submission-response>");
        assertEquals(rs.length, 2);

        SmsSubmissionResult r = rs[1];
        assertEquals(r.getDestination(), "not-a-number");
    }

    @Test
    public void testParseResponseInvalidStatus() throws Exception {
        SmsSubmissionResult[] rs = endpoint.parseResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "<mt-submission-response>\n" +
                "    <messages count='2'>\n" +
                "        <message>\n" +
                "            <to>not-a-number</to>\n" +
                "            <messageId>message-id-1</messageId>\n" +
                "            <status>this-should-be-a-number</status>\n" +
                "            <remainingBalance>26.43133450</remainingBalance>\n" +
                "            <messagePrice>0.03330000</messagePrice>\n" +
                "            <network>12345</network>\n" +
                "            <clientRef>abcde</clientRef>\n" +
                "        </message>\n" +
                "        <message>\n" +
                "            <to>not-a-number</to>\n" +
                "            <messageId>message-id-2</messageId>\n" +
                "            <status>0</status>\n" +
                "            <remainingBalance>26.39803450</remainingBalance>\n" +
                "            <messagePrice>0.03330000</messagePrice>\n" +
                "            <network>12345</network>\n" +
                "        </message>\n" +
                "    </messages>\n" +
                "</mt-submission-response>");
        assertEquals(SmsSubmissionResult.STATUS_INTERNAL_ERROR, rs[0].getStatus());
    }

    @Test
    public void testParseResponseStatusMissing() throws Exception {
        try {
            endpoint.parseResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                    "<mt-submission-response>\n" +
                    "    <messages count='1'>\n" +
                    "        <message>\n" +
                    "            <to>not-a-number</to>\n" +
                    "            <messageId>message-id-1</messageId>\n" +
                    "            <remainingBalance>26.43133450</remainingBalance>\n" +
                    "            <messagePrice>0.03330000</messagePrice>\n" +
                    "            <network>12345</network>\n" +
                    "        </message>\n" +
                    "    </messages>\n" +
                    "</mt-submission-response>");
            fail("A missing <status> should result in a NexmoResponseParseException being thrown");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseResponseMissingValues() throws Exception {
        SmsSubmissionResult[] rs = endpoint.parseResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "<mt-submission-response>\n" +
                "    <messages count='1'>\n" +
                "        <message>\n" +
                "            <to></to>\n" +
                "            <messageId></messageId>\n" +
                "            <status></status>\n" +
                "            <remainingBalance></remainingBalance>\n" +
                "            <messagePrice></messagePrice>\n" +
                "            <network></network>\n" +
                "            <errorText></errorText>\n" +
                "            <clientRef></clientRef>\n" +
                "            <errorText></errorText>\n" +
                "        </message>\n" +
                "    </messages>\n" +

                "</mt-submission-response>");
        assertNull(rs[0].getMessageId());
        assertNull(rs[0].getDestination());
        assertEquals(SmsSubmissionResult.STATUS_INTERNAL_ERROR, rs[0].getStatus());
        assertNull(rs[0].getErrorText());
        assertNull(rs[0].getClientReference());
        assertNull(rs[0].getRemainingBalance());
        assertNull(rs[0].getMessagePrice());
        assertNull(rs[0].getNetwork());
    }

    @Test
    public void testParseResponseError() throws Exception {
        SmsSubmissionResult[] rs = endpoint.parseResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "<mt-submission-response>\n" +
                "    <messages count='1'>\n" +
                "        <message>\n" +
                "            <status>6</status>\n" +
                "            <errorText>The message was invalid</errorText>\n" +
                "        </message>\n" +
                "    </messages>\n" +
                "</mt-submission-response>");
        assertEquals(SmsSubmissionResult.STATUS_INVALID_MESSAGE, rs[0].getStatus());
    }

    @Test
    public void testParseResponseUnexpectedNode() throws Exception {
        SmsSubmissionResult[] rs = endpoint.parseResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "<mt-submission-response>\n" +
                "    <messages count='1'>\n" +
                "        <message>\n" +
                "            <status>0</status>\n" +
                "            <to>not-a-number</to>\n" +
                "            <messageId>message-id-1</messageId>\n" +
                "            <remainingBalance>26.43133450</remainingBalance>\n" +
                "            <messagePrice>0.03330000</messagePrice>\n" +
                "            <network>12345</network>\n" +
                "            <WHATISTHIS></WHATISTHIS>\n" +
                "        </message>\n" +
                "    </messages>\n" +
                "</mt-submission-response>");
        assertEquals(SmsSubmissionResult.STATUS_OK, rs[0].getStatus());
    }

    @Test
    public void testParseResponseInvalidNumbers() throws Exception {
        SmsSubmissionResult[] rs = endpoint.parseResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "<mt-submission-response>\n" +
                "    <messages count='1'>\n" +
                "        <message>\n" +
                "            <status>0</status>\n" +
                "            <to>not-a-number</to>\n" +
                "            <messageId>message-id-1</messageId>\n" +
                "            <remainingBalance>NOTANUMBER</remainingBalance>\n" +
                "            <messagePrice>ALSONOTANUMBER</messagePrice>\n" +
                "            <network>12345</network>\n" +
                "        </message>\n" +
                "    </messages>\n" +
                "</mt-submission-response>");
        assertNull(rs[0].getRemainingBalance());
        assertNull(rs[0].getMessagePrice());
    }

    @Test
    public void testParseResponseStatusThrottled() throws Exception {
        SmsSubmissionResult[] rs = endpoint.parseResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "<mt-submission-response>\n" +
                "    <messages count='1'>\n" +
                "        <message>\n" +
                "            <status>1</status>\n" +
                "        </message>\n" +
                "    </messages>\n" +
                "</mt-submission-response>");
        assertEquals(SmsSubmissionResult.STATUS_THROTTLED, rs[0].getStatus());
        assertTrue(rs[0].getTemporaryError());
    }

    @Test
    public void testParseResponseStatusInternalError() throws Exception {
        SmsSubmissionResult[] rs = endpoint.parseResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "<mt-submission-response>\n" +
                "    <messages count='1'>\n" +
                "        <message>\n" +
                "            <status>5</status>\n" +
                "        </message>\n" +
                "    </messages>\n" +
                "</mt-submission-response>");
        assertEquals(SmsSubmissionResult.STATUS_INTERNAL_ERROR, rs[0].getStatus());
        assertTrue(rs[0].getTemporaryError());
    }

    @Test
    public void testParseResponseStatusTooManyBinds() throws Exception {
        SmsSubmissionResult[] rs = endpoint.parseResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "<mt-submission-response>\n" +
                "    <messages count='1'>\n" +
                "        <message>\n" +
                "            <status>10</status>\n" +
                "        </message>\n" +
                "    </messages>\n" +
                "</mt-submission-response>");
        assertEquals(SmsSubmissionResult.STATUS_TOO_MANY_BINDS, rs[0].getStatus());
        assertTrue(rs[0].getTemporaryError());
    }

    @Test
    public void testParseResponseBadXml() throws Exception {
        try {
            SmsSubmissionResult[] rs = endpoint.parseResponse("not-xml");
            fail("Invalid XML should result in a NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
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
        when(authCollection.getAcceptableAuthMethod(any(Set.class))).thenReturn(tokenAuth);
        when(wrapper.getHttpClient()).thenReturn(client);
        when(client.execute(any(HttpUriRequest.class))).thenReturn(
                TestUtils.makeJsonHttpResponse(200, "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                        "<mt-submission-response>\n" +
                        "    <messages count='1'>\n" +
                        "        <message>\n" +
                        "            <status>10</status>\n" +
                        "        </message>\n" +
                        "    </messages>\n" +
                        "</mt-submission-response>")
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
