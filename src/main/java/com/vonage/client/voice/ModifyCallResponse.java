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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;

/**
 * Response if a {@link Call} was successfully modified.
 * <p>
 * This would be returned by {@link VoiceClient#modifyCall(String, ModifyCallAction)}
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyCallResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public static ModifyCallResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, ModifyCallResponse.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce ModifyCallResponse from json.", jpe);
        }
    }

}
