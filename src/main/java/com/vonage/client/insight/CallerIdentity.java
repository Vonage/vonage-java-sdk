/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.insight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CallerIdentity {
    private CallerType type;
    private String name;
    private String firstName;
    private String lastName;

    /**
     * @return The caller type, as an enum.
     */
    @JsonProperty("caller_type")
    public CallerType getType() {
        return type;
    }

    /**
     * @return Full name of the person or business who owns the phone number, or "unknown" if this
     * information is not available. This parameter is only present if cnam had a value of
     * <code>true</code> in the request.
     */
    @JsonProperty("caller_name")
    public String getName() {
        return name;
    }

    /**
     * @return First name of the person who owns the phone number if the owner is an individual.
     * This parameter is only present if cnam had a value of <code>true</code> in the request.
     */
    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return Last name of the person who owns the phone number if the owner is an individual.
     * This parameter is only present if cnam had a value of <code>true</code> in the request.
     */
    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }
}
