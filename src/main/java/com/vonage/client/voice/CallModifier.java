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
package com.vonage.client.voice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.voice.ncco.Ncco;


public class CallModifier {
    private final String uuid;
    private final ModifyCallPayload modifyCallPayload;

    public CallModifier(String uuid, ModifyCallPayload modifyCallPayload) {
        this.uuid = uuid;
        this.modifyCallPayload = modifyCallPayload;
    }

    public CallModifier(String uuid, ModifyCallAction action) {
        this.uuid = uuid;
        this.modifyCallPayload = new ModifyCallPayload(action);
    }

    public static CallModifier transferCall(String uuid, String nccoUrl) {
        return new CallModifier(uuid, new TransferCallPayload(nccoUrl));
    }

    public static CallModifier transferCall(String uuid, Ncco ncco) {
        return new CallModifier(uuid, new TransferCallPayload(ncco));
    }

    public String getUuid() {
        return uuid;
    }

    public ModifyCallAction getAction() {
        return modifyCallPayload.getAction();
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this.modifyCallPayload);
        } catch (JsonProcessingException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from CallModifier object.", jpe);
        }
    }
}
