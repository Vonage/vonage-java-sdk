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
import com.vonage.client.voice.CallEndpoint;
import com.vonage.client.voice.EndpointType;

/**
 * An endpoint for a {@link ConnectAction} to connect to. For outbound call endpoints, see {@link CallEndpoint}.
 *
 * @since 9.0.0 Renamed to ConnectEndpoint from Endpoint.
 */
public interface ConnectEndpoint {

    /**
     * Gets the endpoint type name.
     *
     * @return The endpoint type as a string.
     */
    @JsonProperty("type")
    EndpointType getType();
}
