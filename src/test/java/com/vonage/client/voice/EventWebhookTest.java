/*
 *   Copyright 2025 Vonage
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

import com.vonage.client.TestUtils;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class EventWebhookTest {

    @Test
    public void testSerdesAllFields() {
        String from = "442079460000",
                to = "447700900000",
                status = "completed",
                disconnectedBy = "platform",
                subState = "beep_start",
                conversationUuid = "CON-aaaaaaaa-bbbb-cccc-dddd-0123456789ab",
                direction = "inbound",
                timestamp = "2020-01-01T12:05:01.200Z",
                detail = "unavailable",
                endTime = "2024-01-09T11:01:58.324Z",
                startTime = "2023-12-30T23:59:59.999Z",
                network = "GB-FIXED",
                conversationUuidFrom = "CON-aaaaaaaa-bbbb-cccc-dddd-0123456789ab",
                conversationUuidTo = "CON-aaaaaaaa-bbbb-cccc-dddd-0123456789ab",
                reason = "Syntax error in NCCO. Invalid value type or action.",
                dtmfDigits = "42",
                speechTimeoutReason = "end_on_silence_timeout",
                speechError = "ERR1: Failed to analyze audio",
                recordingUrl = "https://api-us.nexmo.com/v1/files/eeeeeee-ffff-0123-4567-0123456789ab",
                speechResultText1 = "sales",
                speechResultText2 = "customer";

        String callUuid = UUID.randomUUID().toString().replace("-", ""),
                recordingUuid = UUID.randomUUID().toString().replace("-", "");

        boolean dtmfTimedOut = true;
        int duration = 6, size = 12228;
        double rate = 0.00450000,
                price = 0.00015000,
                speechResultConfidence1 = 0.9405097,
                speechResultConfidence2 = 0.8764321;

        String json = "{\n" +
                "  \"call_uuid\": \"" + callUuid + "\",\n" +
                "  \"from\": \"" + from + "\",\n" +
                "  \"to\": \"" + to + "\",\n" +
                "  \"status\": \"" + status + "\",\n" +
                "  \"disconnected_by\": \"" + disconnectedBy + "\",\n" +
                "  \"sub_state\": \"" + subState + "\",\n" +
                "  \"conversation_uuid\": \"" + conversationUuid + "\",\n" +
                "  \"direction\": \"" + direction + "\",\n" +
                "  \"timestamp\": \"" + timestamp + "\",\n" +
                "  \"detail\": \"" + detail + "\",\n" +
                "  \"end_time\": \"" + endTime + "\",\n" +
                "  \"network\": \"" + network + "\",\n" +
                "  \"duration\": " + duration + ",\n" +
                "  \"start_time\": \"" + startTime + "\",\n" +
                "  \"size\": " + size + ",\n" +
                "  \"rate\": " + rate + ",\n" +
                "  \"price\": " + price + ",\n" +
                "  \"conversation_uuid_from\": \"" + conversationUuidFrom + "\",\n" +
                "  \"conversation_uuid_to\": \"" + conversationUuidTo + "\",\n" +
                "  \"reason\": \"" + reason + "\",\n" +
                "  \"recording_url\": \"" + recordingUrl + "\",\n" +
                "  \"recording_uuid\": \"" + recordingUuid + "\",\n" +
                "  \"dtmf\": {\n" +
                "    \"digits\": \"" + dtmfDigits + "\",\n" +
                "    \"timed_out\": " + dtmfTimedOut + "\n" +
                "  },\n" +
                "  \"speech\": {\n" +
                "    \"timeout_reason\": \"" + speechTimeoutReason + "\",\n" +
                "    \"results\": [\n" +
                "      {\n" +
                "        \"text\": \"" + speechResultText1 + "\",\n" +
                "        \"confidence\": " + speechResultConfidence1 + "\n" +
                "      },\n" +
                "      {\n" +
                "        \"text\": \"" + speechResultText2 + "\",\n" +
                "        \"confidence\": " + speechResultConfidence2 + "\n" +
                "      }\n" +
                "    ],\n" +
                "    \"error\": \"" + speechError + "\",\n" +
                "    \"recording_url\": \"" + recordingUrl + "\"\n" +
                "  }\n" +
                "}\n";

        EventWebhook event = EventWebhook.fromJson(json);
        testJsonableBaseObject(event);
        assertEquals(callUuid, event.getCallUuid());
        assertEquals(conversationUuid, event.getConversationUuid());
        assertEquals(conversationUuidFrom, event.getConversationUuidFrom());
        assertEquals(conversationUuidTo, event.getConversationUuidTo());
        assertEquals(from, event.getFrom());
        assertEquals(to, event.getTo());
        assertEquals(status, event.getStatus().toString());
        assertEquals(disconnectedBy, event.getDisconnectedBy().toString());
        assertEquals(subState, event.getMachineDetectionSubstate().toString());
        assertEquals(detail, event.getDetail().toString());
        assertEquals(direction, event.getDirection().toString());
        assertEquals(Instant.parse(timestamp), event.getTimestamp());
        assertEquals(Instant.parse(startTime), event.getStartTime());
        assertEquals(Instant.parse(endTime), event.getEndTime());
        assertEquals(URI.create(recordingUrl), event.getRecordingUrl());
        assertEquals(recordingUuid, event.getRecordingUuid());
        assertEquals(reason,  event.getReason());
        assertEquals(network, event.getNetwork());
        assertEquals(size, event.getSize());
        assertEquals(rate, event.getRate());
        assertEquals(price, event.getPrice());
        assertEquals(duration, event.getDuration());
        DtmfResult dtmf = event.getDtmf();
        assertNotNull(dtmf);
        assertEquals(dtmfDigits, dtmf.getDigits());
        assertEquals(dtmfTimedOut, dtmf.isTimedOut());
        SpeechResults speech = event.getSpeech();
        assertNotNull(speech);
        List<SpeechTranscript> speechResults = speech.getResults();
        assertNotNull(speechResults);
        assertEquals(2, speechResults.size());
        SpeechTranscript speechResult1 = speechResults.getFirst();
        assertNotNull(speechResult1);
        assertEquals(speechResultConfidence1, speechResult1.getConfidence());
        assertEquals(speechResultText1, speechResult1.getText());
        SpeechTranscript speechResult2 = speechResults.get(1);
        assertNotNull(speechResult2);
        assertEquals(speechResultConfidence2, speechResult2.getConfidence());
        assertEquals(speechResultText2, speechResult2.getText());
        assertEquals(speechError, speech.getError());
        assertEquals(speechTimeoutReason, speech.getTimeoutReason().toString());
        assertEquals(URI.create(recordingUrl), speech.getRecordingUrl());
    }

    @Test
    public void testParseEmptyJson() {
        EventWebhook event = EventWebhook.fromJson("{}");
        testJsonableBaseObject(event);
        assertNull(event.getDetail());
        assertNull(event.getCallUuid());
        assertNull(event.getReason());
        assertNull(event.getSize());
        assertNull(event.getRecordingUuid());
        assertNull(event.getRecordingUrl());
        assertNull(event.getDuration());
        assertNull(event.getPrice());
        assertNull(event.getRate());
        assertNull(event.getNetwork());
        assertNull(event.getEndTime());
        assertNull(event.getStartTime());
        assertNull(event.getTimestamp());
        assertNull(event.getFrom());
        assertNull(event.getTo());
        assertNull(event.getConversationUuid());
        assertNull(event.getConversationUuidTo());
        assertNull(event.getConversationUuidFrom());
        assertNull(event.getDirection());
        assertNull(event.getMachineDetectionSubstate());
        assertNull(event.getDisconnectedBy());
        assertNull(event.getStatus());
        assertNull(event.getDtmf());
        assertNull(event.getSpeech());
    }

    @Test
    public void testCallUuidPrimaryOnly() {
        String callUuid = UUID.randomUUID().toString().replace("-", ""),
                uuid = UUID.randomUUID().toString().replace("-", "");
        String json = "{\"call_uuid\":\""+callUuid+"\",\"uuid\":\""+uuid+"\"}";
        EventWebhook event = EventWebhook.fromJson(json);
        testJsonableBaseObject(event);
        assertEquals(uuid, event.getCallUuid());
        assertEquals(uuid, EventWebhook.fromJson("{\"uuid\": \""+uuid+"\"}").getCallUuid());
    }

    @Test
    public void testCallStatusDetail() {
        String json = "{\"status\":\"unanswered\",\"detail\":\"oops\"}";
        EventWebhook event = EventWebhook.fromJson(json);
        assertEquals(CallStatus.UNANSWERED, event.getStatus());
        assertEquals(CallStatusDetail.UNMAPPED_DETAIL, event.getDetail());
        assertEquals(CallStatusDetail.NO_DETAIL, CallStatusDetail.fromString(null));
    }

    @Test
    public void testMachineDetectionStatusUnknown() {
        CallStatus status = CallStatus.REJECTED;
        String json = "{\"status\":\"" + status + "\",\"sub_state\": \"L0L\"}";
        EventWebhook event = EventWebhook.fromJson(json);
        assertEquals(status, event.getStatus());
        assertEquals(MachineDetectionStatus.UNKNOWN, event.getMachineDetectionSubstate());
    }

    @Test
    public void testSpeechTimeoutReasonUnknown() {
        CallStatus status = CallStatus.INPUT;
        String json = "{\n" +
            "  \"status\":\"" + status + "\",\n" +
            "  \"speech\": {\n" +
            "    \"timeout_reason\": \"who_knows\"\n" +
            "  }\n" +
            "}";
        EventWebhook event = EventWebhook.fromJson(json);
        assertEquals(status, event.getStatus());
        SpeechResults speech = event.getSpeech();
        assertNotNull(speech);
        assertEquals(SpeechTimeoutReason.UNKNOWN, speech.getTimeoutReason());
        assertNull(speech.getResults());
        assertNull(speech.getRecordingUrl());
        assertNull(speech.getError());
    }

    @Test
    public void testDisconnectedByEnum() {
        assertEquals(DisconnectedBy.PLATFORM, DisconnectedBy.fromString("platform"));
        assertEquals(DisconnectedBy.USER, DisconnectedBy.fromString("user"));
        assertNull(DisconnectedBy.fromString(null));
        assertEquals(DisconnectedBy.UNKNOWN, DisconnectedBy.fromString("Somebody Else"));
    }
}
