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
package com.nexmo.client.sns;

import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.sns.response.SnsPublishResponse;
import com.nexmo.client.sns.response.SnsResponse;
import com.nexmo.client.sns.response.SnsSubscribeResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SnsEndpointTest {
    private SnsEndpoint endpoint;

    @Before
    public void setUp() {
        this.endpoint = new SnsEndpoint(null);
    }

    @Test
    public void testBaseUri() throws Exception {
        SnsEndpoint methodUnderTest = new SnsEndpoint(null, "https://example.com");
        assertEquals("https://example.com/sns/xml", methodUnderTest.getUri());
    }

    @Test
    public void testParseSubscribeResponse() throws Exception {
        SnsResponse result = this.endpoint.parseSubmitResponse("<nexmo-sns>\n" +
                "   <command>subscribe</command>\n" +
                "   <resultCode>0</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                "</nexmo-sns>");
        assertEquals(0, result.getResultCode());
        assertEquals("subscribe", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
        assertEquals(SnsSubscribeResponse.class, result.getClass());
        SnsSubscribeResponse subscribeResponse = (SnsSubscribeResponse)result;
        assertEquals("arn:aws:sns:region:num:id", subscribeResponse.getSubscriberArn());
    }

    @Test
    public void testParsePublishResponse() throws Exception {
        SnsResponse result = this.endpoint.parseSubmitResponse("<nexmo-sns>\n" +
                "   <command>publish</command>\n" +
                "   <resultCode>0</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <transactionId>1234</transactionId>\n" +
                "</nexmo-sns>");
        assertEquals(0, result.getResultCode());
        assertEquals("publish", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
        assertEquals(SnsPublishResponse.class, result.getClass());
        SnsPublishResponse publishResponse = (SnsPublishResponse)result;
        assertEquals("1234", publishResponse.getTransactionId());
    }

    @Test
    public void testParseResponseInvalidResultCode() throws Exception {
        SnsResponse result = this.endpoint.parseSubmitResponse("<nexmo-sns>\n" +
                "   <command>subscribe</command>\n" +
                "   <resultCode>non-numeric</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                "</nexmo-sns>");
        assertEquals(SnsResponse.STATUS_INTERNAL_ERROR, result.getResultCode());
        assertEquals("subscribe", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test
    public void testParseResponseNullResultCode() throws Exception {
        SnsResponse result = this.endpoint.parseSubmitResponse("<nexmo-sns>\n" +
                "   <command>subscribe</command>\n" +
                "   <resultCode/>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                "   <transactionId>1234</transactionId>\n" +
                "</nexmo-sns>");
        assertEquals(SnsResponse.STATUS_INTERNAL_ERROR, result.getResultCode());
        assertEquals("subscribe", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test
    public void testParseResponseMissingResultCode() throws Exception {
        try {
            this.endpoint.parseSubmitResponse("<nexmo-sns>\n" +
                    "   <command>subscribe</command>\n" +
                    "   <resultMessage>a result message</resultMessage>\n" +
                    "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                    "   <transactionId>1234</transactionId>\n" +
                    "</nexmo-sns>");
            fail("A missing <resultCode> tag should raise NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseResponseInvalidTagIsIgnored() throws Exception {
        SnsResponse result = this.endpoint.parseSubmitResponse("<nexmo-sns>\n" +
                "   <command>subscribe</command>\n" +
                "   <resultCode>0</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                "   <transactionId>1234</transactionId>\n" +
                "   <whatOnEarthIsThis/>\n" +
                "</nexmo-sns>");
        assertEquals(0, result.getResultCode());
        assertEquals("subscribe", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test
    public void testParseResponseUnparseable() throws Exception {
        try {
            this.endpoint.parseSubmitResponse("not-xml");
            fail("Attempting to parse non-xml should throw NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseDummyCommand() throws Exception {
        try {
            this.endpoint.parseSubmitResponse("<nexmo-sns>\n" +
                    "   <command>dummy command</command>\n" +
                    "   <resultCode>0</resultCode>\n" +
                    "   <resultMessage>a result message</resultMessage>\n" +
                    "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                    "   <transactionId>1234</transactionId>\n" +
                    "   <whatOnEarthIsThis/>\n" +
                    "</nexmo-sns>");
            fail("An invalid command should throw NexmoResponseParseException");
        } catch (NexmoResponseParseException e) {
            // this is expected
        }
    }
}
