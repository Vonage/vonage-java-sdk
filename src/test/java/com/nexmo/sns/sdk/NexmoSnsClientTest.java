package com.nexmo.sns.sdk;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.nexmo.sns.sdk.request.SubscribeRequest;
import com.nexmo.sns.sdk.response.SnsServiceResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class NexmoSnsClientTest {
    private NexmoSnsClient client;

    @Before
    public void setUp() throws Exception {
        this.client = new NexmoSnsClient("not-api-key", "not-api-secret");
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
    public void testConstructorWithNullBaseUrl() throws Exception {
        try {
            new NexmoSnsClient(null, "api-key", "api-secret", 0, 0);
            fail("A null baseUrl should result in an IllegalArgumentException");
        } catch (IllegalArgumentException exc) {
            // expected
        }
    }

    @Test
    public void testConstructorWithHttpUrl() throws Exception {
        try {
            new NexmoSnsClient("http://very.insecure.example.com/", "api-key", "api-secret", 0, 0);
            fail("An http baseUrl should result in an IllegalArgumentException");
        } catch (IllegalArgumentException exc) {
            // expected
        }
    }

    @Test
    public void testSubmit() throws Exception {
        this.client.setHttpClient(this.stubHttpClient(200, "<nexmo-sns>\n" +
                "   <command>dummy command</command>\n" +
                "   <resultCode>0</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                "   <transactionId>1234</transactionId>\n" +
                "</nexmo-sns>"));
        SnsServiceResult result = this.client.submit(new SubscribeRequest(
                "arn:aws:sns:region:num:id",
                "447777111222"
        ));
        assertEquals(0, result.getResultCode());
        assertEquals("a result message", result.getResultMessage());
        assertEquals("1234", result.getTransactionId());
    }

    @Test
    public void testSubmitWithInvalidResponse() throws Exception {
        this.client.setHttpClient(this.stubHttpClient(500, "<nexmo-sms/>"));
        SnsServiceResult result = this.client.submit(new SubscribeRequest(
                "arn:aws:sns:region:num:id",
                "447777111222"
        ));
        assertEquals(SnsServiceResult.STATUS_COMMS_FAILURE, result.getResultCode());
    }

    @Test
    public void testParseResponse() throws Exception {
        SnsServiceResult result = this.client.parseSubmitResponse("<nexmo-sns>\n" +
                "   <command>dummy command</command>\n" +
                "   <resultCode>0</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                "   <transactionId>1234</transactionId>\n" +
                "</nexmo-sns>");
        assertEquals(0, result.getResultCode());
        assertEquals("dummy command", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
        assertEquals("arn:aws:sns:region:num:id", result.getSubscriberArn());
        assertEquals("1234", result.getTransactionId());
    }

    @Test
    public void testParseResponseInvalidResultCode() throws Exception {
        SnsServiceResult result = this.client.parseSubmitResponse("<nexmo-sns>\n" +
                "   <command>dummy command</command>\n" +
                "   <resultCode>non-numeric</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                "   <transactionId>1234</transactionId>\n" +
                "</nexmo-sns>");
        assertEquals(SnsServiceResult.STATUS_INTERNAL_ERROR, result.getResultCode());
        assertEquals("dummy command", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
        assertEquals("arn:aws:sns:region:num:id", result.getSubscriberArn());
        assertEquals("1234", result.getTransactionId());
    }

    @Test
    public void testParseResponseNullResultCode() throws Exception {
        SnsServiceResult result = this.client.parseSubmitResponse("<nexmo-sns>\n" +
                "   <command>dummy command</command>\n" +
                "   <resultCode/>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                "   <transactionId>1234</transactionId>\n" +
                "</nexmo-sns>");
        assertEquals(SnsServiceResult.STATUS_INTERNAL_ERROR, result.getResultCode());
        assertEquals("dummy command", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
        assertEquals("arn:aws:sns:region:num:id", result.getSubscriberArn());
        assertEquals("1234", result.getTransactionId());
    }

    @Test
    public void testParseResponseMissingResultCode() throws Exception {
        try {
            SnsServiceResult result = this.client.parseSubmitResponse("<nexmo-sns>\n" +
                    "   <command>dummy command</command>\n" +
                    "   <resultMessage>a result message</resultMessage>\n" +
                    "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                    "   <transactionId>1234</transactionId>\n" +
                    "</nexmo-sns>");
            fail();
        } catch (Exception e) {
            // this is expected
        }
    }

    @Test
    public void testParseResponseInvalidTagIsIgnored() throws Exception {
        SnsServiceResult result = this.client.parseSubmitResponse("<nexmo-sns>\n" +
                "   <command>dummy command</command>\n" +
                "   <resultCode>0</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                "   <transactionId>1234</transactionId>\n" +
                "   <whatOnEarthIsThis/>\n" +
                "</nexmo-sns>");
        assertEquals(0, result.getResultCode());
        assertEquals("dummy command", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
        assertEquals("arn:aws:sns:region:num:id", result.getSubscriberArn());
        assertEquals("1234", result.getTransactionId());
    }

    @Test
    public void testParseResponseUnparseable() throws Exception {
        try {
            SnsServiceResult result = this.client.parseSubmitResponse("not-xml");
            fail();
        } catch (Exception e) {
            // this is expected
        }
    }
}
