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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Represents a receiver endpoint in a {@link Call}.
 * For NCCO endpoints, see {@link com.vonage.client.voice.ncco.Endpoint}.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PhoneEndpoint.class, name = "phone"),
        @JsonSubTypes.Type(value = SipEndpoint.class, name = "sip"),
        @JsonSubTypes.Type(value = WebSocketEndpoint.class, name = "websocket"),
        @JsonSubTypes.Type(value = VbcEndpoint.class, name = "vbc"),
        @JsonSubTypes.Type(value = AppEndpoint.class, name = "app")
})
public interface Endpoint {

    /**
     * Endpoint type name.
     *
     * @return The type of endpoint as a string.
     */
    @JsonProperty("type")
    String getType();

    /**
     * Description of the endpoint.
     *
     * @return A string representation of the object.
     */
    @JsonIgnore
    String toLog();
}
