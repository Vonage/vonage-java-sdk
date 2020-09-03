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
package com.nexmo.client.verify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyDetails {

    private String requestId;
    private String accountId;
    private String number;
    private String senderId;
    private Date dateSubmitted;
    private Date dateFinalized;
    private Date firstEventDate;
    private Date lastEventDate;
    private Status status;
    private BigDecimal price;
    private String currency;
    private List<VerifyCheck> checks = new ArrayList<>();

    @JsonProperty("request_id")
    public String getRequestId() {
        return this.requestId;
    }

    @JsonProperty("account_id")
    public String getAccountId() {
        return this.accountId;
    }

    public String getNumber() {
        return this.number;
    }

    @JsonProperty("sender_id")
    public String getSenderId() {
        return this.senderId;
    }

    @JsonProperty("date_submitted")
    public Date getDateSubmitted() {
        return this.dateSubmitted;
    }

    @JsonProperty("date_finalized")
    public Date getDateFinalized() {
        return this.dateFinalized;
    }

    @JsonProperty("first_event_date")
    public Date getFirstEventDate() {
        return this.firstEventDate;
    }

    @JsonProperty("last_event_date")
    public Date getLastEventDate() {
        return this.lastEventDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public String getCurrency() {
        return this.currency;
    }

    public List<VerifyCheck> getChecks() {
        return this.checks;
    }

    public enum Status {
        IN_PROGRESS("IN PROGRESS"),
        SUCCESS("SUCCESS"),
        FAILED("FAILED"),
        EXPIRED("EXPIRED"),
        CANCELLED("CANCELLED"),
        INVALID("101");

        private String status;

        private static Map<String, Status> stringStatusValues = new HashMap<>();

        static {
            for (Status status : Status.values()) {
                stringStatusValues.put(status.status, status);
            }
        }

        /**
         * Look up the {@link Status} based on the string value.
         *
         * @param status the status value to lookup.
         * @return VerifyStatus based on the int value given.
         */
        public static Status fromString(String status) {
            return stringStatusValues.get(status);
        }

        Status(String status) {
            this.status = status;
        }

        @JsonValue
        public String getStatus() {
            return this.status;
        }
    }
}
