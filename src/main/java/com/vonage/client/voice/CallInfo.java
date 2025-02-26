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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.util.Date;

/**
 * Holds the information related to a call. It is obtained using {@link VoiceClient#listCalls()}.
 */
public class CallInfo extends JsonableBaseObject {
    private Endpoint from, to;
    private String conversationUuid, uuid, network, price, rate;
    private CallDirection direction;
    private Integer duration;
    private Date startTime, endTime;
    private CallStatus status;

    CallInfo() {}

    /**
     * Call destination.
     *
     * @return The call recipient endpoint.
     */
    @JsonProperty("to")
    public Endpoint getTo() {
        return to;
    }

    /**
     * Call source.
     *
     * @return The caller endpoint.
     */
    @JsonProperty("from")
    public Endpoint getFrom() {
        return from;
    }

    /**
     * ID of the call.
     *
     * @return The call ID as a string.
     */
    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    /**
     * ID of the conversation.
     *
     * @return The conversation ID as a string.
     */
    @JsonProperty("conversation_uuid")
    public String getConversationUuid() {
        return conversationUuid;
    }

    /**
     * Time elapsed for the call to take place in seconds.
     * This is only present if {@linkplain #getStatus()} is {@linkplain CallStatus#COMPLETED}.
     *
     * @return The call duration in seconds as an integer, or {@code null} if unknown.
     */
    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    /**
     * Start time of the call.
     * This is only present if {@linkplain #getStatus()} is {@linkplain CallStatus#COMPLETED}.
     *
     * @return The start time of the call as a {@link Date} object, or {@code null} if unknown.
     */
    @JsonProperty("start_time")
    public Date getStartTime() {
        return this.startTime;
    }

    /**
     * End time of the call.
     * This is only present if {@linkplain #getStatus()} is {@linkplain CallStatus#COMPLETED}.
     *
     * @return The end time of the call as a {@link Date} object, or {@code null} if unknown.
     */
    @JsonProperty("end_time")
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Total price charged for this call.
     * This is only present if {@linkplain #getStatus()} is {@linkplain CallStatus#COMPLETED}.
     *
     * @return The call total cost as a string, or {@code null} if unknown.
     */
    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    /**
     * Price per minute for this call.
     * This is only present if {@linkplain #getStatus()} is {@linkplain CallStatus#COMPLETED}.
     *
     * @return The per-minute call rate as a string, or {@code null} if unknown.
     */
    @JsonProperty("rate")
    public String getRate() {
        return rate;
    }

    /**
     * Status of the call.
     *
     * @return The call status as an enum.
     */
    @JsonProperty("status")
    public CallStatus getStatus() {
        return status;
    }

    /**
     * Direction of the call: either inbound or outbound.
     *
     * @return The call direction as an enum.
     */
    @JsonProperty("direction")
    public CallDirection getDirection() {
        return direction;
    }

    /**
     * The Mobile Country Code Mobile Network Code (MCCMNC) for the carrier network used to make this call.
     * This is only present if {@linkplain #getStatus()} is {@linkplain CallStatus#COMPLETED}.
     *
     * @return The MCCMNC as a string, or {@code null} if unknown.
     */
    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    @Override
    public String toString() {
        return "<CallInfo " +
                "ID: " + this.getUuid() + ", " +
                "From: " + this.getFrom().toLog() + ", " +
                "To: " + this.getTo().toLog() + ", " +
                "Status: " + this.getStatus() +
                ">";
    }

    /**
     * Creates an instance of this class from a JSON payload.
     *
     * @param json The JSON string to parse.
     *
     * @return An instance of this class with the fields populated, if present.
     *
     * @deprecated Use {@link Jsonable#fromJson(String, Class)}. This will be removed in a future release.
     */
    @Deprecated
    public static CallInfo fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}