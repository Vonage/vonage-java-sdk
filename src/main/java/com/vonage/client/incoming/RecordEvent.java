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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import java.util.Date;

/**
 * @deprecated Use {@link com.vonage.client.voice.EventWebhook}.
 */
@Deprecated
public class RecordEvent implements Jsonable {
    private Date startTime, endTime, timestamp;
    private String url, uuid, conversationUuid;
    private int size;

    @JsonProperty("start_time")
    public Date getStartTime() {
        return startTime;
    }

    @JsonProperty("recording_url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("size")
    public int getSize() {
        return size;
    }

    @JsonProperty("recording_uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("end_time")
    public Date getEndTime() {
        return endTime;
    }

    @JsonProperty("conversation_uuid")
    public String getConversationUuid() {
        return conversationUuid;
    }

    @JsonProperty("timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    public static RecordEvent fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
