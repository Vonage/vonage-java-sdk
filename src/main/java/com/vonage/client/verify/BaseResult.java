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


/**
 * An abstract base class for verification results.
 *
 *
 */
public abstract class BaseResult {
    /**
     * Verify was successfully submitted to the Vonage service
     */
    public static final int STATUS_OK = 0;

    /**
     * Verify was rejected due to exceeding the maximum throughput allowed for this account.<br>
     * Verify can be re-requested after a short delay
     */
    public static final int STATUS_THROTTLED = 1;

    /**
     * Verify was rejected due to incomplete data in the submission request
     */
    public static final int STATUS_MISSING_PARAMS = 2;

    /**
     * Verify was rejected due to an illegal value in one or more elements of the submission request
     */
    public static final int STATUS_INVALID_PARAMS = 3;

    /**
     * Verify was rejected due to receiving invalid account api key and/or secret
     */
    public static final int STATUS_INVALID_CREDENTIALS = 4;

    /**
     * Verify was rejected due to a failure within the Vonage systems.<br>
     * Verify can be re-submitted after a short delay
     */
    public static final int STATUS_INTERNAL_ERROR = 5;

    /**
     * Verify was rejected because the Vonage service was unable to handle this request. eg, the destination was un-routable.
     */
    public static final int STATUS_INVALID_REQUEST = 6;

    /**
     * Verify was rejected because the phone number you tried to submit to has been blacklisted.
     */
    public static final int STATUS_NUMBER_BARRED = 7;

    /**
     * Verify was rejected because your account has been barred, or has not yet been activated
     */
    public static final int STATUS_PARTNER_ACCOUNT_BARRED = 8;

    /**
     * Verify was rejected because your pre-paid balance does not contain enough credit to handle this request.<br>
     * Please top up your balance before re-submitting this request or subsequent requests.
     */
    public static final int STATUS_PARTNER_QUOTA_EXCEEDED = 9;

    /**
     * Verify was rejected because another verification to the same number was already requested.
     */
    public static final int STATUS_ALREADY_REQUESTED = 10;

    /**
     * The destination number is not in a supported network
     */
    public static final int STATUS_UNSUPPORTED_NETWORK = 15;

    /**
     * The code inserted does not match the expected value
     */
    public static final int STATUS_INVALID_CODE = 16;

    /**
     * A wrong code was provided too many times
     */
    public static final int STATUS_WRONG_CODE_THROTTLED = 17;

    /**
     * There are more than the maximum allowed number of destinations in this request
     */
    public static final int STATUS_TOO_MANY_DESTINATIONS = 18;

    /**
     * There are no matching verification requests
     */
    public static final int STATUS_NO_RESPONSE = 101;

    /**
     * A network error occurred
     */
    public static final int STATUS_COMMS_FAILURE = -1;

    private final int status;
    private final String errorText;
    private final boolean temporaryError;

    protected BaseResult(final int status,
                         final String errorText,
                         final boolean temporaryError) {
        this.status = status;
        this.errorText = errorText;
        this.temporaryError = temporaryError;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorText() {
        return errorText;
    }

    public boolean isTemporaryError() {
        return temporaryError;
    }
}
