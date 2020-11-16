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
package com.vonage.client.verify;

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
        return requestId;
    }

    @JsonProperty("account_id")
    public String getAccountId() {
        return accountId;
    }

    public String getNumber() {
        return number;
    }

    @JsonProperty("sender_id")
    public String getSenderId() {
        return senderId;
    }

    @JsonProperty("date_submitted")
    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    @JsonProperty("date_finalized")
    public Date getDateFinalized() {
        return dateFinalized;
    }

    @JsonProperty("first_event_date")
    public Date getFirstEventDate() {
        return firstEventDate;
    }

    @JsonProperty("last_event_date")
    public Date getLastEventDate() {
        return lastEventDate;
    }

    public Status getStatus() {
        return status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public List<VerifyCheck> getChecks() {
        return checks;
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
            return status;
        }
    }
}
