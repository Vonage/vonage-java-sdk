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
package com.vonage.client.insight;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum InsightStatus {
    /**
     * Request accepted for delivery.
     */
    SUCCESS(0),
    /**
     * You have made more requests in the last second than are permitted by your account. Please retry.
     */
    THROTTLED(1),
    /**
     * Your request is incomplete and missing some mandatory parameters.
     */
    INVALID_PARAMS(3),
    /**
     * The api_key or api_secret you supplied is either not valid or has been disabled.
     */
    INVALID_CREDENTIALS(4),
    /**
     * The format of the recipient address is not valid.
     */
    INTERNAL_ERROR(5),
    /**
     * Your account does not have sufficient credit to process this request.
     */
    PARTNER_QUOTA_EXCEEDED(9),
    /**
     * Your request makes use of a facility that is not enabled on your account.
     */
    FACILITY_NOT_ALLOWED(19),
    /**
     * Live mobile lookup not returned. Not all return parameters are available.
     */
    LOOKUP_NOT_RETURNED_43(43),
    /**
     * Live mobile lookup not returned. Not all return parameters are available.
     */
    LOOKUP_NOT_RETURNED_44(44),
    /**
     * Live mobile lookup not returned. Not all return parameters are available.
     */
    LOOKUP_NOT_RETURNED_45(45),
    /**
     * Request unparseable.
     */
    REQUEST_UNPARSEABLE(999),
    /**
     * Undefined or unknown value.
     */
    UNKNOWN(Integer.MAX_VALUE);

    private final int statusCode;

    InsightStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Look up the {@link InsightStatus} based on the int value.
     *
     * @param insightStatus the int value of the insight status.
     *
     * @return InsightStatus based on the int value given.
     */
    @JsonCreator
    public static InsightStatus fromInt(int insightStatus) {
        return Arrays.stream(InsightStatus.values())
                .filter(status -> status.statusCode == insightStatus)
                .findAny().orElse(UNKNOWN);
    }

    /**
     * @return The status code used to create this enum.
     */
    public int getInsightStatus() {
        return statusCode;
    }
}
