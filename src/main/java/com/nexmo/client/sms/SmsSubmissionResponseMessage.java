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
package com.nexmo.client.sms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

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

    @JsonCreator
    SmsSubmissionResponseMessage(@JsonProperty("to") String to,
                                 @JsonProperty("message-id") String id,
                                 @JsonProperty(value = "status", required = true) MessageStatus status,
                                 @JsonProperty("remaining-balance") String remainingBalance,
                                 @JsonProperty("message-price") String messagePrice,
                                 @JsonProperty("network") String network,
                                 @JsonProperty("error-text") String errorText,
                                 @JsonProperty("client-ref") String clientRef) {

        // Kind of hacky, but the XML response used null instead of empty strings.
        this.to = (StringUtils.isNotBlank(to)) ? to : null;
        this.id = (StringUtils.isNotBlank(id)) ? id : null;;
        this.status = status;

        // Again, this is just to emulate how the XML response was working
        try {
            this.remainingBalance = (remainingBalance != null) ? new BigDecimal(remainingBalance) : null;
            this.messagePrice = (messagePrice != null) ? new BigDecimal(messagePrice) : null;
        } catch (NumberFormatException nfe) {
            this.remainingBalance = null;
            this.messagePrice = null;
        }
        this.network = (StringUtils.isNotBlank(network)) ? network : null;;
        this.clientRef = (StringUtils.isNotBlank(clientRef)) ? clientRef : null;;
        this.errorText = (StringUtils.isNotBlank(errorText)) ? errorText : null;;
    }

    public String getTo() {
        return this.to;
    }

    public String getId() {
        return this.id;
    }

    public MessageStatus getStatus() {
        return this.status;
    }

    public String getErrorText() {
        return this.errorText;
    }

    public String getClientRef() {
        return this.clientRef;
    }

    public BigDecimal getRemainingBalance() {
        return this.remainingBalance;
    }

    public BigDecimal getMessagePrice() {
        return this.messagePrice;
    }

    public String getNetwork() {
        return this.network;
    }

    public boolean getTemporaryError() {
        return this.status == MessageStatus.STATUS_INTERNAL_ERROR
                || this.status == MessageStatus.STATUS_TOO_MANY_BINDS
                || this.status == MessageStatus.STATUS_THROTTLED;
    }
}
