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
package com.vonage.client.sms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.time.Instant;

/**
 * Represents an <a href=https://developer.vonage.com/en/messaging/sms/guides/inbound-sms>Inbound SMS webhook</a>.
 *
 * @since 8.3.0
 */
public final class MessageEvent extends JsonableBaseObject {
    private String msisdn, to, messageId, text, keyword, nonce, data, udh;
    private MessageType type;
    private Instant messageTimestamp;
    private Long timestamp;
    private Boolean concat;
    private Integer concatRef, concatTotal, concatPart;

    private MessageEvent() {}

    /**
     * The phone number that this inbound message was sent from.
     *
     * @return The sender number in E.164 format.
     */
    @JsonProperty("msisdn")
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * Phone number that the message was sent to. This is your virtual number.
     *
     * @return The recipient number in E.164 format.
     */
    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    /**
     * Vonage's unique identifier for this message.
     *
     * @return The message ID.
     */
    @JsonProperty("messageId")
    public String getMessageId() {
        return messageId;
    }

    /**
     * Type of message.
     *
     * @return The message type as an enum.
     */
    @JsonProperty("type")
    public MessageType getType() {
        return type;
    }

    /**
     * Message body for this inbound message.
     *
     * @return The text message, or {@code null} if {@linkplain #getType()} is {@linkplain MessageType#BINARY}.
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * The first word in the message body. This is typically used with short codes.
     *
     * @return The message keyword, or {@code null} if {@linkplain #getType()} is {@linkplain MessageType#BINARY}.
     */
    @JsonProperty("keyword")
    public String getKeyword() {
        return keyword;
    }

    /**
     * The binary content of this message.
     *
     * @return The message content as a string, or {@code null} if
     * {@linkplain #getType()} is not {@linkplain MessageType#BINARY}.
     */
    @JsonProperty("data")
    public String getData() {
        return data;
    }

    /**
     * The hex encoded user data header.
     *
     * @return The user data header as a hex-encoded string, or {@code null} if
     * {@linkplain #getType()} is not {@linkplain MessageType#BINARY}.
     */
    @JsonProperty("udh")
    public String getUdh() {
        return udh;
    }

    /**
     * The UTC±00:00 time when Vonage started to push this inbound message to your webhook endpoint.
     *
     * @return The message timestamp as an Instant.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonProperty("message-timestamp")
    public Instant getMessageTimestamp() {
        return messageTimestamp;
    }

    /**
     * The Unix timestamp representation of {@link #getMessageTimestamp()}.
     *
     * @return The Unix timestamp of the message as a Long, or {@code null} if unavailable.
     */
    @JsonProperty("timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * A random string that adds an extra element of unpredictability into the signature for the request.
     * You use the nonce and timestamp parameters with your shared secret to calculate and validate the
     * signature for inbound messages.
     *
     * @return The nonce, or {@code null} if the message is unsigned.
     */
    @JsonProperty("nonce")
    public String getNonce() {
        return nonce;
    }

    /**
     * Whether the message exceeded the maximum-permitted length of a single message.
     *
     * @return {@code true} for multipart (concatenated) message, or {@code null} if not applicable.
     */
    @JsonProperty("concat")
    public Boolean getConcat() {
        return concat;
    }

    /**
     * The transaction reference. All parts of this message share this reference.
     *
     * @return The concatenation reference, or {@code null} if not applicable.
     */
    @JsonProperty("concat-ref")
    public Integer getConcatRef() {
        return concatRef;
    }

    /**
     * The number of parts in this concatenated message.
     *
     * @return Total number of messages used, or {@code null} if not applicable.
     */
    @JsonProperty("concat-total")
    public Integer getConcatTotal() {
        return concatTotal;
    }

    /**
     * The number of this part in the message. The first part of the message is 1.
     *
     * @return The sequence number (part) for this message, or {@code null} if not applicable.
     */
    @JsonProperty("concat-part")
    public Integer getConcatPart() {
        return concatPart;
    }

    /**
     * Creates an instance of this class from a JSON payload.
     *
     * @param json The JSON string to parse.
     *
     * @return An instance of this class with the fields populated, if present.
     */
    @JsonCreator
    public static MessageEvent fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
