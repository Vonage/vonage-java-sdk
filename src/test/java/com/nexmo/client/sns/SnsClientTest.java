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


import com.nexmo.client.HttpWrapper;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sns.request.SnsPublishRequest;
import com.nexmo.client.sns.request.SnsSubscribeRequest;
import com.nexmo.client.sns.response.SnsPublishResponse;
import com.nexmo.client.sns.response.SnsSubscribeResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SnsClientTest {
    private HttpWrapper httpWrapper;
    private SnsClient client;

    @Before
    public void setUp() throws Exception {
        this.httpWrapper = new HttpWrapper(new TokenAuthMethod("abcd", "efgh"));
        this.client = new SnsClient(httpWrapper);
    }

    private HttpClient stubHttpClient(int statusCode, String content) throws Exception {
        HttpClient result = mock(HttpClient.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes("UTF-8")));
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        return result;
    }

    @Test
    public void testSubscribe() throws Exception {
        this.httpWrapper.setHttpClient(this.stubHttpClient(200, "<nexmo-sns>\n" +
                "   <command>subscribe</command>\n" +
                "   <resultCode>0</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <subscriberArn>arn:aws:sns:region:num:id</subscriberArn>\n" +
                "</nexmo-sns>"));
        SnsSubscribeResponse result = this.client.subscribe(new SnsSubscribeRequest(
                "arn:aws:sns:region:num:id",
                "447777111222"
        ));
        assertEquals(0, result.getResultCode());
        assertEquals("a result message", result.getResultMessage());
    }

    @Test
    public void testPublish() throws Exception {
        this.httpWrapper.setHttpClient(this.stubHttpClient(200, "<nexmo-sns>\n" +
                "   <command>publish</command>\n" +
                "   <resultCode>0</resultCode>\n" +
                "   <resultMessage>a result message</resultMessage>\n" +
                "   <transactionId>1234</transactionId>\n" +
                "</nexmo-sns>"));
        SnsPublishResponse result = this.client.publish(new SnsPublishRequest(
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
            this.httpWrapper.setHttpClient(this.stubHttpClient(500, "<nexmo-sms/>"));
            this.client.subscribe(new SnsSubscribeRequest(
                    "arn:aws:sns:region:num:id",
                    "447777111222"
            ));
            fail("An error Http response should raise IOException");
        } catch (IOException ioe) {
            // This is expected
        }
    }


}
