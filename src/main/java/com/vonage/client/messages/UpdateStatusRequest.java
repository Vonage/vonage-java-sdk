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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.ApiRegion;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.messages.whatsapp.ReplyingIndicator;
import java.util.Objects;
import java.util.UUID;

/**
 * Wrapper for the status update endpoint.
 *
 * @since 8.11.0
 */
class UpdateStatusRequest extends JsonableBaseObject {
    @JsonProperty("status") final String status;
    @JsonProperty("replying_indicator") final ReplyingIndicator replyingIndicator;
    @JsonIgnore final UUID messageId;
    @JsonIgnore final ApiRegion region;

    UpdateStatusRequest(String status, String messageId, ApiRegion region) {
        this(status, messageId, region, null);
    }

    UpdateStatusRequest(String status, String messageId, ApiRegion region, ReplyingIndicator replyingIndicator) {
        this.status = status;
        this.messageId = UUID.fromString(Objects.requireNonNull(messageId, "Message ID is required."));
        this.region = region != null ? region : ApiRegion.API_EU;
        this.replyingIndicator = replyingIndicator;
    }
}
