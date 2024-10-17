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
import com.vonage.client.JsonableBaseObject;

/**
 * Represents a VBC call type.
 *
 * @since 7.3.0
 */
public class VbcEndpoint extends JsonableBaseObject implements Endpoint {
    private String extension;

    protected VbcEndpoint() {
    }

    public VbcEndpoint(String extension) {
        this.extension = extension;
    }

    /**
     * The extension to call.
     *
     * @return The VBC extension, or {@code null} if unset.
     */
    @JsonProperty("extension")
    public String getExtension() {
        return extension;
    }

    @Override
    public String toLog() {
        return extension;
    }

    @JsonProperty("type")
    @Override
    public String getType() {
        return EndpointType.VBC.toString();
    }
}