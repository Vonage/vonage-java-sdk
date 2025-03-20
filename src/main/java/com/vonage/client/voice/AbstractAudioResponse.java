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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Base class for {@linkplain TalkResponse} and {@linkplain StreamResponse}.
 *
 * @since 9.0.0
 */
class AbstractAudioResponse extends JsonableBaseObject {
    private String uuid, message;

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    AbstractAudioResponse() {}

    /**
     * UUID of the call to which the message was sent.
     *
     * @return The call ID as a string.
     */
    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    /**
     * A message describing the result of the operation.
     *
     * @return The response message.
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}
