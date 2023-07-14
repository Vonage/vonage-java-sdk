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

import com.vonage.client.ClientTest;
import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.sms.messages.Message;
import com.vonage.client.sms.messages.TextMessage;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

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

    @Test(expected = VonageResponseParseException.class)
    public void testSubmitMessageHttpError() throws Exception {
        stubResponse(500, "");
        Message message = new TextMessage("TestSender", "not-a-number", "Test");
        client.submitMessage(message);
    }

    /*@Test
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
            protected String sampleRequestString() {
                return null;
            }
        }
        .runTests();
    }*/
}
