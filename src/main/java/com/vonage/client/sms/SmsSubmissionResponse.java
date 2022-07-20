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
package com.vonage.client.sms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageResponseParseException;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsSubmissionResponse {
    private int messageCount;
    private List<SmsSubmissionResponseMessage> messages;

    public static SmsSubmissionResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, SmsSubmissionResponse.class);
        } catch (JsonProcessingException jme) {
            throw new VonageResponseParseException("Failed to produce SmsSubmissionResponse from json.", jme);
        }
    }

    /**
     * @return The number of messages in the request.
     */
    @JsonProperty("message-count")
    public int getMessageCount() {
        return messageCount;
    }

    /**
     * @return Responses for each of the messages.
     */
    @JsonProperty("messages")
    public List<SmsSubmissionResponseMessage> getMessages() {
        return messages;
    }
}
