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
package com.vonage.client.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents the main attributes of a User.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseUser extends JsonableBaseObject {
    String id, name;

    BaseUser() {
    }

    void setId(String id) {
        this.id = id;
    }

    /**
     * Unique user ID.
     *
     * @return The user ID as a string, or {@code null} if unknown.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Unique name of the user.
     *
     * @return The user's name.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }
}
