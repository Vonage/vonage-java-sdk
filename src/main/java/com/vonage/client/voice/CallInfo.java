/*
 *   Copyright 2020 Vonage
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;
import java.util.Date;

/**
 * CallInfo holds the information related to a call. It is obtained using {@link VoiceClient#listCalls}
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = { "_links" }, ignoreUnknown = true)
public class CallInfo {
    private Endpoint to;
    private Endpoint from;

    private String conversationUuid = null;
    private CallDirection direction = null;
    private Integer duration = null;
    private Date endTime = null;
    private String network = null;
    private String price = null;
    private String rate = null;
    private Date startTime = null;
    private CallStatus status = null;
    private String uuid = null;

    public CallInfo() {}

    public CallInfo(String to, String from) {
        this(new PhoneEndpoint(to), new PhoneEndpoint(from));
    }

    public CallInfo(Endpoint to, Endpoint from) {
        this.to = to;
        this.from = from;
    }

    public Endpoint getTo() {
        return to;
    }

    public void setTo(Endpoint to) {
        this.to = to;
    }

    public Endpoint getFrom() {
        return from;
    }

    public void setFrom(Endpoint from) {
        this.from = from;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonProperty("conversation_uuid")
    public String getConversationUuid() {
        return conversationUuid;
    }

    public void setConversationUuid(String conversationUuid) {
        this.conversationUuid = conversationUuid;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @JsonProperty("end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @JsonProperty("start_time")
    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }

    public CallDirection getDirection() {
        return direction;
    }

    public void setDirection(CallDirection direction) {
        this.direction = direction;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String toString() {
        return new StringBuilder()
                .append("<CallInfo ")
                .append("ID: ").append(this.getUuid()).append(", ")
                .append("From: ").append(this.getFrom().toLog()).append(", ")
                .append("To: ").append(this.getTo().toLog()).append(", ")
                .append("Status: ").append(this.getStatus())
                .append(">")
                .toString();
    }

    public static CallInfo fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.readValue(json, CallInfo.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from CallInfo object.", jpe);
        }
    }
}