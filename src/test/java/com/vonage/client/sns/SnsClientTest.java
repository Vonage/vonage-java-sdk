/*
 *   Copyright 2022 Vonage
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

import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.logging.LoggingUtils;
import com.vonage.client.sns.request.SnsPublishRequest;
import com.vonage.client.sns.request.SnsSubscribeRequest;
import com.vonage.client.sns.response.SnsPublishResponse;
import com.vonage.client.sns.response.SnsSubscribeResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SnsClientTest {
    private HttpWrapper httpWrapper;
    private SnsClient client;

    @Before
    public void setUp() throws Exception {
        httpWrapper = new HttpWrapper(new TokenAuthMethod("abcd", "efgh"));
        client = new SnsClient(httpWrapper);
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
    public void testSubscribe() throws Exception {
        httpWrapper.setHttpClient(stubHttpClient(200, "<nexmo-sns>\n" +
                "   <command>subscribe</command>\n" +
                "   <resultCode>0</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                "</nexmo-sns>"));
        SnsSubscribeResponse result = client.subscribe(new SnsSubscribeRequest(
                "arn:aws:sns:region:num:id",
                "447777111222"
        ));
        assertEquals(0, result.getResultCode());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test
    public void testPublish() throws Exception {
        httpWrapper.setHttpClient(stubHttpClient(200, "<nexmo-sns>\n" +
                "   <command>publish</command>\n" +
                "   <resultCode>0</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <transactionId>1234</transactionId>\n" +
                "</nexmo-sns>"));
        SnsPublishResponse result = client.publish(new SnsPublishRequest(
                "arn:aws:sns:region:num:id",
                "447777111222",
                "447777111223",
                "Hello!"
        ));
        assertEquals(0, result.getResultCode());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test
    public void testSubmitWithInvalidResponse() throws Exception {
        try {
            httpWrapper.setHttpClient(stubHttpClient(500, "<nexmo-sms/>"));
            client.subscribe(new SnsSubscribeRequest(
                    "arn:aws:sns:region:num:id",
                    "447777111222"
            ));
            fail("An error Http response should raise IOException");
        } catch (VonageResponseParseException nrp) {
            // This is expected
        }
    }


}
