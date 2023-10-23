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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import java.util.Date;

/**
 * CallInfo holds the information related to a call. It is obtained using {@link VoiceClient#listCalls()}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = { "_links" }, ignoreUnknown = true)
public class CallInfo implements Jsonable {
    private Endpoint from, to;
    private String conversationUuid, uuid, network, price, rate;
    private CallDirection direction;
    private Integer duration;
    private Date startTime, endTime;
    private CallStatus status;

    @Deprecated
    public CallInfo() {}

    @Deprecated
    public CallInfo(String to, String from) {
        this(new PhoneEndpoint(to), new PhoneEndpoint(from));
    }

    @Deprecated
    public CallInfo(Endpoint to, Endpoint from) {
        this.to = to;
        this.from = from;
    }

    @JsonProperty("to")
    public Endpoint getTo() {
        return to;
    }

    @JsonProperty("from")
    public Endpoint getFrom() {
        return from;
    }

    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("conversation_uuid")
    public String getConversationUuid() {
        return conversationUuid;
    }

    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @JsonProperty("end_time")
    public Date getEndTime() {
        return endTime;
    }

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("rate")
    public String getRate() {
        return rate;
    }

    @JsonProperty("start_time")
    public Date getStartTime() {
        return this.startTime;
    }

    @JsonProperty("status")
    public CallStatus getStatus() {
        return status;
    }

    @JsonProperty("direction")
    public CallDirection getDirection() {
        return direction;
    }

    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    @Deprecated
    public void setTo(Endpoint to) {
        this.to = to;
    }

    @Deprecated
    public void setFrom(Endpoint from) {
        this.from = from;
    }

    @Deprecated
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Deprecated
    public void setConversationUuid(String conversationUuid) {
        this.conversationUuid = conversationUuid;
    }

    @Deprecated
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Deprecated
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Deprecated
    public void setPrice(String price) {
        this.price = price;
    }

    @Deprecated
    public void setRate(String rate) {
        this.rate = rate;
    }

    @Deprecated
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Deprecated
    public void setNetwork(String network) {
        this.network = network;
    }

    @Deprecated
    public void setDirection(CallDirection direction) {
        this.direction = direction;
    }

    @Deprecated
    public void setStatus(CallStatus status) {
        this.status = status;
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
     */
    public static CallInfo fromJson(String json) {
        return Jsonable.fromJson(json, CallInfo.class);
    }
}