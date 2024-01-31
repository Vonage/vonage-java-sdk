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
package com.vonage.client.sms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.math.BigDecimal;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsSubmissionResponseMessage extends JsonableBaseObject {
    private MessageStatus status;
    private String to, id, network, errorText, clientRef, accountRef;
    private BigDecimal remainingBalance, messagePrice;

    /**
     * @return The number the message was sent to. Numbers are specified in E.164 format.
     */
    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    /**
     * @return The ID of the message.
     */
    @JsonProperty("message-id")
    public String getId() {
        return id;
    }

    /**
     * @return The status of the message. A non-zero code (i.e. anything that isn't
     * {@link MessageStatus#OK} indicates an error. See
     * <a href=https://developer.vonage.com/messaging/sms/guides/troubleshooting-sms>Troubleshooting Failed SMS</a>.
     */
    @JsonProperty("status")
    public MessageStatus getStatus() {
        return status;
    }

    /**
     * @return The description of the error, if present.
     */
    @JsonProperty("error-text")
    public String getErrorText() {
        return errorText;
    }

    /**
     * @return Your estimated remaining balance.
     */
    @JsonProperty("remaining-balance")
    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    /**
     * @return The estimated cost of the message.
     */
    @JsonProperty("message-price")
    public BigDecimal getMessagePrice() {
        return messagePrice;
    }

    /**
     * @return The estimated ID of the network of the recipient.
     */
    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    /**
     * @return If a client-ref was included when sending the SMS,
     * this field will be included and hold the value that was sent.
     */
    @JsonProperty("client-ref")
    public String getClientRef() {
        return clientRef;
    }

    /**
     * This is an advanced feature and requires activation via a support request before it can be used.
     *
     * @return An optional string used to identify separate accounts using the SMS endpoint for billing purposes.
     */
    @JsonProperty("account-ref")
    public String getAccountRef() {
        return accountRef;
    }

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
