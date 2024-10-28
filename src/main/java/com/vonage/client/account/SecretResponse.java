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
package com.vonage.client.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.time.Instant;

/**
 * Represents metadata about an API account secret.
 */
public class SecretResponse extends JsonableBaseObject {
    private String id;
    private Instant created;

    /**
     * ID of the secret.
     *
     * @return The secret ID as a string.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Time the secret was created.
     *
     * @return The creation time as an Instant, or {@code null} if unknown.
     */
    @JsonProperty("created_at")
    public Instant getCreated() {
        return created;
    }

    @Deprecated
    public static SecretResponse fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
