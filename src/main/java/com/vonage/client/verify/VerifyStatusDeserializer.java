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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class VerifyStatusDeserializer extends JsonDeserializer<VerifyStatus> {
    @Override
    public VerifyStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Perform the conversion using parseInt so that potential number format exceptions can be converted into a default
        // VerifyStatus.INTERNAL_ERROR
        // Jackson's getValueAsInt defaults results to 0 and throws no exceptions.
        try {
            return VerifyStatus.fromInt(Integer.parseInt(p.getText()));
        } catch (NumberFormatException nfe) {
            return VerifyStatus.INTERNAL_ERROR;
        }
    }
}
