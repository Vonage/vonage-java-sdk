/*
 * Copyright (c) 2011-2018 Nexmo Inc
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

import com.fasterxml.jackson.annotation.JsonCreator;
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
    private Status verificationStatus;
    private BigDecimal price;
    private String currency;
    private List<VerifyCheck> checks;

    @JsonCreator
    public VerifyDetails(@JsonProperty("request_id") String requestId,
                         @JsonProperty("account_id") String accountId,
                         @JsonProperty("number") String number,
                         @JsonProperty("sender_id") String senderId,
                         @JsonProperty("date_submitted") Date dateSubmitted,
                         @JsonProperty("date_finalized") Date dateFinalized,
                         @JsonProperty("first_event_date") Date firstEventDate,
                         @JsonProperty("last_event_date") Date lastEventDate,
                         @JsonProperty("status") Status verificationStatus,
                         @JsonProperty("price") BigDecimal price,
                         @JsonProperty("currency") String currency,
                         @JsonProperty(value = "checks", defaultValue = "[]") List<VerifyCheck> checks) {
        this.requestId = requestId;
        this.accountId = accountId;
        this.number = number;
        this.senderId = senderId;
        this.dateSubmitted = dateSubmitted;
        this.dateFinalized = dateFinalized;
        this.firstEventDate = firstEventDate;
        this.lastEventDate = lastEventDate;
        this.verificationStatus = verificationStatus;
        this.price = price;
        this.currency = currency;
        this.checks = checks != null ? checks : new ArrayList<VerifyCheck>();
    }

    public enum Status {
        IN_PROGRESS("IN PROGRESS"), SUCCESS("SUCCESS"), FAILED("FAILED"), EXPIRED("EXPIRED"), CANCELLED("CANCELLED"), INVALID(
                "101");

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
         * @param status the verificationStatus value to lookup.
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

    public String getRequestId() {
        return this.requestId;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String getNumber() {
        return this.number;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public Date getDateSubmitted() {
        return this.dateSubmitted;
    }

    public Date getDateFinalized() {
        return this.dateFinalized;
    }

    public Date getFirstEventDate() {
        return this.firstEventDate;
    }

    public Date getLastEventDate() {
        return this.lastEventDate;
    }

    public Status getVerificationStatus() {
        return this.verificationStatus;
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
}
