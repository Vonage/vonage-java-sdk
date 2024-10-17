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
 * Represents an app-to-app call type.
 *
 * @since 7.3.0
 */
public class AppEndpoint extends JsonableBaseObject implements Endpoint {
    private String user;

    protected AppEndpoint() {
    }

    public AppEndpoint(String user) {
        this.user = user;
    }

    /**
     * The username of the user to connect to. This username must have been added as a user.
     *
     * @return The app user, or {@code null} if unset.
     */
    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    @Override
    public String toLog() {
        return user;
    }

    @Override
    public String getType() {
        return EndpointType.APP.toString();
    }
}