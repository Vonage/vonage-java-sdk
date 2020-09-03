/*
 * Copyright (c) 2020 Vonage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.verify;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.VonageResponseParseException;
import com.nexmo.client.VonageUnexpectedException;

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
        return this.requestId;
    }

    @JsonProperty("event_id")
    public String getEventId() {
        return this.eventId;
    }

    public VerifyStatus getStatus() {
        return this.status;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public String getCurrency() {
        return this.currency;
    }

    @JsonProperty("error_text")
    public String getErrorText() {
        return this.errorText;
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
