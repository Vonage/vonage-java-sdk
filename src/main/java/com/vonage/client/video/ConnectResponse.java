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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.UUID;

/**
 * Represents the response from {@link VideoClient#startCaptions(CaptionsRequest)}.
 *
 * @since 8.5.0
 */
public final class ConnectResponse extends JsonableBaseObject {
    private UUID id, connectionId;

    ConnectResponse() {}

    /**
     * A unique identifier for the Audio Connector WebSocket connection.
     *
     * @return The connection ID.
     */
    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    /**
     * The connection ID for the Audio Connector WebSocket connection in the Vonage Video session.
     *
     * @return The connection UUID.
     */
    @JsonProperty("connectionId")
    public UUID getConnectionId() {
        return connectionId;
    }
}
