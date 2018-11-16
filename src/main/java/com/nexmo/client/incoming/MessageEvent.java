/*
 * Copyright (c) 2011-2018 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.incoming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

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
            throw new NexmoUnexpectedException("Failed to produce MessageEvent from json.", jpe);
        }
    }
}
