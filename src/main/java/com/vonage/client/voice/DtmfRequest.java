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
package com.vonage.client.voice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

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
