/*
 *   Copyright 2024 Vonage
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.VonageResponseParseException;

public class VerifyResponse extends JsonableBaseObject {
    private VerifyStatus status;
    private String requestId, errorText, network;

    private VerifyResponse() {
    }

    public VerifyResponse(VerifyStatus status) {
        this.status = status;
    }

    /**
     * @return The unique ID of the Verify request. You need this {@code request_id} for the Verify check.
     * Note that this may not be present when {@link #getNetwork()} returns a non-null / non-empty value.
     */
    @JsonProperty("request_id")
    public String getRequestId() {
        return requestId;
    }

    /**
     * @return The outcome of the request; {@code 0} (i.e. {@link VerifyStatus#OK}) indicates success.
     */
    @JsonProperty("status")
    public VerifyStatus getStatus() {
        return status;
    }

    /**
     * @return If status is non-zero, this explains the error encountered.
     */
    @JsonProperty("error_text")
    public String getErrorText() {
        return errorText;
    }

    /**
     * @return The network ID, if {@link #getStatus()} returns {@link VerifyStatus#NUMBER_BARRED}.
     *
     * @since 7.1.0
     */
    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    public static VerifyResponse fromJson(String json) {
        VerifyResponse response = new VerifyResponse();
        response.updateFromJson(json);
        if (response.status == null) {
            throw new VonageResponseParseException("Response status is missing.");
        }
        return response;
    }
}
