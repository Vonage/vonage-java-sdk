/*
 * Copyright (c) 2011-2018 Nexmo Inc
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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.NexmoUnexpectedException;

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
        return this.requestId;
    }

    public VerifyStatus getStatus() {
        return this.status;
    }

    @JsonProperty("error_text")
    public String getErrorText() {
        return this.errorText;
    }

    public static VerifyResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, VerifyResponse.class);
        } catch (JsonMappingException jme) {
            throw new NexmoResponseParseException("Failed to produce VerifyResponse from json.", jme);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce VerifyResponse from json.", jpe);
        }
    }
}
