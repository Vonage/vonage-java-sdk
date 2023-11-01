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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallEvent implements Jsonable {
    private String uuid, conversationUuid;
    private CallStatus status;
    private CallDirection direction;

    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("conversation_uuid")
    public String getConversationUuid() {
        return conversationUuid;
    }

    @JsonProperty("status")
    public CallStatus getStatus() {
        return status;
    }

    @JsonProperty("direction")
    public CallDirection getDirection() {
        return direction;
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
    public void setStatus(CallStatus status) {
        this.status = status;
    }

    @Deprecated
    public void setDirection(CallDirection direction) {
        this.direction = direction;
    }

    /**
     * Creates an instance of this class from a JSON payload.
     *
     * @param json The JSON string to parse.
     *
     * @return An instance of this class with the fields populated, if present.
     */
    public static CallEvent fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}