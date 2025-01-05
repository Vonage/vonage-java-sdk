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
package com.vonage.client.verify;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonDeserialize(using = VerifyStatusDeserializer.class)
public enum VerifyStatus {
    /**
     * The request was successfully accepted by Vonage.
     */
    OK(0),

    /**
     * You are trying to send more than the maximum of 30 requests per second.
     */
    THROTTLED(1),

    /**
     * Your request is incomplete and missing the mandatory parameter(s).
     */
    MISSING_PARAMS(2),

    /**
     * Invalid value for parameter. If you see Facility not allowed in the error text,
     * check that you are using the correct Base URL in your request.
     */
    INVALID_PARAMS(3),

    /**
     * The supplied API key or secret in the request is either invalid or disabled.
     */
    INVALID_CREDENTIALS(4),

    /**
     * An error occurred processing this request in the Cloud Communications Platform.
     */
    INTERNAL_ERROR(5),

    /**
     * The request could not be routed.
     */
    INVALID_REQUEST(6),

    /**
     * The number you are trying to verify is blacklisted for verification.
     */
    NUMBER_BARRED(7),

    /**
     * The api_key you supplied is for an account that has been barred from submitting messages.
     */
    PARTNER_ACCOUNT_BARRED(8),

    /**
     * Your account does not have sufficient credit to process this request.
     */
    PARTNER_QUOTA_EXCEEDED(9),

    /**
     * Concurrent verifications to the same number are not allowed.
     */
    ALREADY_REQUESTED(10),

    /**
     * The request has been rejected. Find out more about this error in the <a href=
     * https://help.nexmo.com/hc/en-us/articles/360018406532-Verify-On-demand-Service-to-High-Risk-CountriesKnowledge
     * >Knowledge Base</a>.
     */
    UNSUPPORTED_NETWORK(15),

    /**
     * The code inserted does not match the expected value.
     */
    INVALID_CODE(16),

    /**
     * You can run Verify check on a specific {@code request_id} up to three times unless a new verification code
     * is generated. If you check a request more than three times, it is set to FAILED and you cannot check it again.
     */
    WRONG_CODE_THROTTLED(17),

    /**
     * For {@code cancel}: Either you have not waited at least 30 seconds after sending a Verify request
     * before cancelling or Verify has made too many attempts to deliver the verification code for this request,
     * and you must now wait for the process to complete. For {@code trigger_next_event}: All attempts to
     * deliver the verification code for this request have completed and there are no remaining events to advance to.
     */
    WAIT_FOR_COMPLETION(19),

    /**
     * Only certain accounts have the ability to set the {@code pin_code} parameter.
     * Please contact your account manager for more information.
     */
    UNSUPPORTED_PIN_CODE(20),

    /**
     * Your Vonage account is still in demo mode. While in demo mode you must add target numbers to the
     * approved list for your account. Add funds to your account tO remove this limitation.
     */
    NON_PERMITTED_DESTINATION(29),

    /**
     * Undefined or unknown value.
     */
    UNKNOWN(Integer.MAX_VALUE);

    private final int verifyStatus;

    private static final Map<Integer, VerifyStatus> VERIFY_STATUS_INDEX =
        Arrays.stream(VerifyStatus.values()).collect(Collectors.toMap(
                VerifyStatus::getVerifyStatus, Function.identity()
        ));

    /**
     * Look up the VerifyStatus based on the int value.
     *
     * @param verifyStatus the int value of the verify status.
     *
     * @return VerifyStatus based on the int value given.
     */
    public static VerifyStatus fromInt(int verifyStatus) {
        return VERIFY_STATUS_INDEX.getOrDefault(verifyStatus, UNKNOWN);
    }

    VerifyStatus(int verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    @JsonValue
    public int getVerifyStatus() {
        return verifyStatus;
    }
}
