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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.UUID;

/**
 * Represents the response from {@link Verify2Client#checkVerificationCode(UUID, String)}.
 *
 * @since 8.9.2
 */
public final class VerifyCodeResponse extends JsonableBaseObject {
    private UUID requestId;
    private VerificationStatus status;

    private VerifyCodeResponse() {}

    /**
     * ID of the verify request.
     *
     * @return The verify request ID.
     */
    @JsonProperty("request_id")
    public UUID getRequestId() {
        return requestId;
    }

    /**
     * Status of the verification workflow.
     *
     * @return The status as an enum.
     */
    @JsonProperty("status")
    public VerificationStatus getStatus() {
        return status;
    }
}
