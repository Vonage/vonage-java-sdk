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
        modifyCallPayload = new ModifyCallPayload(action);
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
            return mapper.writeValueAsString(modifyCallPayload);
        } catch (JsonProcessingException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from CallModifier object.", jpe);
        }
    }
}
