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
package com.vonage.client.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.List;

/**
 * Represents the response from the SMS API when a request is made to submit a message.
 * Since a message may be split into multiple parts, the response metadata for each individual SMS
 * can be retrieved from the {@link #getMessages()} method, which returns a list of
 * {@link SmsSubmissionResponseMessage} for each corresponding message that was submitted.
 */
public class SmsSubmissionResponse extends JsonableBaseObject {
    private Integer messageCount;
    private List<SmsSubmissionResponseMessage> messages;

    /**
     * Default constructor used by Jackson.
     */
    SmsSubmissionResponse() {}

    /**
     * Message count.
     *
     * @return The number of messages in the request.
     */
    @JsonProperty("message-count")
    public Integer getMessageCount() {
        return messageCount;
    }

    /**
     * Responses for each of the messages.
     *
     * @return The list of message responses in order of submission.
     */
    @JsonProperty("messages")
    public List<SmsSubmissionResponseMessage> getMessages() {
        return messages;
    }
}
