/*
 * Copyright (c) 2011-2017 Vonage Inc
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
package com.nexmo.client.voice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.VonageUnexpectedException;

/**
 * The request object to send DTMF tones.
 * <p>
 * Contains the {@code uuid} of the {@link Call} and the {@link DtmfPayload} to be sent in the request.
 */

public class DtmfRequest {
    private String uuid;
    private DtmfPayload payload;

    public DtmfRequest(String uuid, String digits) {
        this.uuid = uuid;
        this.payload = new DtmfPayload(digits);
    }

    public String getUuid() {
        return uuid;
    }

    public String getDigits() {
        return payload.getDigits();
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setDigits(String digits) {
        this.payload.setDigits(digits);
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this.payload);
        } catch (JsonProcessingException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from DtmfRequest object.", jpe);
        }
    }
}
