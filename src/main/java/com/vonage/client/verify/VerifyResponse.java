/*
 *   Copyright 2020 Vonage
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
package com.vonage.client.verify;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;

public class VerifyResponse {
    private String requestId;
    private VerifyStatus status;
    private String errorText;

    @JsonCreator
    public VerifyResponse(@JsonProperty(value = "status", required = true) VerifyStatus status) {
        this.status = status;
    }

    @JsonProperty("request_id")
    public String getRequestId() {
        return requestId;
    }

    public VerifyStatus getStatus() {
        return status;
    }

    @JsonProperty("error_text")
    public String getErrorText() {
        return errorText;
    }

    public static VerifyResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, VerifyResponse.class);
        } catch (JsonMappingException jme) {
            throw new VonageResponseParseException("Failed to produce VerifyResponse from json.", jme);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce VerifyResponse from json.", jpe);
        }
    }
}
