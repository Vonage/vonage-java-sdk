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
package com.vonage.client.messages.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents optional settings for the SMS message in {@link SmsTextRequest}.
 *
 * @since 8.1.0
 */
public final class OutboundSettings extends JsonableBaseObject {
    final EncodingType encodingType;
    final String contentId, entityId, poolId;
    final Boolean trustedRecipient;

    private OutboundSettings(EncodingType encodingType, String contentId, String entityId, Boolean trustedRecipient) {
        this(encodingType, contentId, entityId, null, trustedRecipient);
    }

    private OutboundSettings(EncodingType encodingType, String contentId, String entityId, String poolId, Boolean trustedRecipient) {
        this.encodingType = encodingType;
        this.contentId = contentId;
        this.entityId = entityId;
        this.poolId = poolId;
        this.trustedRecipient = trustedRecipient;
    }

    static OutboundSettings construct(EncodingType encodingType, String contentId, String entityId) {
        return construct(encodingType, contentId, entityId, null, null);
    }

    static OutboundSettings construct(EncodingType encodingType, String contentId, String entityId, Boolean trustedRecipient) {
        return construct(encodingType, contentId, entityId, null, trustedRecipient);
    }

    static OutboundSettings construct(EncodingType encodingType, String contentId, String entityId, String poolId, Boolean trustedRecipient) {
        if (encodingType == null && contentId == null && entityId == null && poolId == null && trustedRecipient == null) {
            return null;
        }
        if (contentId != null && contentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Content ID cannot be blank.");
        }
        if (entityId != null && entityId.trim().isEmpty()) {
            throw new IllegalArgumentException("Entity ID cannot be blank.");
        }
        if (poolId != null && poolId.trim().isEmpty()) {
            throw new IllegalArgumentException("Pool ID cannot be blank.");
        }
        return new OutboundSettings(encodingType, contentId, entityId, poolId, trustedRecipient);
    }

    @JsonProperty("encoding_type")
    public EncodingType getEncodingType() {
        return encodingType;
    }

    @JsonProperty("content_id")
    public String getContentId() {
        return contentId;
    }

    @JsonProperty("entity_id")
    public String getEntityId() {
        return entityId;
    }

    @JsonProperty("pool_id")
    public String getPoolId() {
        return poolId;
    }

    @JsonProperty("trusted_recipient")
    public Boolean getTrustedRecipient() {
        return trustedRecipient;
    }
}
