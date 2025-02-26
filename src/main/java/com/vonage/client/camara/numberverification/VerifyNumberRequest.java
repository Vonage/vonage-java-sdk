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
package com.vonage.client.camara.numberverification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.E164;
import java.net.URI;
import java.util.Objects;

class VerifyNumberRequest extends JsonableBaseObject {
    private final String phoneNumber;
    @JsonIgnore final URI redirectUrl;
    @JsonIgnore private String code;

    VerifyNumberRequest(String phoneNumber, URI redirectUrl) {
        this.phoneNumber = '+' + new E164(phoneNumber).toString();
        this.redirectUrl = Objects.requireNonNull(redirectUrl, "Redirect URL is required.");
    }

    @JsonIgnore
    VerifyNumberRequest withCode(String code) {
        this.code = Objects.requireNonNull(code, "Code is required.");
        return this;
    }

    @JsonIgnore
    String getCode() {
        return code;
    }

    /**
     * Gets the MSISDN for this request.
     *
     * @return The phone number in E.164 format.
     */
    @JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
