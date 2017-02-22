/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
package com.nexmo.client.messaging.messages;


import com.nexmo.client.messaging.messages.parameters.MessageClass;
import org.apache.http.client.methods.RequestBuilder;

/**
 * Represents the details common to any message that is to be submitted to the Nexmo SMS API.
 */
public abstract class Message {
    public enum MessageType {
        /**
         * Message is a regular TEXT SMS message
         */
        TEXT,
        /**
         * Message is a binary SMS message with a custom UDH and binary payload
         */
        BINARY,
        /**
         * Message is a wap-push message to send a browsable / downloadable url to the handset
         */
        WAPPUSH,
        /**
         * Message is a unicode message, for sending messages in non-latin script to a supported handset
         */
        UNICODE;

        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    private final MessageType type;
    private final String from;
    private final String to;

    private String clientReference;
    private boolean statusReportRequired = false;
    private MessageClass messageClass = null;
    private Long timeToLive = null;
    private String callbackUrl = null;

    protected Message(final MessageType type,
                      final String from,
                      final String to) {
        this(type, from, to, false);
    }

    /**
     * Abstract type for more specific SMS message types.<br>
     * This constructor exposes the full range of possible parameters and is not for general use
     * Instead, it is accessed via super() in the constructors of various sub-classes that expose a relevant
     * sub-set of the available parameters
     *
     * @param type the type of SMS message to be sent
     * @param from the 'from' address that will be seen on the handset when this message arrives, typically either a
     *             valid short-code / long code that can be replied to, or a short text description of the application
     *             sending the message (Max 11 chars)
     * @param to   the phone number of the handset you wish to send the message to
     */
    protected Message(final MessageType type,
                      final String from,
                      final String to,
                      final boolean statusReportRequired) {
        if (from.length() > 11) {
            throw new IllegalArgumentException("The length of the 'from' argument must be 11 characters or fewer.");
        }

        this.type = type;
        this.from = from;
        this.to = to;
        this.statusReportRequired = statusReportRequired;
    }

    /**
     * @return int the type of message will influence the makeup of the request we post to the Nexmo server, and also the action taken by the Nexmo server in response to this message
     */
    public MessageType getType() {
        return this.type;
    }

    /**
     * @return String the 'from' address that will be seen on the handset when this message arrives,
     * typically either a valid short-code / long code that can be replied to, or a short text description of the application sending the message (Max 11 chars)
     */
    public String getFrom() {
        return this.from;
    }

    /**
     * @return String the phone number of the handset that you wish to send the message to
     */
    public String getTo() {
        return this.to;
    }

    /**
     * @return String A user definable value that will be stored in the Nexmo messaging records. It will
     * be available in detailed reporting &amp; analytics in order to help with reconciliation of messages
     */
    public String getClientReference() {
        return this.clientReference;
    }

    public void setClientReference(String clientReference) {
        if (clientReference.length() > 40) {
            throw new IllegalArgumentException("Client reference must be 40 characters or less.");
        }
        this.clientReference = clientReference;
    }

    /**
     * @return com.nexmo.messaging.verify.messages.parameters.MessageClass The message class that is to be applied to this message.
     */
    public MessageClass getMessageClass() {
        return this.messageClass;
    }

    public void setMessageClass(MessageClass messageClass) {
        this.messageClass = messageClass;
    }

    public Long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    /**
     * @return boolean if set to true, then a delivery notification will be requested for this message delivery attempt.
     * upon receiving notification of delivery or failure from the network, the nexmo platform will submit a notification to the url configured in your
     * nexmo rest account that represents the outcome of this message.
     */
    public boolean getStatusReportRequired() {
        return this.statusReportRequired;
    }

    public void addParams(RequestBuilder request) {
        request.addParameter("from", getFrom())
                .addParameter("to", getTo())
                .addParameter("type", getType().toString());
        if (statusReportRequired) {
            request.addParameter("status-report-req", "1");
        }
        if (clientReference != null) {
            request.addParameter("client-ref", clientReference);
        }
        if (timeToLive != null) {
            request.addParameter("ttl", timeToLive.toString());
        }
        if (callbackUrl != null) {
            request.addParameter("callback", callbackUrl);
        }
        if (messageClass != null) {
            request.addParameter("message-class", Integer.toString(messageClass.getMessageClass()));
        }
    }
}
