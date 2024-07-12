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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents all call events sent to the {@code event_url} webhook configured in your Voice application settings.
 * See <a href=https://developer.vonage.com/en/voice/voice-api/webhook-reference#event-webhook>
 * the webhook reference</a> for details. For the {@code answer_url} webhook, use {@link AnswerWebhook}.
 * The fields present depends on which event has occurred. This class maps all known fields for all events.
 *
 * @since 8.2.0
 */
public class EventWebhook extends JsonableBaseObject {
    private CallStatus status;
    private CallDirection direction;
    private CallStatusDetail detail;
    private MachineDetectionStatus machineDetectionSubstate;
    private DtmfResult dtmf;
    private SpeechResults speech;
    private Instant timestamp, startTime, endTime;
    private Integer duration, size;
    private Double rate, price;
    private URI recordingUrl;
    private String to, from, network, reason,
            callUuid, recordingUuid, conversationUuid, conversationUuidFrom, conversationUuidTo;

    protected EventWebhook() {}

    /**
     * Event type.
     *
     * @return The call status as an enum.
     */
    @JsonProperty("status")
    public CallStatus getStatus() {
        return status;
    }

    /**
     * Call direction, can be either inbound or outbound.
     *
     * @return The call direction as an enum, or {@code null} if not applicable.
     */
    @JsonProperty("direction")
    public CallDirection getDirection() {
        return direction;
    }

    /**
     * Provides a more specific status to accompany {@linkplain #getStatus()}.
     *
     * @return The event status detail as an enum, or {@code null} if not applicable.
     */
    @JsonProperty("detail")
    public CallStatusDetail getDetail() {
        return detail;
    }

    /**
     * Advanced machine detection status, when call is answered by voicemail and the beep is detected. This is
     * present if {@linkplain #getStatus()} is {@linkplain CallStatus#HUMAN} or {@linkplain CallStatus#MACHINE}.
     *
     * @return The machine detection substate, or {@code null} if not applicable.
     */
    @JsonProperty("sub_state")
    public MachineDetectionStatus getMachineDetectionSubstate() {
        return machineDetectionSubstate;
    }

    /**
     * DTMF capturing results.
     * This is only present if {@linkplain #getStatus()} is {@linkplain CallStatus#INPUT}.
     *
     * @return The DTMF input, or {@code null} if not applicable.
     */
    @JsonProperty("dtmf")
    public DtmfResult getDtmf() {
        return dtmf;
    }

    /**
     * Speech recognition results.
     * This is only present if {@linkplain #getStatus()} is {@linkplain CallStatus#INPUT}.
     *
     * @return The speech properties, or {@code null} if not applicable.
     */
    @JsonProperty("speech")
    public SpeechResults getSpeech() {
        return speech;
    }

    /**
     * Event timestamp in ISO 8601 format.
     *
     * @return The timestamp as an Instant, or {@code null} if unknown.
     */
    @JsonProperty("timestamp")
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Start time in ISO 8601 format. This is applicable to recording events.
     *
     * @return The start time as an Instant, or {@code null} if unknown / not applicable.
     */
    @JsonProperty("start_time")
    public Instant getStartTime() {
        return startTime;
    }

    /**
     * End time in ISO 8601 format. This is applicable to recording events.
     *
     * @return The end time as an Instant, or {@code null} if unknown / not applicable.
     */
    @JsonProperty("end_time")
    public Instant getEndTime() {
        return endTime;
    }

    /**
     * Call length, in seconds. This is present if {@linkplain #getStatus()} is {@linkplain CallStatus#COMPLETED}.
     *
     * @return The length of the call, or {@code null} if not applicable.
     */
    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    /**
     * Size of the recording file, in bytes. This is present for recording events only.
     *
     * @return The file size in bytes, or {@code null} if not applicable.
     */
    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    /**
     * Cost per minute of the call, in Euros.
     * This will be present if {@linkplain #getStatus()} is {@linkplain CallStatus#COMPLETED}.
     *
     * @return The call rate as a double, or {@code null} if not applicable.
     */
    @JsonProperty("rate")
    public Double getRate() {
        return rate;
    }

    /**
     * Total cost of the call in Euros.
     * This will be present if {@linkplain #getStatus()} is {@linkplain CallStatus#COMPLETED}.
     *
     * @return The call cost as a double, or {@code null} if not applicable.
     */
    @JsonProperty("price")
    public Double getPrice() {
        return price;
    }

    /**
     * Where to download the recording. This is present for recording and transcription events only.
     *
     * @return The URL of the recording, or {@code null} if not applicable.
     */
    @JsonProperty("recording_url")
    public URI getRecordingUrl() {
        return recordingUrl;
    }

    /**
     * Unique identifier for the call event.
     *
     * @return The call ID as a string, or {@code null} not applicable.
     */
    @JsonProperty("uuid")
    @JsonAlias({"uuid", "call_uuid"})
    public String getCallUuid() {
        return callUuid;
    }

    /**
     * Unique identifier for the recording. This is only present for recording events.
     *
     * @return The recording ID as a string, or {@code null} if not applicable.
     */
    @JsonProperty("recording_uuid")
    public String getRecordingUuid() {
        return recordingUuid;
    }

    /**
     * Number the call was made to, in E.164 format.
     *
     * @return The call destination, or {@code null} if not applicable.
     */
    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    /**
     * Number the call came from, in E.164 format.
     *
     * @return The call source number, or {@code null} if not applicable.
     */
    @JsonProperty("from")
    public String getFrom() {
        return from;
    }

    /**
     * Unique identifier for the conversation. Starts with {@code CON-} followed by a UUID.
     *
     * @return The conversation ID as a string, or {@code null} if not applicable.
     */
    @JsonProperty("conversation_uuid")
    public String getConversationUuid() {
        return conversationUuid;
    }

    /**
     * Conversation ID that the leg was originally in.
     * This is only present if {@linkplain #getStatus()} is {@linkplain CallStatus#TRANSFER}.
     *
     * @return The originating conversation ID leg, or {@code null} if not applicable.
     */
    @JsonProperty("conversation_uuid_from")
    public String getConversationUuidFrom() {
        return conversationUuidFrom;
    }

    /**
     * Conversation ID that the leg was transferred to.
     * This is only present if {@linkplain #getStatus()} is {@linkplain CallStatus#TRANSFER}.
     *
     * @return The destination conversation ID leg, or {@code null} if not applicable.
     */
    @JsonProperty("conversation_uuid_to")
    public String getConversationUuidTo() {
        return conversationUuidTo;
    }

    /**
     * Type of network that was used in the call.
     *
     * @return The network, or {@code null} if unknown / not applicable.
     */
    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    /**
     * Information about the nature of the error. This is only present for error webhooks.
     *
     * @return The error description, or {@code null} if not applicable.
     */
    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    /**
     * Constructs an instance, populating this class's fields from the JSON string.
     *
     * @param json The JSON payload as a string.
     *
     * @return A new instance of this class.
     */
    public static EventWebhook fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
