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
package com.vonage.client.voice;

import com.vonage.client.AbstractClientTest;
import com.vonage.client.RestEndpoint;
import com.vonage.client.TestUtils;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.voice.ncco.Ncco;
import com.vonage.client.voice.ncco.TalkAction;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class VoiceClientTest extends AbstractClientTest<VoiceClient> {
    static final UUID SAMPLE_CALL_UUID = UUID.randomUUID();
    static final String SAMPLE_CALL_ID = SAMPLE_CALL_UUID.toString();

    public VoiceClientTest() {
        client = new VoiceClient(wrapper);
    }

    void assert401Response(Executable invocation) throws Exception {
        assert401ApiResponseException(VoiceResponseException.class, invocation);
    }

    @Test
    public void testVerifySignature() {
        String secret = "XsA09z2MhUxYcdbXaUX3aTT7TzGmnCLfkdILf0NIyC9hN9criTEUdlI3OZ5hRjR";
        String header = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String payload = "eyJzdWIiOiJTaW5hIiwibmFtZSI6IkphdmFfU0RLIiwiaWF0IjoxNjk4NjgwMzkyfQ";
        String signature = "4qJpi46NSYURiLI1xoLIfGRygA8IUI2QSG9P2Kus1Oo";
        String token = header + '.' + payload + '.' + signature;
        assertTrue(VoiceClient.verifySignature(token, secret));
        String badToken = header + '.' + payload + '.' + "XsaXHXqxe2kfIbPy-JH2J6hfbHnEv8jdWsOhEuvzU98";
        assertFalse(VoiceClient.verifySignature(badToken, secret));
        assertThrows(NullPointerException.class, () -> VoiceClient.verifySignature(null, secret));
        assertThrows(NullPointerException.class, () -> VoiceClient.verifySignature(token, null));
    }

    @Test
    public void testCreateCall() throws Exception {
        String expectedJson = "{\n" + "  \"conversation_uuid\": \"63f61863-4a51-4f6b-86e1-46edebio0391\",\n"
                + "  \"uuid\": \"" + SAMPLE_CALL_ID + "\",\n"
                + "  \"status\": \"started\",\n" + "  \"direction\": \"outbound\"\n" + "}";
        stubResponse(200, expectedJson);
        CallEvent evt = client.createCall(new Call("447700900903", "447700900904", "http://api.example.com/answer"));
        assertEquals(CallEvent.fromJson(expectedJson), evt);
        assertEquals("63f61863-4a51-4f6b-86e1-46edebio0391", evt.getConversationUuid());
        assertEquals(SAMPLE_CALL_ID, evt.getUuid());
        assertEquals(CallDirection.OUTBOUND, evt.getDirection());
        assert401Response(() -> client.createCall(Call.builder().to(new VbcEndpoint("123")).build()));
    }

    @Test
    public void testListCallsNoFilter() throws Exception {
        stubResponse(200,
                "{\n" + "  \"page_size\": 10,\n" + "  \"record_index\": 0,\n" + "  \"count\": 0,\n"
                        + "  \"_embedded\": {\n" + "    \"calls\": []\n" + "  },\n" + "  \"_links\": {\n"
                        + "    \"self\": {\n" + "      \"href\": \"/v1/calls?page_size=10&record_index=0\"\n"
                        + "    },\n" + "    \"first\": {\n" + "      \"href\": \"/v1/calls?page_size=10\"\n"
                        + "    },\n" + "    \"last\": {\n" + "      \"href\": \"/v1/calls?page_size=10\"\n" + "    }\n"
                        + "  }\n" + "}\n"
        );
        CallInfoPage page = client.listCalls();
        testJsonableBaseObject(page);
        assertEquals(0, page.getCount());
        assert401Response(client::listCalls);
    }

    @Test
    public void testListCallsWithFilter() throws Exception {
        stubResponse(200,
                "{\n" + "  \"page_size\": 10,\n" + "  \"record_index\": 0,\n" + "  \"count\": 0,\n"
                        + "  \"_embedded\": {\n" + "    \"calls\": []\n" + "  },\n" + "  \"_links\": {\n"
                        + "    \"self\": {\n" + "      \"href\": \"/v1/calls?page_size=10&record_index=0\"\n"
                        + "    },\n" + "    \"first\": {\n" + "      \"href\": \"/v1/calls?page_size=10\"\n"
                        + "    },\n" + "    \"last\": {\n" + "      \"href\": \"/v1/calls?page_size=10\"\n" + "    }\n"
                        + "  }\n" + "}\n"
        );
        CallInfoPage page = client.listCalls(CallsFilter.builder().build());
        assertEquals(0, page.getCount());
    }

    @Test
    public void testGetCallDetails() throws Exception {
        stubResponse(200,
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
        );
        CallInfo call = client.getCallDetails("93137ee3-580e-45f7-a61a-e0b5716000ef");
        testJsonableBaseObject(call, true);
        assertEquals("93137ee3-580e-45f7-a61a-e0b5716000ef", call.getUuid());
        assert401Response(() -> client.getCallDetails(SAMPLE_CALL_ID));
    }

    @Test
    public void testSendDtmf() throws Exception {
        stubResponse(200,
                "{\n" + "  \"message\": \"DTMF sent\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}"
        );

        DtmfResponse response = client.sendDtmf("944dd293-ca13-4a58-bc37-6252e11474be", "332393");
        testJsonableBaseObject(response);
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
        assertEquals("DTMF sent", response.getMessage());
        assertThrows(IllegalArgumentException.class, () ->
                client.sendDtmf("944dd293-ca13-4a58-bc37-6252e11474be", null)
        );
        assertThrows(NullPointerException.class, () -> client.sendDtmf(null, "1234"));
        assert401Response(() -> client.sendDtmf(SAMPLE_CALL_ID, "789#0"));
    }

    @Test
    public void testTerminateCall() throws Exception {
        stubResponseAndRun(204, () -> client.terminateCall(SAMPLE_CALL_ID));
        stubResponseAndAssertThrows(204, () -> client.terminateCall(null), NullPointerException.class);
        stubResponseAndAssertThrows(404, () -> client.terminateCall(SAMPLE_CALL_ID), VoiceResponseException.class);
        assert401Response(() -> client.terminateCall(SAMPLE_CALL_ID));
    }

    @Test
    public void testMuteCall() throws Exception {
        stubResponseAndRun(204, () -> client.muteCall(SAMPLE_CALL_ID));
        stubResponseAndAssertThrows(204, () -> client.muteCall(null), NullPointerException.class);
        stubResponseAndAssertThrows(404, () -> client.muteCall(SAMPLE_CALL_ID), VoiceResponseException.class);
        assert401Response(() -> client.muteCall(SAMPLE_CALL_ID));
    }

    @Test
    public void testUnmuteCall() throws Exception {
        stubResponseAndRun(204, () -> client.unmuteCall(SAMPLE_CALL_ID));
        stubResponseAndAssertThrows(204, () -> client.unmuteCall(null), NullPointerException.class);
        stubResponseAndAssertThrows(404, () -> client.unmuteCall(SAMPLE_CALL_ID), VoiceResponseException.class);
        assert401Response(() -> client.unmuteCall(SAMPLE_CALL_ID));
    }

    @Test
    public void testEarmuffCall() throws Exception {
        stubResponseAndRun(204, () -> client.earmuffCall(SAMPLE_CALL_ID));
        stubResponseAndAssertThrows(204, () -> client.earmuffCall(null), NullPointerException.class);
        stubResponseAndAssertThrows(404, () -> client.earmuffCall(SAMPLE_CALL_ID), VoiceResponseException.class);
        assert401Response(() -> client.earmuffCall(SAMPLE_CALL_ID));
    }

    @Test
    public void testUnearmuffCall() throws Exception {
        stubResponseAndRun(204, () -> client.unearmuffCall(SAMPLE_CALL_ID));
        stubResponseAndAssertThrows(204, () -> client.unearmuffCall(null), NullPointerException.class);
        stubResponseAndAssertThrows(404, () -> client.unearmuffCall(SAMPLE_CALL_ID), VoiceResponseException.class);
        assert401Response(() -> client.unearmuffCall(SAMPLE_CALL_ID));
    }

    @Test
    public void testTransferCall() throws Exception {
        stubResponse(200, "{\"message\":\"Received\"}");
        final String url = "https://example.com/ncco2";
        client.transferCall("93137ee3-580e-45f7-a61a-e0b5716000ef", url);
        assertThrows(IllegalArgumentException.class, () ->
                client.transferCall("93137ee3-580e-45f7-a61a-e0b5716000ef", "';,x^")
        );
        assertThrows(NullPointerException.class, () -> client.transferCall(null, url));
        assert401Response(() -> client.transferCall(SAMPLE_CALL_ID, url));
    }

    @Test
    public void testTransferCallInlineNcco() throws Exception {
        stubResponse(200, "{\"message\":\"Received\"}");
        client.transferCall("93137ee3-580e-45f7-a61a-e0b5716000ef", new Ncco(
                TalkAction.builder("Thank you for calling!").build(),
                TalkAction.builder("Bye!").build()
        ));
        assertThrows(NullPointerException.class, () ->
                client.transferCall("93137ee3-580e-45f7-a61a-e0b5716000ef", (Ncco) null)
        );
    }

    @Test
    public void testStartStreamNonLooping() throws Exception {
        stubResponse(200,
                "{\n" + "  \"message\": \"Stream started\",\n"
                        + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n" + "}"
        );
        final String url = "https://nexmo-community.github.io/ncco-examples/assets/voice_api_audio_streaming.mp3";
        StreamResponse response = client.startStream("944dd293-ca13-4a58-bc37-6252e11474be", url);
        assertEquals("Stream started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
        assertThrows(NullPointerException.class, () -> client.startStream(null, url));
        assertThrows(IllegalArgumentException.class, () -> client.startStream("944dd293-ca13-4a58-bc37-6252e11474be", null));
        assert401Response(() -> client.startStream(SAMPLE_CALL_ID, url));
    }

    @Test
    public void testStartStreamLooping() throws Exception {
        stubResponse(200,
                "{\n" + "  \"message\": \"Stream started\",\n"
                        + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n" + "}"
        );
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
        stubResponse(200,
                "{\n" + "  \"message\": \"Stream stopped\",\n"
                        + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n" + "}\n"
        );

        StreamResponse response = client.stopStream("944dd293-ca13-4a58-bc37-6252e11474be");
        testJsonableBaseObject(response);
        assertEquals("Stream stopped", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
        assert401Response(() -> client.stopStream(SAMPLE_CALL_ID));
    }

    @Test
    public void testStartTalkAllParamsModern() throws Exception {
        stubResponse(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        );

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be",
                TalkPayload.builder("Bonjour, monde!")
                        .style(1).level(-0.5).loop(3).premium(false)
                        .language(TextToSpeechLanguage.FRENCH).build()
        );
        testJsonableBaseObject(response);
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
        assertThrows(NullPointerException.class, () ->
                client.startTalk(null, TalkPayload.builder("Hi").build())
        );
        assertThrows(NullPointerException.class, () ->
                client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be", (TalkPayload) null)
        );
        assert401Response(() -> client.startTalk(SAMPLE_CALL_ID, TalkPayload.builder("Hey up").build()));
    }

    @Test
    public void testStartTalkAllParamsLegacy() throws Exception {
        stubResponse(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        );

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
        stubResponse(200,
            "{\n" +
                    "  \"message\": \"Talk started\",\n" +
                    "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n" +
                    "}\n"
        );

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
        stubResponse(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        );

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be", "Hello World", 3);
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }

    @Test
    public void testStartTalkWithLanguageAndStyle() throws Exception {
        stubResponse(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        );

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be", "Hello World", TextToSpeechLanguage.AMERICAN_ENGLISH, 5);
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }

    @Test
    public void testStartTalkWithLanguageStyleAndLoop() throws Exception {
        stubResponse(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        );

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be", "Hello World", TextToSpeechLanguage.AMERICAN_ENGLISH, 5,1);
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }

    @Test
    public void testStartTalkWithLanguage() throws Exception {
        stubResponse(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        );

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be", "Hello World", TextToSpeechLanguage.AMERICAN_ENGLISH);
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }


    @Test
    public void testStartTalkNonLoopingWithDefaultVoice() throws Exception {
        stubResponse(200,
                "{\n" + "  \"message\": \"Talk started\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        );

        TalkResponse response = client.startTalk("944dd293-ca13-4a58-bc37-6252e11474be", "Hello World");
        assertEquals("Talk started", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
    }

    @Test
    public void testStopTalk() throws Exception {
        stubResponse(200,
                "{\n" + "  \"message\": \"Talk stopped\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}\n"
        );

        TalkResponse response = client.stopTalk("944dd293-ca13-4a58-bc37-6252e11474be");
        assertEquals("Talk stopped", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
        assertThrows(NullPointerException.class, () -> client.stopTalk(null));
        assert401Response(() -> client.stopTalk(SAMPLE_CALL_ID));
    }

    @Test
    public void testAddDtmfListener() throws Exception {
        stubResponse(200);
        var url = "https://example.app/webhooks/answer";
        client.addDtmfListener(SAMPLE_CALL_ID, url);
        assertThrows(NullPointerException.class, () -> client.addDtmfListener(null, url));
        assertThrows(IllegalArgumentException.class, () -> client.addDtmfListener(SAMPLE_CALL_ID, null));
        assert401Response(() -> client.addDtmfListener(SAMPLE_CALL_ID, url));
    }

    @Test
    public void removeDtmfListener() throws Exception {
        stubResponse(204);
        client.removeDtmfListener(SAMPLE_CALL_ID);
        assertThrows(NullPointerException.class, () -> client.removeDtmfListener(null));
        assert401Response(() -> client.removeDtmfListener(SAMPLE_CALL_ID));
    }

    @Test
    public void testDownloadRecording() throws Exception {
        String recordingId = UUID.randomUUID().toString();
        String content = "<BINARY>";
        stubResponse(200, content);
        String url = "https://api.nexmo.com/v1/files/" + recordingId;
        Path temp = Files.createTempFile(null, null);
        Files.delete(temp);

        stubResponse(200, content);
        client.saveRecording(url, temp);
        assertArrayEquals(content.getBytes(), Files.readAllBytes(temp));
        stubResponse(200, content);
        client.saveRecording(url, temp.getParent());
        Path recordingPath = temp.resolveSibling(recordingId);
        assertTrue(Files.exists(recordingPath));
        assertArrayEquals(content.getBytes(), Files.readAllBytes(recordingPath));

        stubResponseAndAssertThrows(content, () ->
                client.saveRecording("ftp:///myserver.co.uk/rec.mp3", recordingPath),
                IllegalArgumentException.class
        );
        stubResponseAndAssertThrows(content, () ->
                client.saveRecording("http://example.org/recording.wav", recordingPath),
                IllegalArgumentException.class
        );
        stubResponseAndAssertThrows(content, () ->
                client.saveRecording("https://example.org/recording.wav", recordingPath),
                IllegalArgumentException.class
        );
        stubResponseAndAssertThrows(content, () ->
                client.saveRecording(url, null),
                NullPointerException.class
        );
        stubResponseAndAssertThrows(content, () ->
                client.saveRecording("not-a-url", recordingPath),
                IllegalArgumentException.class
        );
    }

    // ENDPOINT TESTS

    @Test
    public void testAddDtmfListenerEndpoint() throws Exception {
        new VoiceEndpointTestSpec<AddDtmfListenerRequest, Void>() {
            private final String uuid = SAMPLE_CALL_ID;
            private final String url = "https://example.org/webhooks/answer";

            @Override
            protected RestEndpoint<AddDtmfListenerRequest, Void> endpoint() {
                return client.addDtmfListener;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.PUT;
            }

            @Override
            protected String expectedEndpointUri(AddDtmfListenerRequest request) {
                return "/v1/calls/" + request.uuid + "/input/dtmf";
            }

            @Override
            protected AddDtmfListenerRequest sampleRequest() {
                return new AddDtmfListenerRequest(uuid, URI.create(url));
            }

            @Override
            protected String sampleRequestBodyString() {
                return "{\"eventUrl\":[\""+url+"\"]}";
            }
        }
        .runTests();
    }

    @Test
    public void testRemoveDtmfListenerEndpoint() throws Exception {
        new VoiceEndpointTestSpec<String, Void>() {
            private final String uuid = SAMPLE_CALL_ID;

            @Override
            protected RestEndpoint<String, Void> endpoint() {
                return client.removeDtmfListener;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.DELETE;
            }

            @Override
            protected String expectedEndpointUri(String request) {
                return "/v1/calls/" + request + "/input/dtmf";
            }

            @Override
            protected String sampleRequest() {
                return uuid;
            }
        }
        .runTests();
    }

    @Test
    public void testDownloadRecordingEndpoint() throws Exception {
        new VoiceEndpointTestSpec<String, byte[]>() {

            @Override
            protected RestEndpoint<String, byte[]> endpoint() {
                return client.downloadRecording;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }

            @Override
            protected String expectedEndpointUri(String request) {
                return request;
            }

            @Override
            protected String expectedDefaultBaseUri() {
                return "";
            }

            @Override
            protected String customBaseUri() {
                return expectedDefaultBaseUri();
            }

            @Override
            protected String sampleRequest() {
                return "http://example.org/sample";
            }
        }
        .runTests();
    }

    @Test
    public void testCreateCallEndpoint() throws Exception {
        new VoiceEndpointTestSpec<Call, CallEvent>() {
            private final String answer = "https://example.com/answer", to ="447700900903", from = "447700900904";

            @Override
            protected RestEndpoint<Call, CallEvent> endpoint() {
                return client.createCall;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected String expectedEndpointUri(Call request) {
                return "/v1/calls";
            }

            @Override
            protected Call sampleRequest() {
                return new Call(to, from, answer);
            }

            @Override
            protected String sampleRequestBodyString() {
                return "{\"to\":[{\"number\":\""+to+"\",\"type\":\"phone\"}]," +
                        "\"from\":{\"number\":\""+from+"\",\"type\":\"phone\"}," +
                        "\"answer_method\":\"GET\",\"answer_url\":[\""+answer+"\"]}";
            }
        }
        .runTests();
    }

    @Test
    public void testGetCallEndpoint() throws Exception {
        new VoiceEndpointTestSpec<String, CallInfo>() {

            @Override
            protected RestEndpoint<String, CallInfo> endpoint() {
                return client.getCall;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }

            @Override
            protected String expectedEndpointUri(String request) {
                return "/v1/calls/" + request;
            }

            @Override
            protected String sampleRequest() {
                return SAMPLE_CALL_ID;
            }
        }
        .runTests();
    }

    @Test
    public void testListCallsEndpoint() throws Exception {
        new VoiceEndpointTestSpec<CallsFilter, CallInfoPage>() {

            @Override
            protected RestEndpoint<CallsFilter, CallInfoPage> endpoint() {
                return client.listCalls;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }

            @Override
            protected String expectedEndpointUri(CallsFilter request) {
                return "/v1/calls";
            }

            @Override
            protected CallsFilter sampleRequest() {
                return CallsFilter.builder().pageSize(3).build();
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                CallsFilter request = sampleRequest();
                Map<String, String> params = new LinkedHashMap<>(2);
                params.put("page_size", request.getPageSize().toString());
                return params;
            }
        }
        .runTests();
    }

    @Test
    public void testModifyCallEndpoint() throws Exception {
        new VoiceEndpointTestSpec<ModifyCallPayload, Void>() {
            private ModifyCallAction action;

            @Override
            protected RestEndpoint<ModifyCallPayload, Void> endpoint() {
                return client.modifyCall;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.PUT;
            }

            @Override
            protected String expectedEndpointUri(ModifyCallPayload request) {
                return "/v1/calls/" + request.uuid;
            }

            @Override
            protected ModifyCallPayload sampleRequest() {
                ModifyCallAction[] actions = ModifyCallAction.values();
                action = actions[(int) (Math.random() * actions.length)];
                ModifyCallPayload request = new ModifyCallPayload(action, SAMPLE_CALL_ID);
                return request;
            }

            @Override
            protected String sampleRequestBodyString() {
                return "{\"action\":\""+action.name().toLowerCase()+"\"}";
            }
        }
        .runTests();
    }

    @Test
    public void testSendDtmfEndpoint() throws Exception {
        new VoiceEndpointTestSpec<DtmfPayload, DtmfResponse>() {

            @Override
            protected RestEndpoint<DtmfPayload, DtmfResponse> endpoint() {
                return client.sendDtmf;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.PUT;
            }

            @Override
            protected String expectedEndpointUri(DtmfPayload request) {
                return "/v1/calls/"+request.uuid+"/dtmf";
            }

            @Override
            protected DtmfPayload sampleRequest() {
                return new DtmfPayload("867", SAMPLE_CALL_ID);
            }

            @Override
            protected String sampleRequestBodyString() {
                return "{\"digits\":\""+sampleRequest().getDigits()+"\"}";
            }
        }
        .runTests();
    }

    @Test
    public void testStartStreamEndpoint() throws Exception {
        new VoiceEndpointTestSpec<StreamPayload, StreamResponse>() {

            @Override
            protected RestEndpoint<StreamPayload, StreamResponse> endpoint() {
                return client.startStream;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.PUT;
            }

            @Override
            protected String expectedEndpointUri(StreamPayload request) {
                return "/v1/calls/"+request.uuid+"/stream";
            }

            @Override
            protected StreamPayload sampleRequest() {
                return new StreamPayload("https://example.com/waiting.mp3", 1, 0.4, SAMPLE_CALL_ID);
            }

            @Override
            protected String sampleRequestBodyString() {
                StreamPayload request = sampleRequest();
                return "{\"stream_url\":[\""+request.getStreamUrl()[0] +
                        "\"],\"loop\":"+request.getLoop()+",\"level\":"+request.getLevel()+"}";
            }
        }
        .runTests();
    }

    @Test
    public void testStartTalkEndpoint() throws Exception {
        new VoiceEndpointTestSpec<TalkPayload, TalkResponse>() {

            @Override
            protected RestEndpoint<TalkPayload, TalkResponse> endpoint() {
                return client.startTalk;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.PUT;
            }

            @Override
            protected String expectedEndpointUri(TalkPayload request) {
                return "/v1/calls/"+request.uuid+"/talk";
            }

            @Override
            protected TalkPayload sampleRequest() {
                TalkPayload request = TalkPayload.builder("Sample text").build();
                request.uuid = SAMPLE_CALL_ID;
                return request;
            }
        }
        .runTests();
    }

    @Test
    public void testStopStreamEndpoint() throws Exception {
        new VoiceEndpointTestSpec<String, StreamResponse>() {

            @Override
            protected RestEndpoint<String, StreamResponse> endpoint() {
                return client.stopStream;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.DELETE;
            }

            @Override
            protected String expectedEndpointUri(String request) {
                return "/v1/calls/"+request+"/stream";
            }

            @Override
            protected String sampleRequest() {
                return SAMPLE_CALL_ID;
            }
        }
        .runTests();
    }

    @Test
    public void testStopTalkEndpoint() throws Exception {
        new VoiceEndpointTestSpec<String, TalkResponse>() {

            @Override
            protected RestEndpoint<String, TalkResponse> endpoint() {
                return client.stopTalk;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.DELETE;
            }

            @Override
            protected String expectedEndpointUri(String request) {
                return "/v1/calls/"+request+"/talk";
            }

            @Override
            protected String sampleRequest() {
                return SAMPLE_CALL_ID;
            }
        }
        .runTests();
    }
}
