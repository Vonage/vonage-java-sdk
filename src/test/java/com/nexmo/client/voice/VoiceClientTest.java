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
package com.nexmo.client.voice;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.TestUtils;
import com.nexmo.client.auth.AuthCollection;
import com.nexmo.client.auth.JWTAuthMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VoiceClientTest {
    private TestUtils testUtils = new TestUtils();

    private HttpWrapper stubHttpWrapper(int statusCode, String content) throws Exception {
        HttpClient client = mock(HttpClient.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(client.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes("UTF-8")));
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
        AuthCollection authCollection = new AuthCollection();
        authCollection.add(new JWTAuthMethod(
                "951614e0-eec4-4087-a6b1-3f4c2f169cb0",
                keyBytes
        ));

        HttpWrapper wrapper = new HttpWrapper(authCollection);
        wrapper.setHttpClient(client);

        return wrapper;
    }

    @Test
    public void testCreateCall() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200, "{\n" +
                "  \"conversation_uuid\": \"63f61863-4a51-4f6b-86e1-46edebio0391\",\n" +
                "  \"status\": \"started\",\n" +
                "  \"direction\": \"outbound\"\n" +
                "}"));
        CallEvent evt = client.createCall(
                new Call("447700900903", "447700900904", "http://api.example.com/answer"));
        assertEquals("63f61863-4a51-4f6b-86e1-46edebio0391", evt.getConversationUuid());
    }

    @Test
    public void testListCallsNoFilter() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200, "{\n" +
                "  \"page_size\": 10,\n" +
                "  \"record_index\": 0,\n" +
                "  \"count\": 0,\n" +
                "  \"_embedded\": {\n" +
                "    \"calls\": []\n" +
                "  },\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"/v1/calls?page_size=10&record_index=0\"\n" +
                "    },\n" +
                "    \"first\": {\n" +
                "      \"href\": \"/v1/calls?page_size=10\"\n" +
                "    },\n" +
                "    \"last\": {\n" +
                "      \"href\": \"/v1/calls?page_size=10\"\n" +
                "    }\n" +
                "  }\n" +
                "}\n"));
        CallInfoPage page = client.listCalls();
        assertEquals(0, page.getCount());
    }

    @Test
    public void testListCallsWithFilter() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200, "{\n" +
                "  \"page_size\": 10,\n" +
                "  \"record_index\": 0,\n" +
                "  \"count\": 0,\n" +
                "  \"_embedded\": {\n" +
                "    \"calls\": []\n" +
                "  },\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"/v1/calls?page_size=10&record_index=0\"\n" +
                "    },\n" +
                "    \"first\": {\n" +
                "      \"href\": \"/v1/calls?page_size=10\"\n" +
                "    },\n" +
                "    \"last\": {\n" +
                "      \"href\": \"/v1/calls?page_size=10\"\n" +
                "    }\n" +
                "  }\n" +
                "}\n"));
        CallInfoPage page = client.listCalls(new CallsFilter());
        assertEquals(0, page.getCount());
    }

    @Test
    public void testGetCallDetails() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200, "      {\n" +
                "        \"uuid\": \"93137ee3-580e-45f7-a61a-e0b5716000ef\",\n" +
                "        \"status\": \"completed\",\n" +
                "        \"direction\": \"outbound\",\n" +
                "        \"rate\": \"0.02400000\",\n" +
                "        \"price\": \"0.00280000\",\n" +
                "        \"duration\": \"7\",\n" +
                "        \"network\": \"23410\",\n" +
                "        \"conversation_uuid\": \"aa17bd11-c895-4225-840d-30dc38c31e50\",\n" +
                "        \"start_time\": \"2017-01-13T13:55:02.000Z\",\n" +
                "        \"end_time\": \"2017-01-13T13:55:09.000Z\",\n" +
                "        \"to\": {\n" +
                "          \"type\": \"phone\",\n" +
                "          \"number\": \"447700900104\"\n" +
                "        },\n" +
                "        \"from\": {\n" +
                "          \"type\": \"phone\",\n" +
                "          \"number\": \"447700900105\"\n" +
                "        },\n" +
                "        \"_links\": {\n" +
                "          \"self\": {\n" +
                "            \"href\": \"/v1/calls/93137ee3-580e-45f7-a61a-e0b5716000ef\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n"));
        CallInfo call = client.getCallDetails("93137ee3-580e-45f7-a61a-e0b5716000ef");
        assertEquals("93137ee3-580e-45f7-a61a-e0b5716000ef", call.getUuid());
    }

    @Test
    public void testSendDtmf() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" +
                        "  \"message\": \"DTMF sent\",\n" +
                        "  \"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"\n" +
                        "}"));

        DtmfResponse response = client.sendDtmf("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", "332393");
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
        assertEquals("DTMF sent", response.getMessage());
    }

    @Test
    public void testModifyCall() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200, "{\"message\":\"Received\"}"));
        ModifyCallResponse call = client.modifyCall("93137ee3-580e-45f7-a61a-e0b5716000ef", "hangup");
        assertEquals("Received", call.getMessage());
    }

    @Test
    public void testStartStreamNonLooping() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" +
                        "  \"message\": \"Stream started\",\n" +
                        "  \"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"\n" +
                        "}"));
        StreamResponse response = client.startStream("ssf61863-4a51-ef6b-11e1-w6edebcf93bb","https://nexmo-community.github.io/ncco-examples/assets/voice_api_audio_streaming.mp3");
        assertEquals("Stream started", response.getMessage());
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
    }

    @Test
    public void testStartStreamLooping() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" +
                        "  \"message\": \"Stream started\",\n" +
                        "  \"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"\n" +
                        "}"));
        StreamResponse response = client.startStream("ssf61863-4a51-ef6b-11e1-w6edebcf93bb","https://nexmo-community.github.io/ncco-examples/assets/voice_api_audio_streaming.mp3", 5);
        assertEquals("Stream started", response.getMessage());
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
    }

    @Test
    public void testStopStream() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200, "{\n" +
                "  \"message\": \"Stream stopped\",\n" +
                "  \"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"\n" +
                "}\n"));

        StreamResponse response = client.stopStream("ssf61863-4a51-ef6b-11e1-w6edebcf93bb");
        assertEquals("Stream stopped", response.getMessage());
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
    }

    @Test
    public void testStartTalkAllParams() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" +
                        "  \"message\": \"Talk started\",\n" +
                        "  \"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"\n" +
                        "}\n"));

        TalkResponse response = client.startTalk("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", "Hello World", VoiceName.CELINE, 8);
        assertEquals("Talk started", response.getMessage());
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
    }

    @Test
    public void testStartTalkNonLooping() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" +
                        "  \"message\": \"Talk started\",\n" +
                        "  \"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"\n" +
                        "}\n"));

        TalkResponse response = client.startTalk("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", "Hello World", VoiceName.EMMA);
        assertEquals("Talk started", response.getMessage());
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
    }

    @Test
    public void testStartTalkLoopingWithDefaultVoice() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" +
                        "  \"message\": \"Talk started\",\n" +
                        "  \"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"\n" +
                        "}\n"));

        TalkResponse response = client.startTalk("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", "Hello World", 3);
        assertEquals("Talk started", response.getMessage());
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
    }

    @Test
    public void testStartTalkNonLoopingWithDefaultVoice() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" +
                        "  \"message\": \"Talk started\",\n" +
                        "  \"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"\n" +
                        "}\n"));

        TalkResponse response = client.startTalk("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", "Hello World");
        assertEquals("Talk started", response.getMessage());
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
    }

    @Test
    public void testStopTalk() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" +
                        "  \"message\": \"Talk stopped\",\n" +
                        "  \"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"\n" +
                        "}\n"));

        TalkResponse response = client.stopTalk("ssf61863-4a51-ef6b-11e1-w6edebcf93bb");
        assertEquals("Talk stopped", response.getMessage());
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
    }
}
