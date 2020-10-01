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
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotifyEvent {
    @JsonProperty(value = "conversation_uuid")
    private String conversationUuid;
    private Date timestamp;
    private Map<String, Object> payload;

    public String getConversationUuid() {
        return conversationUuid;
    }

    public void setConversationUuid(String conversationUuid) {
        this.conversationUuid = conversationUuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "NotifyEvent{" +
                "conversationUuid='" + conversationUuid + '\'' +
                ", timestamp=" + timestamp +
                ", payload=" + payload +
                '}';
    }

    public static NotifyEvent fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, NotifyEvent.class);
        } catch (IOException e) {
            throw new VonageUnexpectedException("Failed to convert NotifyEvent from json.", e);
        }
    }
}
