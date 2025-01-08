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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.math.BigDecimal;

/**
 * Represents the API response for a single SMS submission.
 */
public class SmsSubmissionResponseMessage extends JsonableBaseObject {
    private MessageStatus status;
    private String to, id, network, errorText, clientRef, accountRef;
    private BigDecimal remainingBalance, messagePrice;

    /**
     * The number the message was sent to.
     *
     * @return The receiving number in E.164 format.
     */
    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    /**
     * ID of the message.
     *
     * @return The message ID as a string.
     */
    @JsonProperty("message-id")
    public String getId() {
        return id;
    }

    /**
     * Status of the message. A non-zero code (i.e. anything that isn't {@link MessageStatus#OK}) indicates an
     * error. See <a href=https://developer.vonage.com/messaging/sms/guides/troubleshooting-sms>
     * Troubleshooting Failed SMS</a> for more details.
     *
     * @return The message status as an enum.
     */
    @JsonProperty("status")
    public MessageStatus getStatus() {
        return status;
    }

    /**
     * Error description, if present.
     *
     * @return The description of the error, or {@code null} if not applicable.
     */
    @JsonProperty("error-text")
    public String getErrorText() {
        return errorText;
    }

    /**
     * Estimated account remaining balance.
     *
     * @return The remaining balance as a {@link BigDecimal}.
     */
    @JsonProperty("remaining-balance")
    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    /**
     * Estimated cost of the message.
     *
     * @return The message price as a {@link BigDecimal}.
     */
    @JsonProperty("message-price")
    public BigDecimal getMessagePrice() {
        return messagePrice;
    }

    /**
     * Estimated ID of the network of the recipient.
     *
     * @return The recipient's network ID as a string, or {@code null} if unknown.
     */
    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    /**
     * If a client-ref was included when sending the SMS, this field will be the value that was sent.
     *
     * @return The reference associated with the message as a string, or {@code null} if there wasn't one set.
     */
    @JsonProperty("client-ref")
    public String getClientRef() {
        return clientRef;
    }

    /**
     * Account reference. An optional string used to identify separate accounts using the SMS endpoint for billing
     * purposes. This is an advanced feature and requires activation via a support request before it can be used.
     *
     * @return The account reference, or {@code null} if not applicable.
     */
    @JsonProperty("account-ref")
    public String getAccountRef() {
        return accountRef;
    }

    /**
     * Convenience method for checking if the message status indicates a temporary error.
     *
     * @return {@code true} if the status is a temporary error, {@code false} otherwise.
     */
    public boolean isTemporaryError() {
        return status == MessageStatus.INTERNAL_ERROR || status == MessageStatus.TOO_MANY_BINDS
                || status == MessageStatus.THROTTLED;
    }

    @Override
    public String toString() {
        return "SmsSubmissionResponseMessage {" +
                "to='" + to + '\'' +
                ", id='" + id + '\'' +
                ", status=" + status +
                ", remainingBalance=" + remainingBalance +
                ", messagePrice=" + messagePrice +
                ", network='" + network + '\'' +
                ", errorText='" + errorText + '\'' +
                ", clientRef='" + clientRef + '\'' +
                '}';
    }
}
