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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsSubmissionResponse {
    @JsonProperty("message-count")
    private int messageCount;

    @JsonProperty("messages")
    private List<SmsSubmissionResponseMessage> messages;

    public static SmsSubmissionResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, SmsSubmissionResponse.class);
        } catch (JsonMappingException jme) {
            throw new VonageResponseParseException("Failed to produce SmsSubmissionResponse from json.", jme);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce SmsSubmissionResponse from json.", jpe);
        }
    }

    public int getMessageCount() {
        return messageCount;
    }

    public List<SmsSubmissionResponseMessage> getMessages() {
        return messages;
    }
}
