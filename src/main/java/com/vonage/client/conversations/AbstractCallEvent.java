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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.voice.CallDirection;

/**
 * Base class for answered and ringing events.
 *
 * @since 8.19.0
 */
abstract class AbstractCallEvent extends AbstractChannelEvent<AbstractCallEvent.Body> {
    AbstractCallEvent() {}

    /**
     * The main body container for SIP answered / ringing events.
     */
    static class Body extends AbstractChannelEvent.Body {
        @JsonProperty("direction") CallDirection direction;
    }

    /**
     * The call direction, either {@linkplain CallDirection#INBOUND} or {@linkplain CallDirection#OUTBOUND}.
     *
     * @return The call direction as an enum, or {@code null} if absent in the response.
     */
    @JsonIgnore
    public CallDirection getDirection() {
        return body != null ? body.direction : null;
    }
}
