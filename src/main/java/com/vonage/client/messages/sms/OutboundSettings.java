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
    final String contentId, entityId;

    private OutboundSettings(EncodingType encodingType, String contentId, String entityId) {
        this.encodingType = encodingType;
        this.contentId = contentId;
        this.entityId = entityId;
    }

    static OutboundSettings construct(EncodingType encodingType, String contentId, String entityId) {
        if (encodingType == null && contentId == null && entityId == null) {
            return null;
        }
        if (contentId != null && contentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Content ID cannot be blank.");
        }
        if (entityId != null && entityId.trim().isEmpty()) {
            throw new IllegalArgumentException("Entity ID cannot be blank.");
        }
        return new OutboundSettings(encodingType, contentId, entityId);
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
}
