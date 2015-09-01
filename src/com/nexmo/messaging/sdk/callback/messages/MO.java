package com.nexmo.messaging.sdk.callback.messages;

import java.math.BigDecimal;
import java.util.Date;

/*
 * Copyright (c) 2011-2013 Nexmo Inc
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

/**
 * MO.java
 *
 * This represents an incoming MO callback request
 *
 * @author  Paul Cook
 * @version
 */
public class MO implements java.io.Serializable {

    private static final long serialVersionUID = 6978599736439760428L;

    private final String messageId;
    private final MESSAGE_TYPE messageType;
    private final String sender;
    private final String destination;
    private final String networkCode;
    private final String keyword;
    private final String messageBody;
    private final byte[] binaryMessageBody;
    private final byte[] userDataHeader;
    private final BigDecimal price;
    private final String sessionId;

    private final boolean concat;
    private final String concatReferenceNumber;
    private final int concatTotalParts;
    private final int concatPartNumber;

    private final Date timeStamp;

    /**
     * Describes the type of payload this message carries
     */
    public static enum MESSAGE_TYPE {

        /**
         * This is a plain text (8 bit alphabet) message
         */
        TEXT ("text"),

        /**
         * This is a raw binary message
         */
        BINARY ("binary"),

        /**
         * This is a unicode message
         */
        UNICODE ("unicode");

        final String type;

        private MESSAGE_TYPE(final String type) {
            this.type = type;
        }

        /**
         * @return String A descriptive value representing this type
         */
        public String getType() {
            return this.type;
        }

    }

    public MO(final String messageId,
              final MESSAGE_TYPE messageType,
              final String sender,
              final String destination,
              final String networkCode,
              final String keyword,
              final String messageBody,
              final byte[] binaryMessageBody,
              final byte[] userDataHeader,
              final BigDecimal price,
              final String sessionId,
              final boolean concat,
              final String concatReferenceNumber,
              final int concatTotalParts,
              final int concatPartNumber,
              final Date timeStamp) {
        this.messageId = messageId;
        this.messageType = messageType;
        this.sender = sender;
        this.destination = destination;
        this.networkCode = networkCode;
        this.keyword = keyword;
        this.messageBody = messageBody;
        this.binaryMessageBody = binaryMessageBody;
        this.userDataHeader = userDataHeader;
        this.price = price;
        this.sessionId = sessionId;
        this.concat = concat;
        this.concatReferenceNumber = concatReferenceNumber;
        this.concatTotalParts = concatTotalParts;
        this.concatPartNumber = concatPartNumber;
        this.timeStamp = timeStamp;
    }

    /**
     * @return String the id assigned to this message by Nexmo before delivery
     */
    public String getMessageId() {
        return this.messageId;
    }

    /**
     * @return MESSAGE_TYPE describes what type of payload this message carries, eg, 8 bit text, unicode text or raw binary
     */
    public MESSAGE_TYPE getMessageType() {
        return this.messageType;
    }

    /**
     * @return String the phone number of the end user that sent this message
     */
    public String getSender() {
        return this.sender;
    }

    /**
     * @return String the short-code/long code number that the end user sent the message to
     */
    public String getDestination() {
        return this.destination;
    }

    /**
     * @return String the network code (if available) of the end user
     */
    public String getNetworkCode() {
        return this.networkCode;
    }

    /**
     * @return String return the first keyword of the message. If this is a shared short-code then this is what the message will have been routed by.
     */
    public String getKeyword() {
        return this.keyword;
    }

    /**
     * @return String The message payload if this is a TEXT or UNICODE message
     */
    public String getMessageBody() {
        return this.messageBody;
    }

    /**
     * @return byte[] the raw binary payload if this is a BINARY message
     */
    public byte[] getBinaryMessageBody() {
        return this.binaryMessageBody;
    }

    /**
     * @return byte[] the raw binary user-data-header if applicable for this message
     */
    public byte[] getUserDataHeader() {
        return this.userDataHeader;
    }

    /**
     * @return BigDecimal if a price was charged for receiving this message, then that is available here
     */
    public BigDecimal getPrice() {
        return this.price;
    }

    /**
     * @return String if this field is populated, then the value should be returned in any MT response
     */
    public String getSessionId() {
        return this.sessionId;
    }

    /**
     * @return boolean is this message part of a concatenated message that needs re-assembly
     */
    public boolean isConcat() {
        return this.concat;
    }

    /**
     * @return String if this message is part of a concatenated set, then this is the reference id that groups the parts together
     */
    public String getConcatReferenceNumber() {
        return this.concatReferenceNumber;
    }

    /**
     * @return String if this message is part of a concatenated set, then this is the total number of parts in the set
     */
    public int getConcatTotalParts() {
        return this.concatTotalParts;
    }

    /**
     * @return String if this message is part of a concatenated set, then this is the 'part number' within the set that this message carries
     */
    public int getConcatPartNumber() {
        return this.concatPartNumber;
    }

    /**
     * @return the timestamp this message was originally received by Nexmo
     */
    public Date getTimeStamp() {
        return this.timeStamp;
    }

}
