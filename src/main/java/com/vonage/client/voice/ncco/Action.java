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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * An NCCO action.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "action"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RecordAction.class, name = "record"),
        @JsonSubTypes.Type(value = TalkAction.class, name = "talk"),
        @JsonSubTypes.Type(value = StreamAction.class, name = "stream"),
        @JsonSubTypes.Type(value = NotifyAction.class, name = "notify"),
        @JsonSubTypes.Type(value = InputAction.class, name = "input"),
        @JsonSubTypes.Type(value = ConnectAction.class, name = "connect"),
        @JsonSubTypes.Type(value = ConversationAction.class, name = "conversation")
})
public interface Action {

    @JsonProperty("action")
    String getAction();
}
