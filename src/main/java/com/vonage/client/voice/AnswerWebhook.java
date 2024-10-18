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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

/**
 * Maps the fields for data sent to the {@code answer_url} configured in your voice application.
 * See <a href=https://developer.vonage.com/en/voice/voice-api/webhook-reference#answer-webhook>
 * the webhook reference</a> for details.
 *
 * @since 8.2.0
 */
public class AnswerWebhook extends JsonableBaseObject {
    @JsonProperty("endpoint_type") private EndpointType endpointType;
    @JsonProperty("from") private String from;
    @JsonProperty("from_user") private String fromUser;
    @JsonProperty("to") private String to;
    @JsonProperty("conversation_uuid") private String conversationUuid;
    @JsonProperty("uuid") private UUID uuid;
    @JsonProperty("region_url") private URI regionUrl;
    @JsonProperty("custom_data") private Map<String, ?> customData;

    protected AnswerWebhook() {}

    /**
     * The type of endpoint that answered the call.
     *
     * @return The endpoint type as an enum, or {@code null} if unknown.
     * @since 8.12.0
     */
    public EndpointType getEndpointType() {
        return endpointType;
    }

    /**
     * The user or number that answered the call. This is the virtual number linked to in your application.
     *
     * @return The answering user or number in E.164 format.
     */
    public String getTo() {
        return to;
    }

    /**
     * The number or user that made the call. This could be a landline or mobile number or another virtual number if
     * the call was made programmatically. It could also be a username if the call was made by a user.
     *
     * @return The calling user or number in E.164 format, or {@code null} if absent.
     */
    @JsonIgnore
    public String getFrom() {
        return from != null ? from : fromUser;
    }

    /**
     * Unique identifier for this conversation. Starts with {@code CON-} followed by a UUID.
     *
     * @return The conversation ID as a string.
     */
    public String getConversationUuid() {
        return conversationUuid;
    }

    /**
     * Unique identifier for this call.
     *
     * @return The call ID as a UUID.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Regional API endpoint which should be used to control the call with REST API; see the
     * <a href=https://developer.vonage.com/en/voice/voice-api/concepts/regions>full list of regions.</a>
     *
     * @return The configured region URL.
     */
    public URI getRegionUrl() {
        return regionUrl;
    }

    /**
     * A custom data map, optionally passed as parameter on the {@code callServer} method when a call is initiated
     * from an application using <a href=https://developer.vonage.com/en/client-sdk/in-app-voice/guides/make-call>
     * the Client SDK</a>.
     *
     * @return The custom data object as a Map, or {@code null} if absent / not applicable.
     */
    public Map<String, ?> getCustomData() {
        return customData;
    }

    /**
     * Constructs an instance, populating this class's fields from the JSON string.
     *
     * @param json The JSON payload as a string.
     *
     * @return A new instance of this class.
     */
    @JsonCreator
    public static AnswerWebhook fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
