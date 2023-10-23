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

import com.vonage.client.ClientTest;
import com.vonage.client.RestEndpoint;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.voice.ncco.Ncco;
import com.vonage.client.voice.ncco.TalkAction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class VoiceClientTest extends ClientTest<VoiceClient> {
    static final String SAMPLE_CALL_ID = UUID.randomUUID().toString();

    public VoiceClientTest() {
        client = new VoiceClient(wrapper);
    }

    @Test
    public void testCreateCall() throws Exception {
        stubResponse(200,
                "{\n" + "  \"conversation_uuid\": \"63f61863-4a51-4f6b-86e1-46edebio0391\",\n"
                        + "  \"status\": \"started\",\n" + "  \"direction\": \"outbound\"\n" + "}"
        );
        CallEvent evt = client.createCall(new Call("447700900903", "447700900904", "http://api.example.com/answer"));
        assertEquals("63f61863-4a51-4f6b-86e1-46edebio0391", evt.getConversationUuid());
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
        assertEquals(0, page.getCount());
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
        assertEquals("93137ee3-580e-45f7-a61a-e0b5716000ef", call.getUuid());
    }

    @Test
    public void testSendDtmf() throws Exception {
        stubResponse(200,
                "{\n" + "  \"message\": \"DTMF sent\",\n" + "  \"uuid\": \"944dd293-ca13-4a58-bc37-6252e11474be\"\n"
                        + "}"
        );

        DtmfResponse response = client.sendDtmf("944dd293-ca13-4a58-bc37-6252e11474be", "332393");
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
        assertEquals("DTMF sent", response.getMessage());
        assertThrows(IllegalArgumentException.class, () ->
                client.sendDtmf("944dd293-ca13-4a58-bc37-6252e11474be", null)
        );
        assertThrows(NullPointerException.class, () -> client.sendDtmf(null, "1234"));
    }

    @Test
    public void testModifyCall() throws Exception {
        stubResponse(200, "{\"message\":\"Received\"}");
        ModifyCallResponse call = client.modifyCall("93137ee3-580e-45f7-a61a-e0b5716000ef", ModifyCallAction.HANGUP);
        assertEquals("Received", call.getMessage());
        assertThrows(NullPointerException.class, () -> client.modifyCall(null, ModifyCallAction.HANGUP));
        assertThrows(NullPointerException.class, () ->
                client.modifyCall("93137ee3-580e-45f7-a61a-e0b5716000ef", null)
        );
    }

    @Test
    public void testModifyCallLegacy() throws Exception {
        stubResponse(200, "{\"message\":\"Received\"}");
        ModifyCallResponse call = client.modifyCall(new CallModifier("93137ee3-580e-45f7-a61a-e0b5716000ef", ModifyCallAction.HANGUP));
        assertEquals("Received", call.getMessage());
    }

    @Test
    public void testTransferCall() throws Exception {
        stubResponse(200, "{\"message\":\"Received\"}");
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
        stubResponse(200, "{\"message\":\"Received\"}");
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
        assertEquals("Stream stopped", response.getMessage());
        assertEquals("944dd293-ca13-4a58-bc37-6252e11474be", response.getUuid());
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
    }

    @Test
    public void testDownloadRecording() throws Exception {
        stubResponse(200, "Bleep bloop");
        Recording recording = client.downloadRecording("http://example.org/sample");
        String content = new Scanner(recording.getContent()).useDelimiter("\\A").next();
        assertEquals("Bleep bloop", content);
        assertThrows(IllegalArgumentException.class, () -> client.downloadRecording(",,[]}{{}D:sd"));
        assertThrows(IllegalArgumentException.class, () -> client.downloadRecording(null));
        assertThrows(IllegalArgumentException.class, () -> client.downloadRecording(""));
    }

    // ENDPOINT TESTS

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
                return "/v1/calls/";
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
                return "/v1/calls/";
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
        new VoiceEndpointTestSpec<ModifyCallPayload, ModifyCallResponse>() {
            private ModifyCallAction action;

            @Override
            protected RestEndpoint<ModifyCallPayload, ModifyCallResponse> endpoint() {
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
                ModifyCallPayload request = new ModifyCallPayload(action);
                request.uuid = SAMPLE_CALL_ID;
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
                return new StreamPayload("https://example.com/waiting.mp3", 1, 0.4);
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
