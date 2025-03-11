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
import com.vonage.client.JsonableBaseObject;
import java.util.Objects;

/**
 * Base class for request payloads that use a UUID in their path, but not the body.
 *
 * @since 8.19.0
 */
class UuidRequestWrapper extends JsonableBaseObject {
    @JsonIgnore private String uuid;

    /**
     * Sets the UUID for the request.
     *
     * @param uuid The resource UUID as a string.
     */
    @JsonIgnore
    protected void setUuid(String uuid) {
        this.uuid = Objects.requireNonNull(uuid, "UUID is required.");
    }

    /**
     * Retrieves the UUID for the request.
     *
     * @return The resource UUID as a string, or {@code null} if not set.
     */
    @JsonIgnore
    protected String getUuid() {
        return uuid;
    }
}
