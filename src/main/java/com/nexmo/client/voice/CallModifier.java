/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
import com.nexmo.client.NexmoUnexpectedException;


public class CallModifier {
    private final String uuid;
    private final ModifyCallPayload modifyCallPayload;

    CallModifier(String uuid, ModifyCallPayload modifyCallPayload) {
        this.uuid = uuid;
        this.modifyCallPayload = modifyCallPayload;
    }

    public CallModifier(String uuid, String action) {
        this.uuid = uuid;
        this.modifyCallPayload = new ModifyCallPayload(ModifyCallPayload.Action.fromString(action));
    }

    public static CallModifier transferCall(String uuid, String nccoUrl) {
        return new CallModifier(uuid, new TransferCallPayload(nccoUrl));
    }

    public String getUuid() {
        return uuid;
    }

    public String getAction() {
        return modifyCallPayload.getAction().toString();
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this.modifyCallPayload);
        } catch (JsonProcessingException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from CallModifier object.", jpe);
        }
    }
}
