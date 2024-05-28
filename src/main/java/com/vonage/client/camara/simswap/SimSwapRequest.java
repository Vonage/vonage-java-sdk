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
package com.vonage.client.camara.simswap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.E164;

class SimSwapRequest extends JsonableBaseObject {
    private String phoneNumber;
    private Integer maxAge;

    SimSwapRequest() {
        super();
    }

    SimSwapRequest(String phoneNumber) {
        this(phoneNumber, null);
    }

    SimSwapRequest(String phoneNumber, Integer maxAge) {
        this.phoneNumber = new E164(phoneNumber).toString();
        if ((this.maxAge = maxAge) != null && (maxAge < 1 || maxAge > 2400)) {
            throw new IllegalArgumentException("maxAge must be between 1 and 2400 hours.");
        }
    }

    /**
     * Gets the MSISDN for this request.
     *
     * @return The phone number in E.164 format, or {@code null} if unspecified.
     */
    @JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * The maximum period in hours for the check, if specified.
     *
     * @return The maxAge field as an Integer, or {@code null} if unspecified.
     */
    @JsonProperty("maxAge")
    public Integer getMaxAge() {
        return maxAge;
    }
}
