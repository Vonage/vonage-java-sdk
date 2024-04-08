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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.Jsonable;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @deprecated Moved to {@link com.vonage.client.sms.MessageEvent}.
 */
@Deprecated
public class MessageEvent implements Jsonable {
    private String msisdn, to, messageId, text, keyword, timestamp, nonce, data, udh;
    private MessageType type;
    private Date messageTimestamp;
    private Boolean concat;
    private int concatRef, concatTotal, concatPart;

    @JsonProperty("msisdn")
    public String getMsisdn() {
        return msisdn;
    }

    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    @JsonProperty("messageId")
    public String getMessageId() {
        return messageId;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("type")
    public MessageType getType() {
        return type;
    }

    @JsonProperty("keyword")
    public String getKeyword() {
        return keyword;
    }

    @JsonProperty("message-timestamp")
    public Date getMessageTimestamp() {
        return messageTimestamp;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("nonce")
    public String getNonce() {
        return nonce;
    }

    @JsonProperty("concat")
    public Boolean getConcat() {
        return concat;
    }

    @JsonProperty("concat-ref")
    public int getConcatRef() {
        return concatRef;
    }

    @JsonProperty("concat-total")
    public int getConcatTotal() {
        return concatTotal;
    }

    @JsonProperty("concat-part")
    public int getConcatPart() {
        return concatPart;
    }

    @JsonProperty("data")
    public String getData() {
        return data;
    }

    @JsonProperty("udh")
    public String getUdh() {
        return udh;
    }

    @Override
    public void updateFromJson(String json) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(dateFormat);
            mapper.readerForUpdating(this).readValue(json, MessageEvent.class);
        }
        catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce MessageEvent from json.", jpe);
        }
    }

    public static MessageEvent fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
