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
package com.vonage.client.sms.messages;


import org.apache.http.client.methods.RequestBuilder;

/**
 * Represents the details common to any message that is to be submitted to the Vonage SMS API.
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
    private boolean statusReportRequired;
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
     *             sending the message (Max 15 chars)
     * @param to   the phone number of the handset you wish to send the message to
     * @param statusReportRequired flag to enable status updates about the delivery of this message
     */
    protected Message(final MessageType type,
                      final String from,
                      final String to,
                      final boolean statusReportRequired) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.statusReportRequired = statusReportRequired;
    }

    /**
     * @return int the type of message will influence the makeup of the request we post to the Vonage server, and also the action taken by the Vonage server in response to this message
     */
    public MessageType getType() {
        return type;
    }

    /**
     * @return String the 'from' address that will be seen on the handset when this message arrives,
     * typically either a valid short-code / long code that can be replied to, or a short text description of the application sending the message (Max 11 chars)
     */
    public String getFrom() {
        return from;
    }

    /**
     * @return String the phone number of the handset that you wish to send the message to
     */
    public String getTo() {
        return to;
    }

    /**
     * @return String A user definable value that will be stored in the Vonage sms records. It will
     * be available in detailed reporting &amp; analytics in order to help with reconciliation of messages
     */
    public String getClientReference() {
        return clientReference;
    }

    public void setClientReference(String clientReference) {
        if (clientReference.length() > 40) {
            throw new IllegalArgumentException("Client reference must be 40 characters or less.");
        }
        this.clientReference = clientReference;
    }

    /**
     * @return {@link MessageClass} The message class that is to be applied to this message.
     */
    public MessageClass getMessageClass() {
        return messageClass;
    }

    public void setMessageClass(MessageClass messageClass) {
        messageClass = messageClass;
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
     * @return get the value of the 'status-report-req' parameter.
     */
    public boolean getStatusReportRequired() {
        return statusReportRequired;
    }

    /**
     * Set the value of the 'status-report-req' parameter.
     *
     * If set to 'true', Vonage will call 'callbackUrl' with status updates about the delivery of this message. If this
     * value is set to 'true', then 'callbackUrl' should also be set to a URL that is configured to receive these
     * status updates.
     *
     * @param statusReportRequired 'true' if status reports are desired, 'false' otherwise.
     */
    public void setStatusReportRequired(boolean statusReportRequired) {
        this.statusReportRequired = statusReportRequired;
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

    /**
     * An enum of the valid values that may be supplied to as the message-class parameter of a rest submission.
     *
     *
     */
    public enum MessageClass {

        /**
         * Message Class 0
         */
        CLASS_0(0),

        /**
         * Message Class 1
         */
        CLASS_1(1),

        /**
         * Message Class 2
         */
        CLASS_2(2),

        /**
         * Message Class 3
         */
        CLASS_3(3);

        private final int messageClass;

        MessageClass(int messageClass) {
            this.messageClass = messageClass;
        }

        public int getMessageClass() {
            return messageClass;
        }

    }
}
