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
package com.vonage.client.voice;

import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.AuthCollection;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.logging.LoggingUtils;
import com.vonage.client.voice.ncco.Ncco;
import com.vonage.client.voice.ncco.TalkAction;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class VoiceClientTest {
    private final TestUtils testUtils = new TestUtils();

    private HttpWrapper stubHttpWrapper(int statusCode, String content) throws Exception {
        HttpClient client = mock(HttpClient.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(client.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(LoggingUtils.logResponse(any(HttpResponse.class))).thenReturn("response logged");
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
        AuthCollection authCollection = new AuthCollection();
        authCollection.add(new JWTAuthMethod("951614e0-eec4-4087-a6b1-3f4c2f169cb0", keyBytes));

        HttpWrapper wrapper = new HttpWrapper(authCollection);
        wrapper.setHttpClient(client);

        return wrapper;
    }

    @Test
    public void testCreateCall() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"conversation_uuid\": \"63f61863-4a51-4f6b-86e1-46edebio0391\",\n"
                        + "  \"status\": \"started\",\n" + "  \"direction\": \"outbound\"\n" + "}"
        ));
        CallEvent evt = client.createCall(new Call("447700900903", "447700900904", "http://api.example.com/answer"));
        assertEquals("63f61863-4a51-4f6b-86e1-46edebio0391", evt.getConversationUuid());
    }

    @Test
    public void testListCallsNoFilter() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"page_size\": 10,\n" + "  \"record_index\": 0,\n" + "  \"count\": 0,\n"
                        + "  \"_embedded\": {\n" + "    \"calls\": []\n" + "  },\n" + "  \"_links\": {\n"
                        + "    \"self\": {\n" + "      \"href\": \"/v1/calls?page_size=10&record_index=0\"\n"
                        + "    },\n" + "    \"first\": {\n" + "      \"href\": \"/v1/calls?page_size=10\"\n"
                        + "    },\n" + "    \"last\": {\n" + "      \"href\": \"/v1/calls?page_size=10\"\n" + "    }\n"
                        + "  }\n" + "}\n"
        ));
        CallInfoPage page = client.listCalls();
        assertEquals(0, page.getCount());
    }

    @Test
    public void testListCallsWithFilter() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"page_size\": 10,\n" + "  \"record_index\": 0,\n" + "  \"count\": 0,\n"
                        + "  \"_embedded\": {\n" + "    \"calls\": []\n" + "  },\n" + "  \"_links\": {\n"
                        + "    \"self\": {\n" + "      \"href\": \"/v1/calls?page_size=10&record_index=0\"\n"
                        + "    },\n" + "    \"first\": {\n" + "      \"href\": \"/v1/calls?page_size=10\"\n"
                        + "    },\n" + "    \"last\": {\n" + "      \"href\": \"/v1/calls?page_size=10\"\n" + "    }\n"
                        + "  }\n" + "}\n"
        ));
        CallInfoPage page = client.listCalls(CallsFilter.builder().build());
        assertEquals(0, page.getCount());
    }

    @Test
    public void testGetCallDetails() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "      {\n" + "        \"uuid\": \"93137ee3-580e-45f7-a61a-e0b5716000ef\",\n"
                        + "        \"status\": \"completed\",\n" + "        \"direction\": \"outbound\",\n"
                        + "        \"rate\": \"0.02400000\",\n" + "        \"price\": \"0.00280000\",\n"
                        + "        \"duration\": \"7\",\n" + "        \"network\": \"23410\",\n"
                        + "        \"conversation_uuid\": \"aa17bd11-c895-4225-840d-30dc38c31e50\",\n"
                        + "        \"start_time\": \"2017-01-13T13:55:02.000Z\",\n"
                        + "        \"end_time\": \"2017-01-13T13:55:09.000Z\",\n" + "        \"to\": {\n"
                        + "          \"type\": \"phone\",\n" + "          \"number\": \"447700900104\"\n"
                        + "        },\n" + "        \"from\": {\n" + "          \"type\": \"phone\",\n"
                        + "          \"number\": \"447700900105\"\n" + "        },\n" + "        \"_links\": {\n"
                        + "          \"self\": {\n"
                        + "            \"href\": \"/v1/calls/93137ee3-580e-45f7-a61a-e0b5716000ef\"\n" + "          }\n"
                        + "        }\n" + "      }\n"
        ));
        CallInfo call = client.getCallDetails("93137ee3-580e-45f7-a61a-e0b5716000ef");
        assertEquals("93137ee3-580e-45f7-a61a-e0b5716000ef", call.getUuid());
    }

    @Test
    public void testSendDtmf() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"message\": \"DTMF sent\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}"
        ));

        DtmfResponse response = client.sendDtmf("944dd293-ca13-4a58-bc37-6252e11474be", "332393");
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
        assertEquals("DTMF sent", response.getMessage());
        assertThrows(IllegalArgumentException.class, () ->
                client.sendDtmf("944dd293-ca13-4a58-bc37-6252e11474be", null)
        );
        assertThrows(IllegalArgumentException.class, () ->
                client.sendDtmf("invalid", "1234")
        );
    }

    @Test
    public void testModifyCall() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200, "{\"message\":\"Received\"}"));
        ModifyCallResponse call = client.modifyCall("93137ee3-580e-45f7-a61a-e0b5716000ef", ModifyCallAction.HANGUP);
        assertEquals("Received", call.getMessage());
        assertThrows(IllegalArgumentException.class, () -> client.modifyCall("invalid", ModifyCallAction.HANGUP));
        assertThrows(NullPointerException.class, () ->
                client.modifyCall("93137ee3-580e-45f7-a61a-e0b5716000ef", null)
        );
    }

    @Test
    public void testModifyCallLegacy() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200, "{\"message\":\"Received\"}"));
        ModifyCallResponse call = client.modifyCall(new CallModifier("93137ee3-580e-45f7-a61a-e0b5716000ef", ModifyCallAction.HANGUP));
        assertEquals("Received", call.getMessage());
    }

    @Test
    public void testTransferCall() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200, "{\"message\":\"Received\"}"));
        final String url = "https://example.com/ncco2";
        ModifyCallResponse call = client.transferCall("93137ee3-580e-45f7-a61a-e0b5716000ef", url);
        assertEquals("Received", call.getMessage());
        assertThrows(IllegalArgumentException.class, () ->
                client.transferCall("93137ee3-580e-45f7-a61a-e0b5716000ef", "';,x^")
        );
        assertThrows(NullPointerException.class, () -> client.transferCall(null, url));
    }

    @Test
    public void testTransferCallInlineNcco() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200, "{\"message\":\"Received\"}"));
        ModifyCallResponse call = client.transferCall("93137ee3-580e-45f7-a61a-e0b5716000ef",
                new Ncco(TalkAction.builder("Thank you for calling!").build(), TalkAction.builder("Bye!").build())
        );
        assertEquals("Received", call.getMessage());
        assertThrows(NullPointerException.class, () ->
                client.transferCall("93137ee3-580e-45f7-a61a-e0b5716000ef", (Ncco) null)
        );
    }

    @Test
    public void testStartStreamNonLooping() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"message\": \"Stream started\",\n"
                        + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n" + "}"
        ));
        final String url = "https://nexmo-community.github.io/ncco-examples/assets/voice_api_audio_streaming.mp3";
        StreamResponse response = client.startStream("944dd293-ca13-4a58-bc37-6252e11474be", url);
        assertEquals("Stream started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
        assertThrows(NullPointerException.class, () -> client.startStream(null, url));
        assertThrows(IllegalArgumentException.class, () -> client.startStream("944dd293-ca13-4a58-bc37-6252e11474be", null));
    }

    @Test
    public void testStartStreamLooping() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"message\": \"Stream started\",\n"
                        + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n" + "}"
        ));
        StreamResponse response = client.startStream(
                "944dd293-ca13-4a58-bc37-6252e11474be",
                "https://nexmo-community.github.io/ncco-examples/assets/voice_api_audio_streaming.mp3",
                5
        );
        assertEquals("Stream started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }

    @Test
    public void testStopStream() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"message\": \"Stream stopped\",\n"
                        + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n" + "}\n"
        ));

        StreamResponse response = client.stopStream("944dd293-ca13-4a58-bc37-6252e11474be");
        assertEquals("Stream stopped", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }

    @Test
    public void testStartTalkAllParamsModern() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        ));

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be",
                TalkPayload.builder("Bonjour, monde!")
                        .style(1).level(-0.5).loop(3).premium(false)
                        .language(TextToSpeechLanguage.FRENCH).build()
        );
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
        assertThrows(NullPointerException.class, () ->
                client.startTalk(null, TalkPayload.builder("Hi").build())
        );
        assertThrows(NullPointerException.class, () ->
                client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be", (TalkPayload) null)
        );
    }

    @Test
    public void testStartTalkAllParamsLegacy() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        ));

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be",
                "Hello World",
                TextToSpeechLanguage.GREEK,
                1,
                8
        );
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }

    @Test
    public void testStartTalkNonLooping() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
            "{\n" +
                    "  \"message\": \"Talk started\",\n" +
                    "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n" +
                    "}\n"
        ));

        TalkResponse response = client.startTalk(
                "944dd293-ca13-4a58-bc37-6252e11474be",
                "Hello World", TextToSpeechLanguage.KOREAN,
                1
        );
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }

    @Test
    public void testStartTalkLoopingWithDefaultVoice() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        ));

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be", "Hello World", 3);
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }

    @Test
    public void testStartTalkWithLanguageAndStyle() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        ));

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be", "Hello World", TextToSpeechLanguage.AMERICAN_ENGLISH, 5);
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }

    @Test
    public void testStartTalkWithLanguageStyleAndLoop() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        ));

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be", "Hello World", TextToSpeechLanguage.AMERICAN_ENGLISH, 5,1);
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }

    @Test
    public void testStartTalkWithLanguage() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        ));

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be", "Hello World", TextToSpeechLanguage.AMERICAN_ENGLISH);
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }


    @Test
    public void testStartTalkNonLoopingWithDefaultVoice() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        ));

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be", "Hello World");
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }

    @Test
    public void testStopTalk() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200,
                "{\n" + "  \"message\": \"Talk stopped\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        ));

        TalkResponse response = client.stopTalk("944dd293-ca13-4a58-bc37-6252e11474be");
        assertEquals("Talk stopped", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
        assertThrows(IllegalArgumentException.class, () -> client.stopTalk("Blah"));
    }

    @Test
    public void testDownloadRecording() throws Exception {
        VoiceClient client = new VoiceClient(stubHttpWrapper(200, "Bleep bloop"));
        Recording recording = client.downloadRecording("http://example.org/sample");
        String content = new Scanner(recording.getContent()).useDelimiter("\\A").next();
        assertEquals("Bleep bloop", content);
        assertThrows(IllegalArgumentException.class, () -> client.downloadRecording(",,[]}{{}D:sd"));
        assertThrows(IllegalArgumentException.class, () -> client.downloadRecording(null));
        assertThrows(IllegalArgumentException.class, () -> client.downloadRecording(""));
    }
}
