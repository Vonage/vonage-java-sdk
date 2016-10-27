package com.nexmo.messaging.sdk;
/*
 * Copyright (c) 2011-2016 Nexmo Inc
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

import com.nexmo.common.NexmoResponseParseException;
import com.nexmo.messaging.sdk.messages.*;
import com.nexmo.messaging.sdk.messages.parameters.ValidityPeriod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NexmoSmsClientTest {
    private NexmoSmsClient client;

    @Before
    public void setUp() throws ParserConfigurationException {
        client = new NexmoSmsClient("not-an-api-key", "secret");
    }

    @Before
    public void testConstructorNullBaseUrl() {
        try {
            client = new NexmoSmsClient(null, "not-an-api-key", "secret", 0, 0, false, null);
            fail("Null baseUrl should raise IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    @Before
    public void testConstructorInsecureBaseUrl() {
        try {
            client = new NexmoSmsClient("http://insecure.example.com/", "not-an-api-key", "secret", 0, 0, false, null);
            fail("Insecure baseUrl should raise IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    private HttpClient stubHttpClient(int statusCode, String content) {
        HttpClient result = mock(HttpClient.class);
        try {
            HttpResponse response = mock(HttpResponse.class);
            StatusLine sl = mock(StatusLine.class);
            HttpEntity entity = mock(HttpEntity.class);

            when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
            when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes("UTF-8")));
            when(sl.getStatusCode()).thenReturn(statusCode);
            when(response.getStatusLine()).thenReturn(sl);
            when(response.getEntity()).thenReturn(entity);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return result;
    }

    @Test
    public void testSubmitMessage() throws IOException, NexmoResponseParseException {
        this.client.setHttpClient(this.stubHttpClient(
                200,
                "<?xml version='1.0' encoding='UTF-8' ?>\n" +
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
                        "</mt-submission-response>"));

        Message message = new TextMessage("TestSender", "not-a-number", "Test");
        try {
            SmsSubmissionResult[] r = client.submitMessage(message);
            assertEquals(r.length, 2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSubmitMessageHttpError() throws IOException, NexmoResponseParseException {
        this.client.setHttpClient(this.stubHttpClient(500, ""));

        Message message = new TextMessage("TestSender", "not-a-number", "Test");
        SmsSubmissionResult[] r = client.submitMessage(message);
        assertEquals(r.length, 1);

        SmsSubmissionResult e = r[0];
        assertEquals(e.getStatus(), SmsSubmissionResult.STATUS_COMMS_FAILURE);
    }

    @Test
    public void testConstructParamsText() {
        Message message = new TextMessage("TestSender", "not-a-number", "Test");
        List<NameValuePair> params = client.constructParams(message, null);

        assertContainsParam(params, "api_key", "not-an-api-key");
        assertContainsParam(params, "api_secret", "secret");
        assertContainsParam(params, "from", "TestSender");
        assertContainsParam(params, "to", "not-a-number");
        assertContainsParam(params, "type", "text");
        assertContainsParam(params, "status-report-req", "false");
        assertContainsParam(params, "text", "Test");
    }

    @Test
    public void testConstructParamsUnicode() {
        Message message = new UnicodeMessage("TestSender", "not-a-number", "Test");
        List<NameValuePair> params = client.constructParams(message, null);

        assertContainsParam(params, "api_key", "not-an-api-key");
        assertContainsParam(params, "api_secret", "secret");
        assertContainsParam(params, "from", "TestSender");
        assertContainsParam(params, "to", "not-a-number");
        assertContainsParam(params, "type", "unicode");
        assertContainsParam(params, "status-report-req", "false");
        assertContainsParam(params, "text", "Test");
    }

    @Test
    public void testConstructParamsBinary() {
        Message message = new BinaryMessage("TestSender", "not-a-number", "abc".getBytes(), "def".getBytes());
        List<NameValuePair> params = client.constructParams(message, null);

        assertContainsParam(params, "api_key", "not-an-api-key");
        assertContainsParam(params, "api_secret", "secret");
        assertContainsParam(params, "from", "TestSender");
        assertContainsParam(params, "to", "not-a-number");
        assertContainsParam(params, "type", "binary");
        assertContainsParam(params, "status-report-req", "false");
        assertContainsParam(params, "udh", "646566");
        assertContainsParam(params, "body", "616263");
    }

    @Test
    public void testConstructParamsWapPush() {
        Message message = new WapPushMessage("TestSender", "not-a-number", "http://the-url", "A Title");
        List<NameValuePair> params = client.constructParams(message, null);

        assertContainsParam(params, "api_key", "not-an-api-key");
        assertContainsParam(params, "api_secret", "secret");
        assertContainsParam(params, "from", "TestSender");
        assertContainsParam(params, "to", "not-a-number");
        assertContainsParam(params, "type", "wappush");
        assertContainsParam(params, "status-report-req", "false");
        assertContainsParam(params, "url", "http://the-url");
        assertContainsParam(params, "title", "A Title");
    }

    @Test
    public void testConstructParamsValidityPeriodTTL() {
        Message message = new BinaryMessage("TestSender", "not-a-number", "abc".getBytes(), "def".getBytes());
        List<NameValuePair> params = client.constructParams(message, new ValidityPeriod(50));

        assertContainsParam(params, "ttl", "50");
    }

    @Test
    public void testConstructParamsValidityPeriodTTLComponents() {
        Message message = new BinaryMessage("TestSender", "not-a-number", "abc".getBytes(), "def".getBytes());
        List<NameValuePair> params = client.constructParams(message, new ValidityPeriod(5, 4, 3));

        assertContainsParam(params, "ttl-hours", "5");
        assertContainsParam(params, "ttl-minutes", "4");
        assertContainsParam(params, "ttl-seconds", "3");
    }

    @Test
    public void testParseResponse() throws NexmoResponseParseException{
        SmsSubmissionResult[] rs = client.parseResponse("<?xml version='1.0' encoding='UTF-8' ?>\n" +
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

    private static void assertContainsParam(List<NameValuePair> params, String key, String value) {
        NameValuePair item = new BasicNameValuePair(key, value);
        assertTrue(
                "" + params + " should contain " + item,
                params.contains(item)
        );
    }
}
