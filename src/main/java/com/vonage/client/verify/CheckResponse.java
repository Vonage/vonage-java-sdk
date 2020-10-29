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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckResponse {
    private String requestId;
    private String eventId;
    private VerifyStatus status;
    private BigDecimal price;
    private String currency;
    private String errorText;

    @JsonCreator
    public CheckResponse(@JsonProperty(value = "status", required = true) VerifyStatus status) {
        this.status = status;
    }

    @JsonProperty("request_id")
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("event_id")
    public String getEventId() {
        return eventId;
    }

    public VerifyStatus getStatus() {
        return status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    @JsonProperty("error_text")
    public String getErrorText() {
        return errorText;
    }

    public static CheckResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, CheckResponse.class);
        } catch (JsonMappingException jme) {
            throw new VonageResponseParseException("Failed to produce CheckResponse from json.", jme);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce CheckResponse from json.", jpe);
        }
    }
}
