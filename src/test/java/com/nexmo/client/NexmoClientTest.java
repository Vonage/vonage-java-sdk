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
package com.nexmo.client;

import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.voice.Call;
import com.nexmo.client.voice.CallEvent;
import com.nexmo.client.voice.CallStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NexmoClientTest {
    private TestUtils testUtils = new TestUtils();

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
    public void testConstructNexmoClient() throws Exception {
        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
        NexmoClient client = new NexmoClient(
                new JWTAuthMethod(
                        "951614e0-eec4-4087-a6b1-3f4c2f169cb0",
                        keyBytes
                )
        );
        client.setHttpClient(stubHttpClient(200, "{\n" +
                "  \"conversation_uuid\": \"63f61863-4a51-4f6b-86e1-46edebio0391\",\n" +
                "  \"status\": \"started\",\n" +
                "  \"direction\": \"outbound\"\n" +
                "}"));

        CallEvent evt = client.getVoiceClient().createCall(new Call("4499991111", "44111222333", "https://callback.example.com/"));
        assertEquals(CallStatus.STARTED, evt.getStatus());
    }

    @Test
    public void testConstructNexmoClientWithBaseUri() throws Exception {
        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
        NexmoClient client = new NexmoClient(
                "https://example.com",
                new JWTAuthMethod(
                        "951614e0-eec4-4087-a6b1-3f4c2f169cb0",
                        keyBytes
                )
                );
        client.setHttpClient(stubHttpClient(200, "{\n" +
                "  \"conversation_uuid\": \"63f61863-4a51-4f6b-86e1-46edebio0391\",\n" +
                "  \"status\": \"started\",\n" +
                "  \"direction\": \"outbound\"\n" +
                "}"));
        assertEquals("https://example.com", client.getBaseUri());
        CallEvent evt = client.getVoiceClient().createCall(new Call("4499991111", "44111222333", "https://callback.example.com/"));
        assertEquals(CallStatus.STARTED, evt.getStatus());
    }


    @Test
    public void testBaseUriSetterOnNexmoClient() throws Exception {
        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
        NexmoClient client = new NexmoClient(
                new JWTAuthMethod(
                        "951614e0-eec4-4087-a6b1-3f4c2f169cb0",
                        keyBytes
                )
        );
        client.setHttpClient(stubHttpClient(200, "{\n" +
                "  \"conversation_uuid\": \"63f61863-4a51-4f6b-86e1-46edebio0391\",\n" +
                "  \"status\": \"started\",\n" +
                "  \"direction\": \"outbound\"\n" +
                "}"));
        assertNull(client.getBaseUri());
        client.setBaseUri("https://example.com");
        assertEquals("https://example.com", client.getBaseUri());
        assertEquals("https://example.com", client.getInsightClient().getBaseUri());
        assertEquals("https://example.com", client.getSmsClient().getBaseUri());
        assertEquals("https://example.com", client.getSnsClient().getBaseUri());
        assertEquals("https://example.com", client.getVerifyClient().getBaseUri());
        assertEquals("https://example.com", client.getVoiceClient().getBaseUri());
    }
}
