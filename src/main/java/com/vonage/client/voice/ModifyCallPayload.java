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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.Objects;

/**
 * Wrapper for call modification requests.
 */
class ModifyCallPayload extends JsonableBaseObject {
    @JsonIgnore final String uuid;
    private final ModifyCallAction action;

    /**
     * Create a new ModifyCallPayload.
     *
     * @param action The action to perform as an enum.
     * @param uuid The call ID to modify.
     */
    ModifyCallPayload(ModifyCallAction action, String uuid) {
        this.action = Objects.requireNonNull(action, "Action is required.");
        this.uuid = Objects.requireNonNull(uuid, "callId is required.");
    }

    /**
     * Call modification action.
     *
     * @return The action as an enum.
     */
    @JsonProperty("action")
    public ModifyCallAction getAction() {
        return action;
    }
}
