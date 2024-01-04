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
package com.vonage.client.conversion;

import com.vonage.client.QueryParamsRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConversionRequest implements QueryParamsRequest {
    private final Type type;
    private final String messageId;
    private final boolean delivered;
    private final Date timestamp;

    public ConversionRequest(Type type, String messageId, boolean delivered, Date timestamp) {
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
        LinkedHashMap<String, String> params = new LinkedHashMap<>(4);
        params.put("message-id", messageId);
        params.put("delivered", String.valueOf(delivered));
        params.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp));
        return params;
    }

    public enum Type {
        SMS, VOICE
    }
}
