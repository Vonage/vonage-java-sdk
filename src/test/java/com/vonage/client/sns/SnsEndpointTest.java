/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.sns;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.sns.request.SnsPublishRequest;
import com.vonage.client.sns.request.SnsRequest;
import com.vonage.client.sns.response.SnsPublishResponse;
import com.vonage.client.sns.response.SnsResponse;
import com.vonage.client.sns.response.SnsSubscribeResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SnsEndpointTest {
    private SnsEndpoint endpoint;

    @Before
    public void setUp() {
        endpoint = new SnsEndpoint(new HttpWrapper());
    }

    @Test
    public void testParseSubscribeResponse() throws Exception {
        SnsResponse result = endpoint.parseSubmitResponse(
                "<nexmo-sns>\n" + "   <command>subscribe</command>\n" + "   <resultCode>0</resultCode>\n"
                        + "   <resultMessage>a result message</resultMessage>\n"
                        + "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" + "</nexmo-sns>");
        assertEquals(0, result.getResultCode());
        assertEquals("subscribe", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
        assertEquals(SnsSubscribeResponse.class, result.getClass());
        SnsSubscribeResponse subscribeResponse = (SnsSubscribeResponse) result;
        assertEquals("arn:aws:sns:region:num:id", subscribeResponse.getSubscriberArn());
    }

    @Test
    public void testParsePublishResponse() throws Exception {
        SnsResponse result = endpoint.parseSubmitResponse(
                "<nexmo-sns>\n" + "   <command>publish</command>\n" + "   <resultCode>0</resultCode>\n"
                        + "   <resultMessage>a result message</resultMessage>\n"
                        + "   <transactionId>1234</transactionId>\n" + "</nexmo-sns>");
        assertEquals(0, result.getResultCode());
        assertEquals("publish", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
        assertEquals(SnsPublishResponse.class, result.getClass());
        SnsPublishResponse publishResponse = (SnsPublishResponse) result;
        assertEquals("1234", publishResponse.getTransactionId());
    }

    @Test
    public void testParseResponseInvalidResultCode() throws Exception {
        SnsResponse result = endpoint.parseSubmitResponse(
                "<nexmo-sns>\n" + "   <command>subscribe</command>\n" + "   <resultCode>non-numeric</resultCode>\n"
                        + "   <resultMessage>a result message</resultMessage>\n"
                        + "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" + "</nexmo-sns>");
        assertEquals(SnsResponse.STATUS_INTERNAL_ERROR, result.getResultCode());
        assertEquals("subscribe", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test
    public void testParseResponseNullResultCode() throws Exception {
        SnsResponse result = endpoint.parseSubmitResponse(
                "<nexmo-sns>\n" + "   <command>subscribe</command>\n" + "   <resultCode/>\n"
                        + "   <resultMessage>a result message</resultMessage>\n"
                        + "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n"
                        + "   <transactionId>1234</transactionId>\n" + "</nexmo-sns>");
        assertEquals(SnsResponse.STATUS_INTERNAL_ERROR, result.getResultCode());
        assertEquals("subscribe", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test
    public void testParseResponseMissingResultCode() throws Exception {
        try {
            endpoint.parseSubmitResponse("<nexmo-sns>\n" + "   <command>subscribe</command>\n"
                    + "   <resultMessage>a result message</resultMessage>\n"
                    + "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n"
                    + "   <transactionId>1234</transactionId>\n" + "</nexmo-sns>");
            fail("A missing <resultCode> tag should raise VonageResponseParseException");
        } catch (VonageResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseResponseInvalidTagIsIgnored() throws Exception {
        SnsResponse result = endpoint.parseSubmitResponse(
                "<nexmo-sns>\n" + "   <command>subscribe</command>\n" + "   <resultCode>0</resultCode>\n"
                        + "   <resultMessage>a result message</resultMessage>\n"
                        + "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n"
                        + "   <transactionId>1234</transactionId>\n" + "   <whatOnEarthIsThis/>\n" + "</nexmo-sns>");
        assertEquals(0, result.getResultCode());
        assertEquals("subscribe", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test
    public void testParseResponseUnparseable() throws Exception {
        try {
            endpoint.parseSubmitResponse("not-xml");
            fail("Attempting to parse non-xml should throw VonageResponseParseException");
        } catch (VonageResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testParseDummyCommand() throws Exception {
        try {
            endpoint.parseSubmitResponse(
                    "<nexmo-sns>\n" + "   <command>dummy command</command>\n" + "   <resultCode>0</resultCode>\n"
                            + "   <resultMessage>a result message</resultMessage>\n"
                            + "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n"
                            + "   <transactionId>1234</transactionId>\n" + "   <whatOnEarthIsThis/>\n"
                            + "</nexmo-sns>");
            fail("An invalid command should throw VonageResponseParseException");
        } catch (VonageResponseParseException e) {
            // this is expected
        }
    }

    @Test
    public void testDefaultUri() throws Exception {
        SnsRequest request = new SnsPublishRequest("to", "arn", "from", "message");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://sns.nexmo.com/sns/xml",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        SnsEndpoint endpoint = new SnsEndpoint(wrapper);
        SnsRequest request = new SnsPublishRequest("to", "arn", "from", "message");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/sns/xml", builder.build().getURI().toString());
    }
}
