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
package com.vonage.client.sms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsSubmissionResponseMessage {
    private String to;
    private String id;
    private MessageStatus status;
    private BigDecimal remainingBalance;
    private BigDecimal messagePrice;
    private String network;
    private String errorText;
    private String clientRef;

    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    @JsonProperty("message-id")
    public String getId() {
        return id;
    }

    @JsonProperty("status")
    public MessageStatus getStatus() {
        return status;
    }

    @JsonProperty("error-text")
    public String getErrorText() {
        return errorText;
    }

    @JsonProperty("client-ref")
    public String getClientRef() {
        return clientRef;
    }

    @JsonProperty("remaining-balance")
    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    @JsonProperty("message-price")
    public BigDecimal getMessagePrice() {
        return messagePrice;
    }

    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    public boolean isTemporaryError() {
        return status == MessageStatus.INTERNAL_ERROR || status == MessageStatus.TOO_MANY_BINDS
                || status == MessageStatus.THROTTLED;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
