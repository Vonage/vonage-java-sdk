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
package com.vonage.client.sms;

import com.vonage.client.*;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.sms.messages.BinaryMessage;
import com.vonage.client.sms.messages.Message;
import com.vonage.client.sms.messages.TextMessage;
import com.vonage.client.sms.messages.WapPushMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class SmsClientTest extends ClientTest<SmsClient> {

    public SmsClientTest() {
        wrapper = new HttpWrapper(new TokenAuthMethod("not-an-api-key", "secret"));
        client = new SmsClient(wrapper);
    }

    @Test
    public void testSubmitMessage() throws Exception {
        stubResponse("{\n" +
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
                "}"
        );

        Message message = new TextMessage("TestSender", "not-a-number", "Test");

        SmsSubmissionResponse r = client.submitMessage(message);
        assertEquals(r.getMessageCount(), 2);
        assertEquals(r.getMessages().size(), 2);
    }

    @Test(expected = VonageApiResponseException.class)
    public void testSubmitMessageHttpError() throws Exception {
        stubResponse(500, "");
        Message message = new TextMessage("TestSender", "not-a-number", "Test");
        client.submitMessage(message);
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
                return Arrays.asList(SignatureAuthMethod.class, TokenAuthMethod.class);
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
            protected String sampleRequestBodyString() {
                return null;
            }

            @Override
            public void runTests() throws Exception {
                super.runTests();
                testConstructParamsText();
                testStatusReportRequiredSetter();
                testConstructParamsUnicode();
                testConstructParamsBinary();
                testConstructParamsWapPush();
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
                Message message = new TextMessage("TestSender", "not-a-number", "Test");
                Map<String, String> params = new LinkedHashMap<>();
                params.put("from", "TestSender");
                params.put("to", "not-a-number");
                params.put("type", "text");
                params.put("text", "Test");
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
                Message message = new TextMessage("TestSender", "not-a-number", "Test", true);
                Map<String, String> params = new LinkedHashMap<>();
                params.put("from", "TestSender");
                params.put("to", "not-a-number");
                params.put("type", "unicode");
                params.put("text", "Test");
                assertRequestParams(params, message);
            }

            void testConstructParamsBinary() throws Exception {
                Message message = new BinaryMessage("TestSender", "not-a-number", "abc".getBytes(), "def".getBytes());
                Map<String, String> params = new LinkedHashMap<>();
                params.put("from", "TestSender");
                params.put("to", "not-a-number");
                params.put("type", "binary");
                params.put("udh", "646566");
                params.put("body", "616263");
                params.put("protocol-id", "0");
                assertRequestParams(params, message);
            }

            void testConstructParamsWapPush() throws Exception {
                Message message = new WapPushMessage("TestSender", "not-a-number", "http://the-url", "A Title");
                Map<String, String> params = new LinkedHashMap<>();
                params.put("from", "TestSender");
                params.put("to", "not-a-number");
                params.put("type", "wappush");
                params.put("url", "http://the-url");
                params.put("title", "A Title");
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
                params.put("content-id","abcd-1234");
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
                    "{\n" + "  \"message-count\":2,\n" + "  \"messages\":[\n" + "    {\n"
                            + "      \"to\":\"not-a-number\",\n" + "      \"message-id\":\"message-id-1\",\n"
                            + "      \"status\":\"0\",\n" + "      \"remaining-balance\":\"26.43133450\",\n"
                            + "      \"message-price\":\"0.03330000\",\n" + "      \"network\":\"12345\",\n"
                            + "      \"client-ref\": \"first ref\"\n" + "    },\n" + "    {\n"
                            + "      \"to\":\"not-a-number-2\",\n" + "      \"message-id\":\"message-id-2\",\n"
                            + "      \"status\":\"0\",\n" + "      \"remaining-balance\":\"27.43133450\",\n"
                            + "      \"message-price\":\"0.03430000\",\n" + "      \"network\":\"98765\",\n"
                            + "      \"client-ref\": \"second ref\"\n" + "    }\n" + "  ]\n" + "}"
                );
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
                assertTrue(firstMessage.toString().contains("first ref"));

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
                        "{\n" + "  \"message-count\":2,\n" + "  \"messages\":[\n" + "    {\n"
                                + "      \"to\":\"not-a-number\",\n" + "      \"message-id\":\"message-id-1\",\n"
                                + "      \"status\":\"12345\",\n" + "      \"remaining-balance\":\"26.43133450\",\n"
                                + "      \"message-price\":\"0.03330000\",\n" + "      \"network\":\"12345\",\n"
                                + "      \"client-ref\":\"abcde\"\n" + "    },\n" + "    {\n"
                                + "      \"to\":\"not-a-number\",\n" + "      \"message-id\":\"message-id-2\",\n"
                                + "      \"status\":\"0\",\n" + "      \"remaining-balance\":\"26.43133450\",\n"
                                + "      \"message-price\":\"0.03330000\",\n" + "      \"network\":\"12345\"\n" + "    }\n"
                                + "  ]\n" + "}"
                );
                assertEquals(MessageStatus.UNKNOWN, rs.getMessages().get(0).getStatus());
            }

            void testParseResponseError() throws Exception {
                SmsSubmissionResponse rs = parseResponse(
                "{\n" + "  \"message-count\":1,\n" + "  \"messages\":[\n" + "    {\n" + "    \"status\":\"6\",\n"
                        + "    \"error-text\": \"The message was invalid\"\n" + "    }\n" + "  ]\n" + "}"
                );
                assertEquals(MessageStatus.INVALID_MESSAGE, rs.getMessages().get(0).getStatus());
            }

            void testParseResponseUnexpectedNode() throws Exception {
                SmsSubmissionResponse rs = parseResponse(
                "{\n" + "  \"message-count\":1,\n" + "  \"messages\":[\n" + "    {\n"
                        + "      \"to\":\"not-a-number\",\n" + "      \"message-id\":\"\",\n"
                        + "      \"status\":\"0\",\n" + "      \"remaining-balance\":\"26.43133450\",\n"
                        + "      \"message-price\":\"0.0330000\",\n" + "      \"network\":\"12345\",\n"
                        + "      \"WHAT-IS-THIS\":\"\"\n" + "    }\n" + "  ]\n" + "}"
                );
                assertEquals(MessageStatus.OK, rs.getMessages().get(0).getStatus());
            }

            void testParseResponseStatusThrottled() throws Exception {
                SmsSubmissionResponse rs = parseResponse(
                "{\n" + "  \"message-count\":1,\n" + "  \"messages\":[\n" + "    {\n" + "      \"status\":\"1\"\n"
                        + "    }\n" + "  ]\n" + "}"
                );
                SmsSubmissionResponseMessage r = rs.getMessages().get(0);
                assertEquals(MessageStatus.THROTTLED, r.getStatus());
                assertTrue(r.isTemporaryError());
            }

            void testParseResponseStatusInternalError() throws Exception {
                SmsSubmissionResponse rs = parseResponse(
                "{\n" + "  \"message-count\":1,\n" + "  \"messages\":[\n" + "    {\n" + "      \"status\":\"5\"\n"
                        + "    }\n" + "  ]\n" + "}"
                );
                SmsSubmissionResponseMessage r = rs.getMessages().get(0);
                assertEquals(MessageStatus.INTERNAL_ERROR, r.getStatus());
                assertTrue(r.isTemporaryError());
            }

            void testParseResponseStatusTooManyBinds() throws Exception {
                SmsSubmissionResponse rs = parseResponse(
                "{\n" + "  \"message-count\":1,\n" + "  \"messages\":[\n" + "    {\n" + "      \"status\":\"10\"\n"
                        + "    }\n" + "  ]\n" + "}"
                );
                SmsSubmissionResponseMessage r = rs.getMessages().get(0);
                assertEquals(MessageStatus.TOO_MANY_BINDS, r.getStatus());
                assertTrue(r.isTemporaryError());
            }
        }
        .runTests();
    }
}
