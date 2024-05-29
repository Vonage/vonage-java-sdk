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
package com.vonage.client.auth.camara;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

final class BackendAuthResponse extends JsonableBaseObject {
    private String authReqId;
    private Integer expiresIn, interval;

    private BackendAuthResponse() {}

    /**
     * ID of the authentication request.
     *
     * @return The authentication request ID.
     */
    @JsonProperty("auth_req_id")
    public String getAuthReqId() {
        return authReqId;
    }

    /**
     * Number of seconds until the authentication code expires.
     *
     * @return The auth code expiration time in seconds.
     */
    @JsonProperty("expires_in")
    public Integer getExpiresIn() {
        return expiresIn;
    }

    /**
     * Number of seconds until the next request should be made.
     *
     * @return The request interval, or {@code null} if unknown / not applicable.
     */
    @JsonProperty("interval")
    public Integer getInterval() {
        return interval;
    }
}
