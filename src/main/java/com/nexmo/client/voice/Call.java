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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

/**
 * Call encapsulates the information required to create a call using {@link VoiceClient#createCall(Call)}
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({ "_links"})
public class Call {
    private CallEndpoint to;
    private CallEndpoint from;
    private String answerUrl;

    private String answerMethod = "GET";
    private String eventUrl = null;
    private String eventMethod = null;
    private MachineDetection machineDetection = null;
    private Integer lengthTimer = null;
    private Integer ringingTimer = null;

    public Call() {}

    public Call(String to, String from, String answerUrl) {
        this(new CallEndpoint(to), new CallEndpoint(from), answerUrl);
    }

    public Call(CallEndpoint to, CallEndpoint from, String answerUrl) {
        this.to = to;
        this.from = from;
        this.answerUrl = answerUrl;
    }

    public CallEndpoint[] getTo() {
        return new CallEndpoint[]{to};
    }

    public void setTo(CallEndpoint[] to) {
        this.to = to[0];
    }

    public CallEndpoint getFrom() {
        return from;
    }

    public void setFrom(CallEndpoint from) {
        this.from = from;
    }

    @JsonProperty("answer_url")
    public String[] getAnswerUrl() {
        return new String[]{answerUrl};
    }

    public void setAnswerUrl(String answerUrl) {
        this.answerUrl = answerUrl;
    }

    @JsonProperty("answer_method")
    public String getAnswerMethod() {
        return answerMethod;
    }

    public void setAnswerMethod(String answerMethod) {
        this.answerMethod = answerMethod;
    }

    @JsonProperty("event_url")
    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    @JsonProperty("event_method")
    public String getEventMethod() {
        return eventMethod;
    }

    public void setEventMethod(String eventMethod) {
        this.eventMethod = eventMethod;
    }

    @JsonProperty("machine_detection")
    public MachineDetection getMachineDetection() {
        return machineDetection;
    }

    public void setMachineDetection(MachineDetection machineDetection) {
        this.machineDetection = machineDetection;
    }

    @JsonProperty("length_timer")
    public Integer getLengthTimer() {
        return lengthTimer;
    }

    public void setLengthTimer(Integer lengthTimer) {
        this.lengthTimer = lengthTimer;
    }

    @JsonProperty("ringing_timer")
    public Integer getRingingTimer() {
        return ringingTimer;
    }

    public void setRingingTimer(Integer ringingTimer) {
        this.ringingTimer = ringingTimer;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from Call object.", jpe);
        }
    }

    public static Call fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Call.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from Call object.", jpe);
        }
    }
}