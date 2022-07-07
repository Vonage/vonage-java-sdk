/*
 *   Copyright 2020 Vonage
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


import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.logging.LoggingUtils;
import com.vonage.client.sms.messages.Message;
import com.vonage.client.sms.messages.TextMessage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SmsClientTest {
    private HttpWrapper wrapper;
    private SmsClient client;

    @Before
    public void setUp() throws ParserConfigurationException {
        wrapper = new HttpWrapper(new TokenAuthMethod("not-an-api-key", "secret"));
        client = new SmsClient(wrapper);
    }

    private HttpClient stubHttpClient(int statusCode, String content) throws Exception {
        HttpClient result = mock(HttpClient.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(LoggingUtils.logResponse(any(HttpResponse.class))).thenReturn("response logged");
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        return result;
    }

    @Test
    public void testSubmitMessage() throws Exception {
        wrapper.setHttpClient(stubHttpClient(
                200,
                "{\n" +
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
                        "}"));

        Message message = new TextMessage("TestSender", "not-a-number", "Test");

        SmsSubmissionResponse r = client.submitMessage(message);
        assertEquals(r.getMessageCount(), 2);
        assertEquals(r.getMessages().size(), 2);
    }

    @Test
    public void testSubmitMessageHttpError() throws Exception {
        wrapper.setHttpClient(stubHttpClient(500, ""));

        Message message = new TextMessage("TestSender", "not-a-number", "Test");
        try {
            client.submitMessage(message);
            fail("A VonageResponseParseException should be thrown if an HTTP 500 response is received.");
        } catch (VonageResponseParseException nrp) {
            // This is expected
        }
    }
}
