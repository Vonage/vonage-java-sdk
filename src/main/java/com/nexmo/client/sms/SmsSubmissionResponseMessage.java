/*
 * Copyright (c) 2020 Vonage
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
package com.nexmo.client.sms;

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
        return this.to;
    }

    @JsonProperty("message-id")
    public String getId() {
        return this.id;
    }

    @JsonProperty("status")
    public MessageStatus getStatus() {
        return this.status;
    }

    @JsonProperty("error-text")
    public String getErrorText() {
        return this.errorText;
    }

    @JsonProperty("client-ref")
    public String getClientRef() {
        return this.clientRef;
    }

    @JsonProperty("remaining-balance")
    public BigDecimal getRemainingBalance() {
        return this.remainingBalance;
    }

    @JsonProperty("message-price")
    public BigDecimal getMessagePrice() {
        return this.messagePrice;
    }

    @JsonProperty("network")
    public String getNetwork() {
        return this.network;
    }

    public boolean isTemporaryError() {
        return this.status == MessageStatus.INTERNAL_ERROR || this.status == MessageStatus.TOO_MANY_BINDS
                || this.status == MessageStatus.THROTTLED;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
