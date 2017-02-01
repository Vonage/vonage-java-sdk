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


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;
import java.util.Date;

/**
 * CallInfo holds the information related to a call. It is obtained using {@link VoiceClient#listCalls}
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = { "_links" }, ignoreUnknown = true)
public class CallInfo {
    private CallEndpoint to;
    private CallEndpoint from;

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
        this(new CallEndpoint(to), new CallEndpoint(from));
    }

    public CallInfo(CallEndpoint to, CallEndpoint from) {
        this.to = to;
        this.from = from;
    }

    public CallEndpoint getTo() {
        return to;
    }

    public void setTo(CallEndpoint to) {
        this.to = to;
    }

    public CallEndpoint getFrom() {
        return from;
    }

    public void setFrom(CallEndpoint from) {
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
                .append("From: ").append(this.getFrom().getNumber()).append(", ")
                .append("To: ").append(this.getTo().getNumber()).append(", ")
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
            throw new NexmoUnexpectedException("Failed to produce json from CallInfo object.", jpe);
        }
    }
}