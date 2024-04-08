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
package com.vonage.client.incoming;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import java.util.Date;

/**
 * @deprecated Use {@link com.vonage.client.voice.EventWebhook}.
 */
@Deprecated
public class CallEvent implements Jsonable {
    private String conversationUuid, callUuid, from, to, uuid, detail;
    private CallDirection direction;
    private CallStatus status;
    private Date timestamp;
    private CallStatusDetail detailEnum;

    @JsonProperty("conversation_uuid")
    public String getConversationUuid() {
        return conversationUuid;
    }

    @JsonProperty("call_uuid")
    public String getCallUuid() {
        return callUuid;
    }

    @JsonProperty("direction")
    public CallDirection getDirection() {
        return direction;
    }

    @JsonProperty("from")
    public String getFrom() {
        return from;
    }

    @JsonProperty("status")
    public CallStatus getStatus() {
        return status;
    }

    @JsonProperty("timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("detail")
    public String getDetail() {
        return detail;
    }

    @JsonIgnore
    public CallStatusDetail getDetailEnum() {
        return CallStatusDetail.fromString(detail);
    }

    public static CallEvent fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
