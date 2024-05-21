/*
 *   Copyright 2024 Vonage
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

import com.vonage.client.*;
import com.vonage.client.auth.ApiKeyQueryParamsAuthMethod;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.sms.messages.BinaryMessage;
import com.vonage.client.sms.messages.Message;
import com.vonage.client.sms.messages.TextMessage;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class SmsClientTest extends ClientTest<SmsClient> {

    public SmsClientTest() {
        client = new SmsClient(wrapper);
    }

    @Test
    public void testSubmitMessage() throws Exception {
        stubResponse("""
                {
                  "message-count":2,
                  "messages":[
                    {
                      "to":"not-a-number",
                      "message-id":"message-id-1",
                      "status":"0",
                      "remaining-balance":"26.43133450",
                      "message-price":"0.03330000",
                      "network":"12345"
                    },
                    {
                      "to":"not-a-number",
                      "message-id":"message-id-2",
                      "status":"0",
                      "remaining-balance":"26.43133450",
                      "message-price":"0.03330000",
                      "network":"12345"
                    }
                  ]
                }"""
        );

        var message = new TextMessage("TestSender", "not-a-number", "Test");

        var response = client.submitMessage(message);
        TestUtils.testJsonableBaseObject(response, true);
        assertEquals(2, response.getMessageCount());
        assertEquals(2, response.getMessages().size());
    }

    @Test
    public void testSubmitMessageHttpError() throws Exception {
        stubResponse(500, "");
        var message = new TextMessage("TestSender", "not-a-number", "Test");
        assertThrows(VonageApiResponseException.class, () -> client.submitMessage(message));
        assertThrows(IllegalArgumentException.class, () -> message.setClientReference("R".repeat(41)));
    }

    @Test
    public void testSendMessageEndpoint() throws Exception {
        new DynamicEndpointTestSpec<Message, SmsSubmissionResponse>() {

            @Override
            protected RestEndpoint<Message, SmsSubmissionResponse> endpoint() {
                return client.sendMessage;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
                return Arrays.asList(SignatureAuthMethod.class, ApiKeyQueryParamsAuthMethod.class);
            }

            @Override
            protected Class<? extends Exception> expectedResponseExceptionType() {
                return VonageApiResponseException.class;
            }

            @Override
            protected String expectedContentTypeHeader(Message request) {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected String expectedDefaultBaseUri() {
                return "https://rest.nexmo.com";
            }

            @Override
            protected String expectedEndpointUri(Message request) {
                return "/sms/json";
            }

            @Override
            protected Message sampleRequest() {
                return new TextMessage("TestSender", "447900000001", "Test msg");
            }

            @Override
            public void runTests() throws Exception {
                super.runTests();
                testConstructParamsText();
                testStatusReportRequiredSetter();
                testConstructParamsUnicode();
                testConstructParamsBinary();
                testConstructParamsValidityPeriodTTL();
                testConstructParamsContentId();
                testConstructParamsEntityId();
                testParseResponse();
                testParseResponseInvalidStatus();
                testParseResponseError();
                testParseResponseUnexpectedNode();
                testParseResponseStatusThrottled();
                testParseResponseStatusInternalError();
                testParseResponseStatusTooManyBinds();
            }

            void testConstructParamsText() throws Exception {
                var message = new TextMessage("TestSender", "not-a-number", "Test Hi");
                message.setClientReference("40 char reference");
                message.setMessageClass(Message.MessageClass.CLASS_3);
                message.setCallbackUrl("https://example.org/sms/cb");
                message.setStatusReportRequired(true);
                Map<String, String> params = new LinkedHashMap<>();
                params.put("status-report-req", "1");
                params.put("from", "TestSender");
                params.put("to", "not-a-number");
                params.put("type", "text");
                params.put("text", "Test Hi");
                params.put("client-ref", "40 char reference");
                params.put("message-class", "3");
                params.put("callback", "https://example.org/sms/cb");
                assertFalse(message.isUnicode());
                assertTrue(message.getStatusReportRequired());
                assertRequestParams(params, message);
            }

            void testStatusReportRequiredSetter() throws Exception {
                Message message = new TextMessage("TestSender", "not-a-number", "Test");
                message.setStatusReportRequired(true);
                Map<String, String> params = new LinkedHashMap<>();
                params.put("from", "TestSender");
                params.put("to", "not-a-number");
                params.put("type", "text");
                params.put("status-report-req", "1");
                params.put("text", "Test");
                assertRequestParams(params, message);
            }

            void testConstructParamsUnicode() throws Exception {
                var message = new TextMessage("TestSender", "not-a-number", "Test", true);
                Map<String, String> params = new LinkedHashMap<>();
                params.put("from", "TestSender");
                params.put("to", "not-a-number");
                params.put("type", "unicode");
                params.put("text", "Test");
                assertTrue(message.isUnicode());
                assertRequestParams(params, message);
            }

            void testConstructParamsBinary() throws Exception {
                var message = new BinaryMessage("TestSender", "not-a-number", "abc".getBytes(), "def".getBytes());
                message.setProtocolId(123456);
                Map<String, String> params = new LinkedHashMap<>();
                params.put("from", "TestSender");
                params.put("to", "not-a-number");
                params.put("type", "binary");
                params.put("udh", "646566");
                params.put("body", "616263");
                params.put("protocol-id", "123456");
                assertRequestParams(params, message);
            }

            void testConstructParamsValidityPeriodTTL() throws Exception {
                Message message = new BinaryMessage("TestSender", "not-a-number", "abc".getBytes(), "def".getBytes());
                message.setTimeToLive(50L);
                Map<String, String> params = new LinkedHashMap<>();
                params.put("ttl", "50");
                assertRequestContainsParams(params, message);
            }

            void testConstructParamsContentId() throws Exception {
                Message message = new TextMessage("TestSender", "not-a-number", "Test");
                message.setContentId("abcd-1234");
                Map<String, String> params = new LinkedHashMap<>();
                params.put("from", "TestSender");
                params.put("to", "not-a-number");
                params.put("type", "text");
                params.put("text", "Test");
                params.put("content-id", "abcd-1234");
                assertRequestContainsParams(params, message);
            }
            
            void testConstructParamsEntityId() throws Exception {
                Message message = new TextMessage("TestSender", "not-a-number", "Test");
                message.setEntityId("abcd-1234");
                Map<String, String> params = new LinkedHashMap<>();
                params.put("from", "TestSender");
                params.put("to", "not-a-number");
                params.put("type", "text");
                params.put("text", "Test");
                params.put("entity-id","abcd-1234");
                assertRequestContainsParams(params, message);
            }

            void testParseResponse() throws Exception {
                SmsSubmissionResponse rs = parseResponse(
                        """
                                {
                                  "message-count":2,
                                  "messages":[
                                    {
                                      "to":"not-a-number",
                                      "message-id":"message-id-1",
                                      "status":"0",
                                      "remaining-balance":"26.43133450",
                                      "message-price":"0.03330000",
                                      "network":"12345",
                                      "client-ref": "first ref",
                                      "account-ref": "customer1234"
                                    },
                                    {
                                      "to":"not-a-number-2",
                                      "message-id":"message-id-2",
                                      "status":"0",
                                      "remaining-balance":"27.43133450",
                                      "message-price":"0.03430000",
                                      "network":"98765",
                                      "client-ref": "second ref"
                                    }
                                  ]
                                }"""
                );
                assertEquals(2, rs.getMessageCount());
                assertEquals(2, rs.getMessages().size());

                var firstMessage = rs.getMessages().getFirst();
                var secondMessage = rs.getMessages().get(1);

                TestUtils.testJsonableBaseObject(firstMessage, true);
                assertEquals("not-a-number", firstMessage.getTo());
                assertEquals("message-id-1", firstMessage.getId());
                assertEquals(MessageStatus.OK, firstMessage.getStatus());
                assertEquals(new BigDecimal("26.43133450"), firstMessage.getRemainingBalance());
                assertEquals(new BigDecimal("0.03330000"), firstMessage.getMessagePrice());
                assertEquals("12345", firstMessage.getNetwork());
                assertEquals("first ref", firstMessage.getClientRef());
                assertEquals("customer1234", firstMessage.getAccountRef());
                assertTrue(firstMessage.toString().contains("first ref"));

                TestUtils.testJsonableBaseObject(secondMessage, true);
                assertEquals("not-a-number-2", secondMessage.getTo());
                assertEquals("message-id-2", secondMessage.getId());
                assertEquals(MessageStatus.OK, secondMessage.getStatus());
                assertEquals(new BigDecimal("27.43133450"), secondMessage.getRemainingBalance());
                assertEquals(new BigDecimal("0.03430000"), secondMessage.getMessagePrice());
                assertEquals("98765", secondMessage.getNetwork());
                assertEquals("second ref", secondMessage.getClientRef());
                assertTrue(secondMessage.toString().contains("second ref"));
            }

            void testParseResponseInvalidStatus() throws Exception {
                SmsSubmissionResponse rs = parseResponse(
                        """
                                {
                                  "message-count":2,
                                  "messages":[
                                    {
                                      "to":"not-a-number",
                                      "message-id":"message-id-1",
                                      "status":"12345",
                                      "remaining-balance":"26.43133450",
                                      "message-price":"0.03330000",
                                      "network":"12345",
                                      "client-ref":"abcde"
                                    },
                                    {
                                      "to":"not-a-number",
                                      "message-id":"message-id-2",
                                      "status":"0",
                                      "remaining-balance":"26.43133450",
                                      "message-price":"0.03330000",
                                      "network":"12345",
                                      "error-text": "Missing to param"
                                    }
                                  ]
                                }"""
                );
                assertEquals(MessageStatus.UNKNOWN, rs.getMessages().getFirst().getStatus());
                assertEquals("Missing to param", rs.getMessages().get(1).getErrorText());
            }

            void testParseResponseError() throws Exception {
                SmsSubmissionResponse rs = parseResponse(
                        """
                                {
                                  "message-count":1,
                                  "messages":[
                                    {
                                    "status":"6",
                                    "error-text": "The message was invalid"
                                    }
                                  ]
                                }"""
                );
                assertEquals(MessageStatus.INVALID_MESSAGE, rs.getMessages().getFirst().getStatus());
            }

            void testParseResponseUnexpectedNode() throws Exception {
                SmsSubmissionResponse rs = parseResponse(
                        """
                                {
                                  "message-count":1,
                                  "messages":[
                                    {
                                      "to":"not-a-number",
                                      "message-id":"",
                                      "status":"0",
                                      "remaining-balance":"26.43133450",
                                      "message-price":"0.0330000",
                                      "network":"12345",
                                      "WHAT-IS-THIS":""
                                    }
                                  ]
                                }"""
                );
                assertEquals(MessageStatus.OK, rs.getMessages().getFirst().getStatus());
            }

            void testParseResponseStatusThrottled() throws Exception {
                SmsSubmissionResponse rs = parseResponse(
                        """
                                {
                                  "message-count":1,
                                  "messages":[
                                    {
                                      "status":"1"
                                    }
                                  ]
                                }"""
                );
                SmsSubmissionResponseMessage r = rs.getMessages().getFirst();
                assertEquals(MessageStatus.THROTTLED, r.getStatus());
                assertTrue(r.isTemporaryError());
            }

            void testParseResponseStatusInternalError() throws Exception {
                SmsSubmissionResponse rs = parseResponse(
                        """
                                {
                                  "message-count":1,
                                  "messages":[
                                    {
                                      "status":"5"
                                    }
                                  ]
                                }"""
                );
                SmsSubmissionResponseMessage r = rs.getMessages().getFirst();
                assertEquals(MessageStatus.INTERNAL_ERROR, r.getStatus());
                assertTrue(r.isTemporaryError());
            }

            void testParseResponseStatusTooManyBinds() throws Exception {
                SmsSubmissionResponse rs = parseResponse(
                        """
                                {
                                  "message-count":1,
                                  "messages":[
                                    {
                                      "status":"10"
                                    }
                                  ]
                                }"""
                );
                SmsSubmissionResponseMessage r = rs.getMessages().getFirst();
                assertEquals(MessageStatus.TOO_MANY_BINDS, r.getStatus());
                assertTrue(r.isTemporaryError());
            }
        }
        .runTests();
    }
}
