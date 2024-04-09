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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents metadata about a call.
 */
public class CallEvent extends JsonableBaseObject {
    private String uuid, conversationUuid;
    private CallStatus status;
    private CallDirection direction;

    /**
     * The unique identifier for this call leg. The UUID is created when your call request is accepted by Vonage.
     * You use the UUID in all requests for individual live calls.
     *
     * @return The call ID.
     */
    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    /**
     * The unique identifier for the conversation this call leg is part of.
     *
     * @return The conversation ID as a string.
     */
    @JsonProperty("conversation_uuid")
    public String getConversationUuid() {
        return conversationUuid;
    }

    /**
     * The status of the call.
     *
     * @return The call's status as an enum.
     */
    @JsonProperty("status")
    public CallStatus getStatus() {
        return status;
    }

    /**
     * Whether the call is inbound or outbound.
     *
     * @return The call direction as an enum.
     */
    @JsonProperty("direction")
    public CallDirection getDirection() {
        return direction;
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