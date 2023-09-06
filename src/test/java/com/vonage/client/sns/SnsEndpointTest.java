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

import com.vonage.client.*;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.sns.request.SnsPublishRequest;
import com.vonage.client.sns.request.SnsRequest;
import com.vonage.client.sns.response.SnsPublishResponse;
import com.vonage.client.sns.response.SnsResponse;
import com.vonage.client.sns.response.SnsSubscribeResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SnsEndpointTest extends DynamicEndpointTestSpec<SnsRequest, SnsResponse> {
    private SnsEndpoint endpoint;

    @Before
    public void setUp() {
        endpoint = new SnsEndpoint(new HttpWrapper());
    }

    @Test
    public void testParseSubscribeResponse() throws Exception {
        SnsResponse result = endpoint.parseResponseFromString(
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
        SnsResponse result = endpoint.parseResponseFromString(
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
        SnsResponse result = endpoint.parseResponseFromString(
                "<nexmo-sns>\n" + "   <command>subscribe</command>\n" + "   <resultCode>non-numeric</resultCode>\n"
                        + "   <resultMessage>a result message</resultMessage>\n"
                        + "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" + "</nexmo-sns>");
        assertEquals(SnsResponse.STATUS_INTERNAL_ERROR, result.getResultCode());
        assertEquals("subscribe", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test
    public void testParseResponseNullResultCode() throws Exception {
        SnsResponse result = endpoint.parseResponseFromString(
                "<nexmo-sns>\n" + "   <command>subscribe</command>\n" + "   <resultCode/>\n"
                        + "   <resultMessage>a result message</resultMessage>\n"
                        + "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n"
                        + "   <transactionId>1234</transactionId>\n" + "</nexmo-sns>");
        assertEquals(SnsResponse.STATUS_INTERNAL_ERROR, result.getResultCode());
        assertEquals("subscribe", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test(expected = VonageResponseParseException.class)
    public void testParseResponseMissingResultCode() throws Exception {
        endpoint.parseResponseFromString("<nexmo-sns>\n" + "   <command>subscribe</command>\n"
                + "   <resultMessage>a result message</resultMessage>\n"
                + "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n"
                + "   <transactionId>1234</transactionId>\n" + "</nexmo-sns>");
    }

    @Test
    public void testParseResponseInvalidTagIsIgnored() throws Exception {
        SnsResponse result = endpoint.parseResponseFromString(
                "<nexmo-sns>\n" + "   <command>subscribe</command>\n" + "   <resultCode>0</resultCode>\n"
                        + "   <resultMessage>a result message</resultMessage>\n"
                        + "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n"
                        + "   <transactionId>1234</transactionId>\n" + "   <whatOnEarthIsThis/>\n" + "</nexmo-sns>");
        assertEquals(0, result.getResultCode());
        assertEquals("subscribe", result.getCommand());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test(expected = VonageResponseParseException.class)
    public void testParseResponseUnparseable() throws Exception {
        endpoint.parseResponseFromString("not-xml");
    }

    @Test(expected = VonageResponseParseException.class)
    public void testParseDummyCommand() {
        endpoint.parseResponseFromString(
                "<nexmo-sns>\n" + "   <command>dummy command</command>\n" + "   <resultCode>0</resultCode>\n"
                        + "   <resultMessage>a result message</resultMessage>\n"
                        + "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n"
                        + "   <transactionId>1234</transactionId>\n" + "   <whatOnEarthIsThis/>\n"
                        + "</nexmo-sns>");
    }

    @Override
    protected void assertErrorResponse(int statusCode) {
        assertThrows(expectedResponseExceptionType(), () -> endpointAsAbstractMethod().parseResponse(
                TestUtils.makeJsonHttpResponse(statusCode, "<nexmo-sns/>")
        ));
    }

    @Test
    @Override
    public void runTests() throws Exception {
        super.runTests();
    }

    @Override
    protected RestEndpoint<SnsRequest, SnsResponse> endpoint() {
        return endpoint;
    }

    @Override
    protected HttpMethod expectedHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
        return Arrays.asList(SignatureAuthMethod.class, TokenAuthMethod.class);
    }

    @Override
    protected Class<? extends Exception> expectedResponseExceptionType() {
        return VonageResponseParseException.class;
    }

    @Override
    protected String expectedDefaultBaseUri() {
        return "https://sns.nexmo.com";
    }

    @Override
    protected String expectedEndpointUri(SnsRequest request) {
        return "/sns/xml";
    }

    @Override
    protected SnsRequest sampleRequest() {
        return new SnsPublishRequest("to", "arn", "from", "message");
    }

}
