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
package com.vonage.client.sms.callback.messages;

import java.math.BigDecimal;
import java.util.Date;



/**
 * This represents an incoming MO callback request
 *
 *
 */
public class MO implements java.io.Serializable {

    private static final long serialVersionUID = 6978599736439760428L;

    private final String messageId;
    private final MESSAGE_TYPE messageType;
    private final String sender;
    private final String destination;
    private final Date timeStamp;

    private String networkCode;
    private String keyword;
    private String messageBody;
    private byte[] binaryMessageBody;
    private byte[] userDataHeader;
    private BigDecimal price;
    private String sessionId;

    private boolean concat;
    private String concatReferenceNumber;
    private int concatTotalParts;
    private int concatPartNumber;

    /**
     * Describes the type of payload this message carries
     */
    public enum MESSAGE_TYPE {

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

        MESSAGE_TYPE(final String type) {
            this.type = type;
        }

        /**
         * @return String A descriptive value representing this type
         */
        public String getType() {
            return type;
        }

    }

    public MO(final String messageId,
              final MESSAGE_TYPE messageType,
              final String sender,
              final String destination,
              final BigDecimal price,
              final Date timeStamp) {
        this.messageId = messageId;
        this.messageType = messageType;
        this.sender = sender;
        this.destination = destination;
        this.price = price;
        this.timeStamp = timeStamp;

        // Set the rest to defaults:

        // text & unicode:
        messageBody = null;
        keyword = null;

        // binary:
        binaryMessageBody = null;
        userDataHeader = null;

        // concatenation data:
        concat = false;
        concatReferenceNumber = null;
        concatTotalParts = 1;
        concatPartNumber = 1;

        // TODO: UNDOCUMENTED
        networkCode = null;
        sessionId = null;
    }

    public void setTextData(String text,
                            String keyword) {
        messageBody = text;
        this.keyword = keyword;
    }

    public void setBinaryData(byte[] binaryMessageBody, byte[] userDataHeader) {
        this.binaryMessageBody = binaryMessageBody;
        this.userDataHeader = userDataHeader;
    }

    public void setConcatenationData(String concatReferenceNumber, int concatTotalParts, int concatPartNumber) {
        concat = true;
        this.concatReferenceNumber = concatReferenceNumber;
        this.concatTotalParts = concatTotalParts;
        this.concatPartNumber = concatPartNumber;
    }

    public void setNetworkCode(String networkCode) {
        this.networkCode = networkCode;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    //    public MO(final String messageId,
//              final MESSAGE_TYPE messageType,
//              final String sender,
//              final String destination,
//              final String networkCode,
//              final String keyword,
//              final String messageBody,
//              final byte[] binaryMessageBody,
//              final byte[] userDataHeader,
//              final BigDecimal price,
//              final String sessionId,
//              final boolean concat,
//              final String concatReferenceNumber,
//              final int concatTotalParts,
//              final int concatPartNumber,
//              final Date timeStamp) {
//        this.messageId = messageId;
//        this.messageType = messageType;
//        this.sender = sender;
//        this.destination = destination;
//        this.networkCode = networkCode;
//        this.keyword = keyword;
//        this.messageBody = messageBody;
//        this.binaryMessageBody = binaryMessageBody;
//        this.userDataHeader = userDataHeader;
//        this.price = price;
//        this.sessionId = sessionId;
//        this.concat = concat;
//        this.concatReferenceNumber = concatReferenceNumber;
//        this.concatTotalParts = concatTotalParts;
//        this.concatPartNumber = concatPartNumber;
//        this.timeStamp = timeStamp;
//    }

    /**
     * @return String the id assigned to this message by Vonage before delivery
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @return MESSAGE_TYPE describes what type of payload this message carries, eg, 8 bit text, unicode text or raw binary
     */
    public MESSAGE_TYPE getMessageType() {
        return messageType;
    }

    /**
     * @return String the phone number of the end user that sent this message
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return String the short-code/long code number that the end user sent the message to
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @return String the network code (if available) of the end user
     */
    public String getNetworkCode() {
        return networkCode;
    }

    /**
     * @return String return the first keyword of the message. If this is a shared short-code then this is what the message will have been routed by.
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @return String The message payload if this is a TEXT or UNICODE message
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * @return byte[] the raw binary payload if this is a BINARY message
     */
    public byte[] getBinaryMessageBody() {
        return binaryMessageBody;
    }

    /**
     * @return byte[] the raw binary user-data-header if applicable for this message
     */
    public byte[] getUserDataHeader() {
        return userDataHeader;
    }

    /**
     * @return BigDecimal if a price was charged for receiving this message, then that is available here
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @return String if this field is populated, then the value should be returned in any MT response
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @return boolean is this message part of a concatenated message that needs re-assembly
     */
    public boolean isConcat() {
        return concat;
    }

    /**
     * @return String if this message is part of a concatenated set, then this is the reference id that groups the parts together
     */
    public String getConcatReferenceNumber() {
        return concatReferenceNumber;
    }

    /**
     * @return String if this message is part of a concatenated set, then this is the total number of parts in the set
     */
    public int getConcatTotalParts() {
        return concatTotalParts;
    }

    /**
     * @return String if this message is part of a concatenated set, then this is the 'part number' within the set that this message carries
     */
    public int getConcatPartNumber() {
        return concatPartNumber;
    }

    /**
     * @return the timestamp this message was originally received by Vonage
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

}
