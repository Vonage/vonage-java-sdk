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
package com.vonage.client.sms.messages;

import com.vonage.client.AbstractQueryParamsRequest;
import java.util.Map;

/**
 * Represents the details common to any message that is to be submitted to the Vonage SMS API.
 */
public abstract class Message extends AbstractQueryParamsRequest {
    private final MessageType type;
    private final String from, to;
    private boolean statusReportRequired;
    private MessageClass messageClass;
    private Long timeToLive;
    private String clientReference, callbackUrl, entityId, contentId;

    protected Message(final MessageType type,
                      final String from,
                      final String to) {
        this(type, from, to, false);
    }

    /**
     * Abstract type for more specific SMS message types.<br>
     * This constructor exposes the full range of possible parameters and is not for general use
     * Instead, it is accessed via super() in the constructors of various subclasses that expose a relevant
     * sub-set of the available parameters.
     *
     * @param type the type of SMS message to be sent.
     * @param from the 'from' address that will be seen on the handset when this message arrives, typically either a
     *             valid short-code / long code that can be replied to, or a short text description of the application
     *             sending the message (Max 15 chars).
     * @param to   the phone number of the handset you wish to send the message to.
     * @param statusReportRequired flag to enable status updates about the delivery of this message.
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
     * The type of message will influence the makeup of the request we post to the Vonage server, and also the
     * action taken by the Vonage server in response to this message.
     *
     * @return The message type as an enum.
     */
    public MessageType getType() {
        return type;
    }

    /**
     * The 'from' address that will be seen on the handset when this message arrives, typically either
     * valid short-code / long code that can be replied to, or a short text description of the application
     * sending the message (maximum 11 characters).
     *
     * @return The message sender ID or number as a string.
     */
    public String getFrom() {
        return from;
    }

    /**
     * The phone number of the handset that you wish to send the message to.
     *
     * @return The recipient phone number as a string, in E.164 format.
     */
    public String getTo() {
        return to;
    }

    /**
     * An optional user definable value that will be stored in the Vonage SMS records. It will be available
     * in detailed reporting and analytics in order to help with reconciliation of messages.
     *
     * @return The client reference as a string, or {@code null} if unspecified.
     */
    public String getClientReference() {
        return clientReference;
    }

    /**
     * Sets the client reference for this message.
     *
     * @param clientReference The custom message reference, maximum 40 characters.
     * @throws IllegalArgumentException if the client reference is longer than 40 characters.
     */
    public void setClientReference(String clientReference) {
        if (clientReference.length() > 40) {
            throw new IllegalArgumentException("Client reference must be 40 characters or less.");
        }
        this.clientReference = clientReference;
    }

    /**
     * The message class that is to be applied to this message.
     *
     * @return The message class as an enum.
     */
    public MessageClass getMessageClass() {
        return messageClass;
    }

    /**
     * Set the message class for this message.
     *
     * @param messageClass The message class as an enum.
     */
    public void setMessageClass(MessageClass messageClass) {
        this.messageClass = messageClass;
    }

    /**
     * The duration in milliseconds the delivery of an SMS will be attempted. By default, Vonage attempts
     * delivery for 72 hours, however the maximum effective value depends on the operator and is typically 24 - 48
     * hours. We recommend this value should be kept at its default or at least 30 minutes.
     *
     * @return The message TTL in milliseconds, or {@code null} if unspecified (the default).
     */
    public Long getTimeToLive() {
        return timeToLive;
    }

    /**
     * The duration in milliseconds the delivery of an SMS will be attempted. By default, Vonage attempts
     * delivery for 72 hours, however the maximum effective value depends on the operator and is typically 24 - 48
     * hours. We recommend this value should be kept at its default or at least 30 minutes.
     *
     * @param timeToLive The message TTL in milliseconds.
     */
    public void setTimeToLive(Long timeToLive) {
        this.timeToLive = timeToLive;
    }

    /**
     * The webhook endpoint the delivery receipt for this SMS is sent to. This parameter overrides the webhook
     * endpoint you set in the Dashboard. Maximum 100 characters.
     *
     * @return The callback URL as a string, or {@code null} if unspecified (the default).
     */
    public String getCallbackUrl() {
        return callbackUrl;
    }

    /**
     * The webhook endpoint the delivery receipt for this SMS is sent to. This parameter overrides the webhook
     * endpoint you set in the Dashboard. Maximum 100 characters.
     *
     * @param callbackUrl The callback URL to send the delivery receipt to for this message.
     */
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    /**
     * A parameter that satisfies regulatory requirements when sending an SMS to specific countries.
     *
     * @return The entity ID as a string, or {@code null} if unspecified.
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * Sets a parameter that satisfies regulatory requirements when sending an SMS to specific countries.
     *
     * @param entityId The entity ID as a string.
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    /**
     * A parameter that satisfies regulatory requirements when sending an SMS to specific countries.
     *
     * @return The content ID as a string, or {@code null} if unspecified.
     */
    public String getContentId() {
        return contentId;
    }

    /**
     * Sets a parameter that satisfies regulatory requirements when sending an SMS to specific countries.
     *
     * @param contentId The content ID as a string.
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    /**
     * Boolean indicating if you'd like to receive a Delivery Receipt.
     *
     * @return {@code true} if a delivery receipt should be requested, {@code false} otherwise.
     */
    public boolean getStatusReportRequired() {
        return statusReportRequired;
    }

    /**
     * Boolean indicating if you like to receive a Delivery Receipt.
     * If set to {@code true}, Vonage will call 'callbackUrl' with status updates about the delivery of this message.
     * Thus, the {@linkplain #setCallbackUrl(String)} should be configured if a global one isn't set already.
     *
     * @param statusReportRequired {@code true} if status reports are desired, {@code false} otherwise.
     */
    public void setStatusReportRequired(boolean statusReportRequired) {
        this.statusReportRequired = statusReportRequired;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        conditionalAdd("from", from);
        conditionalAdd("to", to);
        conditionalAdd("type", type);
        if (getStatusReportRequired()) {
            conditionalAdd("status-report-req", "1");
        }
        conditionalAdd("client-ref", clientReference);
        conditionalAdd("ttl", timeToLive);
        conditionalAdd("callback", callbackUrl);
        if (messageClass != null) {
            conditionalAdd("message-class", messageClass.getMessageClass());
        }
        conditionalAdd("entity-id", entityId);
        conditionalAdd("content-id", contentId);
        return params;
    }

}
