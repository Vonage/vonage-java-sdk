/*
 *   Copyright 2020 Vonage
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
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageEvent {
    private String msisdn;
    private String to;
    private String messageId;
    private String text;
    private MessageType type;
    private String keyword;
    private Date messageTimestamp;
    private String timestamp;
    private String nonce;
    private Boolean concat;
    private int concatRef;
    private int concatTotal;
    private int concatPart;
    private String data;
    private String udh;

    public String getMsisdn() {
        return msisdn;
    }

    public String getTo() {
        return to;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getText() {
        return text;
    }

    public MessageType getType() {
        return type;
    }

    public String getKeyword() {
        return keyword;
    }

    @JsonProperty("message-timestamp")
    public Date getMessageTimestamp() {
        return messageTimestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNonce() {
        return nonce;
    }

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

    public String getData() {
        return data;
    }

    public String getUdh() {
        return udh;
    }

    public static MessageEvent fromJson(String json) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(dateFormat);
            return mapper.readValue(json, MessageEvent.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce MessageEvent from json.", jpe);
        }
    }
}
