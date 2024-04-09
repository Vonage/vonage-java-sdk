/*
 *   Copyright 2024 Vonage
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
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.util.List;

public class SmsSubmissionResponse extends JsonableBaseObject {
    private int messageCount;
    private List<SmsSubmissionResponseMessage> messages;

    /**
     * @deprecated This will be made private in the next major release.
     */
    @Deprecated
    public SmsSubmissionResponse() {
    }

    /**
     * Message count.
     *
     * @return The number of messages in the request.
     */
    @JsonProperty("message-count")
    public int getMessageCount() {
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

    public static SmsSubmissionResponse fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
