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
package com.vonage.client.verify;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.JsonableBaseObject;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VerifyDetails extends JsonableBaseObject {
    private String requestId, accountId, number, senderId, currency;
    private Date dateSubmitted, dateFinalized, firstEventDate, lastEventDate;
    private Status status;
    private BigDecimal price, estimatedPriceMessagesSent;
    private List<VerifyCheck> checks = new ArrayList<>(0);

    /**
     * @return The {@code request_id} that you received in the response to the
     * Verify request and used in the Verify search request.
     */
    @JsonProperty("request_id")
    public String getRequestId() {
        return requestId;
    }

    /**
     * @return The Vonage account ID the request was for.
     */
    @JsonProperty("account_id")
    public String getAccountId() {
        return accountId;
    }

    /**
     * @return The phone number this verification request was used for.
     */
    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    /**
     * @return The {@code sender_id} you provided in the Verify request.
     */
    @JsonProperty("sender_id")
    public String getSenderId() {
        return senderId;
    }

    /**
     * @return The date and time the verification request was submitted.
     */
    @JsonProperty("date_submitted")
    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    /**
     * @return The date and time the verification request was completed.
     */
    @JsonProperty("date_finalized")
    public Date getDateFinalized() {
        return dateFinalized;
    }

    /**
     * @return The date and time the first verification attempt was made.
     */
    @JsonProperty("first_event_date")
    public Date getFirstEventDate() {
        return firstEventDate;
    }

    /**
     * @return The date and time the last verification attempt was made.
     */
    @JsonProperty("last_event_date")
    public Date getLastEventDate() {
        return lastEventDate;
    }

    /**
     * @return The status.
     */
    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    /**
     * @return The cost incurred for this verification request.
     */
    @JsonProperty("price")
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @return The currency code.
     */
    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    /**
     * @return The list of checks made for this verification and their outcomes.
     */
    @JsonProperty("checks")
    public List<VerifyCheck> getChecks() {
        return checks;
    }

    /**
     * @return This field may not be present, depending on your pricing model.
     * The value indicates the cost (in EUR) of the calls made and messages sent for the verification process.
     * This value may be updated during and shortly after the request completes because user input events can
     * overlap with message/call events. When this field is present, the total cost of the verification is the
     * sum of this field and the {@code price} field.
     *
     * @since 7.1.0
     */
    @JsonProperty("estimated_price_messages_sent")
    public BigDecimal getEstimatedPriceMessagesSent() {
        return estimatedPriceMessagesSent;
    }

    public enum Status {
        /**
         * The search is still in progress.
         */
        IN_PROGRESS("IN PROGRESS"),
        /**
         * Your user entered a correct verification code.
         */
        SUCCESS("SUCCESS"),
        /**
         * Your user entered an incorrect code more than three times.
         */
        FAILED("FAILED"),
        /**
         * Your user did not enter a code before the pin_expiry time elapsed.
         */
        EXPIRED("EXPIRED"),
        /**
         * The verification process was cancelled by a Verify control request.
         */
        CANCELLED("CANCELLED"),
        /**
         * You supplied an invalid {@code request_id}, or the data is not available.
         * Note that for recently-completed requests, there can be a delay of up to 1 minute
         * before the results are available in search.
         */
        INVALID("101");

        private final String status;

        private static final Map<String, Status> stringStatusValues =
            Arrays.stream(Status.values()).collect(Collectors.toMap(
                    Status::getStatus, Function.identity()
            ));

        /**
         * Look up the Status enum based on the string value.
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
