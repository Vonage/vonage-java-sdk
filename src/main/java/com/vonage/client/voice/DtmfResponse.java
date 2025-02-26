/*
 *   Copyright 2025 Vonage
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;

/**
 * Response if DTMF tones were successfully sent to an active {@link Call}, as
 * returned by {@link VoiceClient#sendDtmf(String, String)}.
 */
public class DtmfResponse extends JsonableBaseObject {
    private String uuid, message;

    @Deprecated
    public DtmfResponse() {}

    /**
     * Unique identifier for the call leg DTMF was sent to.
     *
     * @return The call ID as a string.
     */
    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    /**
     * Description of the action taken.
     *
     * @return The action description.
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * Creates an instance of this class from a JSON payload.
     *
     * @param json The JSON string to parse.
     *
     * @return An instance of this class with the fields populated, if present.
     */
    public static DtmfResponse fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
