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
package com.vonage.client.conversion;

import com.vonage.client.AbstractQueryParamsRequest;
import java.util.Date;
import java.util.Map;

class ConversionRequest extends AbstractQueryParamsRequest {
    private final Type type;
    private final String messageId;
    private final boolean delivered;
    private final Date timestamp;

    public ConversionRequest(Type type, String messageId, Boolean delivered, Date timestamp) {
        this.type = type;
        this.messageId = messageId;
        this.delivered = delivered;
        this.timestamp = timestamp;
    }

    public Type getType() {
        return type;
    }

    public String getMessageId() {
        return messageId;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        conditionalAdd("message-id", messageId);
        conditionalAdd("delivered", delivered);
        conditionalAdd("timestamp", timestamp);
        return params;
    }
}
