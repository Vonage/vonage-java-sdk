package com.nexmo.verify.sdk;
/*
 * Copyright (c) 2011-2013 Nexmo Inc
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

/**
 * Verification base result class.
 * @author Daniele Ricci
 */
public abstract class BaseResult implements java.io.Serializable {

    private static final long serialVersionUID = -2501134793517817181L;

    /**
     * Verify was successfully submitted to the Nexmo service
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
     * Verify was rejected due to a failure within the Nexmo systems.<br>
     * Verify can be re-submitted after a short delay
     */
    public static final int STATUS_INTERNAL_ERROR = 5;

    /**
     * Verify was rejected because the Nexmo service was unable to handle this request. eg, the destination was un-routable.
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
     * A network error occured
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
        return this.status;
    }

    public String getErrorText() {
        return this.errorText;
    }

    public boolean isTemporaryError() {
        return this.temporaryError;
    }
}
