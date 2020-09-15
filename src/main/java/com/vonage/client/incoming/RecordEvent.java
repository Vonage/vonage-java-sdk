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
package com.vonage.client.incoming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordEvent {
    private Date startTime;
    private String url;
    private int size;
    private String uuid;
    private Date endTime;
    private String conversationUuid;
    private Date timestamp;

    @JsonProperty("start_time")
    public Date getStartTime() {
        return startTime;
    }

    @JsonProperty("recording_url")
    public String getUrl() {
        return url;
    }

    public int getSize() {
        return size;
    }

    @JsonProperty("recording_uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("end_time")
    public Date getEndTime() {
        return endTime;
    }

    @JsonProperty("conversation_uuid")
    public String getConversationUuid() {
        return conversationUuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public static RecordEvent fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, RecordEvent.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce RecordEvent from json.", jpe);
        }
    }
}
